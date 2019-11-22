package cn.vansz.opencv.ui

import android.view.View
import android.widget.SeekBar
import cn.vansz.opencv.R
import kotlinx.android.synthetic.main.activity_image_blur.*
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class ImageBlurActivity : BaseActivity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    override fun layoutResID(): Int = R.layout.activity_image_blur

    override fun init() {
        averageBtn.setOnClickListener(this)
        gaussianXSeek.setOnClickListener(this)
        gaussianYSeek.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        averageBlur()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }


    private fun averageBlur() {

        val src = Mat()
        val dst = Mat()

        Utils.bitmapToMat(bitmap, src)
        Imgproc.blur(src, dst, Size(50.0, 50.0), Point(-1.0, -1.0), Core.BORDER_DEFAULT)
        Utils.matToBitmap(dst, bitmap)
        ivSimple.setImageBitmap(bitmap)

        dst.release()
        src.release()
    }
}
