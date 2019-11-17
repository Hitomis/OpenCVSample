package cn.vansz.opencv.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import cn.vansz.opencv.R
import cn.vansz.opencv.ext.shortShow
import kotlinx.android.synthetic.main.activity_cv_simple.*
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import kotlin.experimental.and

class CvSimpleActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var bitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv_simple)
        initLoadOpenCV()
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.lyf)
        btnGray.setOnClickListener(this)
        btnSqrt.setOnClickListener(this)
        btnAveraging.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnGray -> imageGray()
            btnSqrt -> imageNegate()
            btnAveraging -> imageAveraging()
        }
    }

    private fun imageAveraging() {

    }

    private fun imageNegate() {
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)
        val mv = arrayListOf<Mat>()
        Core.split(src, mv)
        for (m in mv) {
            var pv: Int
            val channels = m.channels()
            val width = m.cols()
            val height = m.rows()
            val data = ByteArray(channels * width * height)
            m.get(0, 0, data)
            for (i in data.indices) {
                pv = (data[i] and 0xff.toByte()).toInt()
                pv = 255 - pv
                data[i] = pv.toByte()
            }
            m.put(0, 0, data)
        }
        Core.merge(mv, src)
        Utils.matToBitmap(src, bitmap)
        ivGray.setImageBitmap(bitmap)
        src.release()
    }

    private fun imageGray() {
        val src = Mat()
        val dst = Mat()
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY)
        Utils.matToBitmap(dst, bitmap)
        ivGray.setImageBitmap(bitmap)
        src.release()
        dst.release()
    }


    private fun initLoadOpenCV() {
        val loaded = OpenCVLoader.initDebug()
        shortShow(this, if (loaded) "OpenCV Libraries loaded..."
        else "Could not load OpenCV Libraries")
    }
}
