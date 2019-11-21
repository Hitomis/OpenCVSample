package cn.vansz.opencv.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.SeekBar
import cn.vansz.opencv.R
import kotlinx.android.synthetic.main.activity_image_adjustment.*
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Scalar

class ImageAdjustmentActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {
    private lateinit var originBitmap: Bitmap

    override fun layoutResID(): Int = R.layout.activity_image_adjustment

    override fun init() {
        originBitmap = BitmapFactory.decodeResource(resources, R.drawable.lyf)
        brightnessSeek.setOnSeekBarChangeListener(this)
        contrastSeek.setOnSeekBarChangeListener(this)
        contrastSeek.progress = 100 // 1 以下，降低对比度，反之提升
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar) {
            brightnessSeek -> {
                println("brightnessSeek => $progress")
                brightness(progress * 1.0)
            }
            contrastSeek -> {
                println("contrastSeek => $progress")
                contrast(progress / 100.0)
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

    private fun contrast(level:  Double) {
        val src = Mat()
        val dst = Mat()

        Utils.bitmapToMat(originBitmap, src)
        Core.multiply(src, Scalar(level, level, level), dst)
        Utils.matToBitmap(dst, bitmap)
        ivSimple.setImageBitmap(bitmap)

        dst.release()
        src.release()
    }

}
