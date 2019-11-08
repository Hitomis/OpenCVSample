package cn.vansz.opencv.ui

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.vansz.opencv.JniHelper
import cn.vansz.opencv.R
import kotlinx.android.synthetic.main.activity_outline.*

class OutlineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outline)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.lyf)
        load.setOnClickListener{
            style_image.setImageBitmap(bitmap)
        }
        process.setOnClickListener{
            JniHelper.getInstance().getEdge(bitmap)
            style_image.setImageBitmap(bitmap)
        }
    }
}
