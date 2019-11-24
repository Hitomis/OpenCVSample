package cn.vansz.opencv.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.SeekBar
import cn.vansz.opencv.R
import kotlinx.android.synthetic.main.activity_image_adjustment.*
import kotlinx.android.synthetic.main.activity_image_blur.*
import kotlinx.android.synthetic.main.activity_image_blur.ivSimple
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class ImageBlurActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {
    private lateinit var originBitmap: Bitmap
    override fun layoutResID(): Int = R.layout.activity_image_blur

    override fun init() {
        originBitmap = BitmapFactory.decodeResource(resources, R.drawable.lyf)

        averageSeek.setOnSeekBarChangeListener(this)
        gaussianSeek.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar) {
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
