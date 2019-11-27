package cn.vansz.opencv.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.SeekBar
import cn.vansz.opencv.R
import kotlinx.android.synthetic.main.activity_image_blur.*
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

class ImageBlurActivity : BaseActivity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private lateinit var originBitmap: Bitmap
    override fun layoutResID(): Int = R.layout.activity_image_blur

    override fun init() {
        originBitmap = BitmapFactory.decodeResource(resources, R.drawable.lyf)
        imagePickBtn.setOnClickListener(this)
        bilateralBtn.setOnClickListener(this)
        pyrMeanShiftBtn.setOnClickListener(this)

        averageSeek.setOnSeekBarChangeListener(this)
        gaussianSeek.setOnSeekBarChangeListener(this)
        medianSeek.setOnSeekBarChangeListener(this)
        dilateSeek.setOnSeekBarChangeListener(this)
        erodeSeek.setOnSeekBarChangeListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            imagePickBtn -> pickImage()
            bilateralBtn -> bilateralBlur()
            pyrMeanShiftBtn -> pyrMeanShiftBlur()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar) {
            averageSeek -> averageBlur(progress * 1.0)
            gaussianSeek -> gaussianBlur(progress * 2.0 + 1)
            medianSeek -> medianBlur(progress * 2 + 1)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    /**
     * 均值迁移 用于人像美容 error
     */
    private fun pyrMeanShiftBlur() {
        // read image
        val src = Imgcodecs.imread(imageUri?.path)
        if (src.empty()) return
        val dst = Mat()
        Imgproc.pyrMeanShiftFiltering(src, dst, 10.0, 50.0)

        val newBitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888)
        val result = Mat()
        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2GRAY)
        Utils.matToBitmap(result, newBitmap)
        ivSimple.setImageBitmap(newBitmap)

        dst.release()
        src.release()
    }

    /**
     * 高斯双边 用于人像美容 error
     */
    private fun bilateralBlur() {
        // read image
        val src = Imgcodecs.imread(imageUri?.path)
        if (src.empty()) return
        val dst = Mat()
        Imgproc.bilateralFilter(src, dst, 0, 10.0, 50.0)

        val newBitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888)
        val result = Mat()
        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2GRAY)
        Utils.matToBitmap(result, newBitmap)
        ivSimple.setImageBitmap(newBitmap)

        dst.release()
        src.release()
    }

    /**
     * 腐蚀有效抑制图像中特定类型的噪声
     */
    private fun erodeBlur(level: Double) {
        val src = Mat()
        val dst = Mat()

        // 获取卷积核
        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(level, level))
        Utils.bitmapToMat(originBitmap, src)
        Imgproc.erode(src, dst, kernel)
        Utils.matToBitmap(dst, bitmap)

        ivSimple.setImageBitmap(bitmap)

        dst.release()
        src.release()
    }

    /**
     * 膨胀有效抑制图像中特定类型的噪声
     */
    private fun dilateBlur(level: Double) {
        val src = Mat()
        val dst = Mat()

        // 获取卷积核
        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(level, level))
        Utils.bitmapToMat(originBitmap, src)
        Imgproc.dilate(src, dst, kernel)
        Utils.matToBitmap(dst, bitmap)

        ivSimple.setImageBitmap(bitmap)

        dst.release()
        src.release()
    }


    /**
     * 有效抑制椒盐噪声
     */
    private fun medianBlur(level: Int) {
        val src = Mat()
        val dst = Mat()

        Utils.bitmapToMat(originBitmap, src)
        Imgproc.medianBlur(src, dst, level)
        Utils.matToBitmap(dst, bitmap)

        ivSimple.setImageBitmap(bitmap)

        dst.release()
        src.release()
    }

    private fun averageBlur(level: Double) {
        val src = Mat()
        val dst = Mat()

        Utils.bitmapToMat(originBitmap, src)
        Imgproc.blur(src, dst, Size(level, level), Point(-1.0, -1.0), Core.BORDER_DEFAULT)
        Utils.matToBitmap(dst, bitmap)
        ivSimple.setImageBitmap(bitmap)

        dst.release()
        src.release()
    }

    private fun gaussianBlur(level: Double) {
        val src = Mat()
        val dst = Mat()

        Utils.bitmapToMat(originBitmap, src)
//        Imgproc.GaussianBlur(src, dst, Size(0.0, 0.0), level, level, Core.BORDER_DEFAULT)
        // level > 0 && level % 2 == 1
        Imgproc.GaussianBlur(src, dst, Size(level, level), 0.0)
        Utils.matToBitmap(dst, bitmap)
        ivSimple.setImageBitmap(bitmap)

        dst.release()
        src.release()
    }
}
