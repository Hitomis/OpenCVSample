package cn.vansz.opencv.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.vansz.opencv.R
import cn.vansz.opencv.ext.shortShow
import org.opencv.android.OpenCVLoader

/**
 * Created by Vans Z on 2019-11-21.
 */
abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResID())
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.lyf)
        loadOpenCVLibraries()
        init()
    }

    abstract fun layoutResID(): Int

    abstract fun init()

    private fun loadOpenCVLibraries() {
        val loaded = OpenCVLoader.initDebug()
        shortShow(this, if (loaded) "OpenCV Libraries loaded..."
        else "Could not load OpenCV Libraries")
    }

}