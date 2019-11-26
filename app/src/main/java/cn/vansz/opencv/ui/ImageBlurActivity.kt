package cn.vansz.opencv.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.SeekBar
import cn.vansz.opencv.R
import kotlinx.android.synthetic.main.activity_image_blur.*
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class ImageBlurActivity : BaseActivity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private lateinit var originBitmap: Bitmap
    override fun layoutResID(): Int = R.layout.activity_image_blur

    override fun init() {
        originBitmap = BitmapFactory.decodeResource(resources, R.drawable.lyf)

        averageSeek.setOnSeekBarChangeListener(this)
        gaussianSeek.setOnSeekBarChangeListener(this)
        medianSeek.setOnSeekBarChangeListener(this)
        dilateSeek.setOnSeekBarChangeListener(this)
        erodeSeek.setOnSeekBarChangeListener(this)
        bilateralSeek.setOnSeekBarChangeListener(this)
        pyrMeanShiftSeek.setOnSeekBarChangeListener(this)
    }

    override fun onClick(v: View?) {
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar) {
            averageSeek -> averageBlur(progress * 1.0)
            gaussianSeek -> gaussianBlur(progress * 2.0 + 1)
            medianSeek -> medianBlur(progress * 2 + 1)
            dilateSeek -> dilateBlur(progress * 1.0)
            erodeSeek -> erodeBlur(progress * 1.0)
            bilateralSeek -> bilateralBlur(progress * 1.0)
            pyrMeanShiftSeek -> pyrMeanShiftBlur(progress * 1.0)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    /**
     * 用于人像美容 error
     */
    private fun pyrMeanShiftBlur(level: Double) {
        val src = Mat()
        val dst = Mat()

        Utils.bitmapToMat(originBitmap, src)
        originBitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.RGB_565)
        val src2 = Mat()
        Imgproc.cvtColor(src, src2, Imgproc.COLOR_BGR2RGBA)
        // Only 8-bit, 3-channel images are supported in function 'pyrMeanShiftFiltering'
        Imgproc.pyrMeanShiftFiltering(src2, dst, 10.0, 50.0)
        Utils.matToBitmap(dst, bitmap)
        ivSimple.setImageBitmap(bitmap)

        dst.release()
        src.release()
    }

    /**
     * 用于人像美容 error
     */
    private fun bilateralBlur(level: Double) {
        val src = Mat()
        val src2 = Mat()
        val dst = Mat()

        Utils.bitmapToMat(originBitmap, src)

        // (src.type() == CV_8UC1 || src.type() == CV_8UC3)
        src.convertTo(src2, CvType.CV_8UC1)
        Imgproc.bilateralFilter(src2, dst, 0, 100.0, 15.0)
        Utils.matToBitmap(dst, bitmap)
        ivSimple.setImageBitmap(bitmap)

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
