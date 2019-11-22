package cn.vansz.opencv.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.SeekBar
import cn.vansz.opencv.R
import kotlinx.android.synthetic.main.activity_image_adjustment.*
import kotlinx.android.synthetic.main.activity_image_adjustment.ivSimple
import kotlinx.android.synthetic.main.activity_image_blur.*
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class ImageAdjustmentActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {
    private lateinit var originBitmap: Bitmap

    override fun layoutResID(): Int = R.layout.activity_image_adjustment

    override fun init() {
        originBitmap = BitmapFactory.decodeResource(resources, R.drawable.lyf)
        brightnessSeek.setOnSeekBarChangeListener(this)
        contrastSeek.setOnSeekBarChangeListener(this)
        contrastSeek.progress = 100 // 1 以下，降低对比度，反之提升

        combineBrightnessSeek.setOnSeekBarChangeListener(this)
        combineContrastSeek.setOnSeekBarChangeListener(this)
        combineContrastSeek.progress = 100 // 1 以下，降低对比度，反之提升

        averageSeek.setOnSeekBarChangeListener(this)
        gaussianSeek.setOnSeekBarChangeListener(this)

    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar) {
            brightnessSeek -> {
                brightness(progress * 1.0)
            }
            contrastSeek -> {
                contrast(progress / 100.0)
            }
            combineBrightnessSeek -> {
                combineSeek(progress * 1.0, 1.0)
            }
            combineContrastSeek -> {
                combineSeek(1.5, progress / 100.0)
            }
            averageSeek -> {
                averageBlur(progress * 1.0)
            }
            gaussianSeek -> {
                gaussian(progress * 2.0 + 1)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    private fun brightness(level: Double) {
        val src = Mat()
        val dst = Mat()

        Utils.bitmapToMat(originBitmap, src)
        Core.add(src, Scalar(level, level, level), dst)
        Utils.matToBitmap(dst, bitmap)
        ivSimple.setImageBitmap(bitmap)

        dst.release()
        src.release()
    }

    private fun contrast(level: Double) {
        val src = Mat()
        val dst = Mat()

        Utils.bitmapToMat(originBitmap, src)
        Core.multiply(src, Scalar(level, level, level), dst)
        Utils.matToBitmap(dst, bitmap)
        ivSimple.setImageBitmap(bitmap)

        dst.release()
        src.release()
    }

    private fun combineSeek(brightness: Double, contrast: Double) {
        val src = Mat()
        val dst = Mat()

        Utils.bitmapToMat(originBitmap, src)
        val black = Mat.zeros(src.size(), src.type())

        Core.addWeighted(src, contrast, black, 1.0 - contrast, brightness, dst)

        val bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        val result = Mat()

        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm)
        ivSimple.setImageBitmap(bm)

        result.release()
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

    private fun gaussian(level: Double) {
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
