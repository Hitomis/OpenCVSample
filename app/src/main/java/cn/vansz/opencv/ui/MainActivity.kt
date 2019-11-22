package cn.vansz.opencv.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import cn.vansz.opencv.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v) {
            outlineImage -> startActivity(Intent(this, OutlineActivity::class.java))
            idDetector -> startActivity(Intent(this, DetectorActivity::class.java))
            gifImage -> startActivity(Intent(this, GifActivity::class.java))
            opencvSimple -> startActivity(Intent(this, CvSimpleActivity::class.java))
            imageAdjustment -> startActivity(Intent(this, ImageAdjustmentActivity::class.java))
            imageBlur -> startActivity(Intent(this, ImageBlurActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        outlineImage.setOnClickListener(this)
        idDetector.setOnClickListener(this)
        gifImage.setOnClickListener(this)
        opencvSimple.setOnClickListener(this)
        imageAdjustment.setOnClickListener(this)
        imageBlur.setOnClickListener(this)
    }

}
