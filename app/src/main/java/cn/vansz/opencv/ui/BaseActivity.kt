package cn.vansz.opencv.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import cn.vansz.opencv.R
import cn.vansz.opencv.ext.shortShow
import org.opencv.android.OpenCVLoader
import java.io.File

/**
 * Created by Vans Z on 2019-11-21.
 */
abstract class BaseActivity : AppCompatActivity() {
    private val requestPickImage = 1

    protected lateinit var bitmap: Bitmap
    protected var imageUri: Uri? = null

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

    fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, requestPickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestPickImage && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val uri = data.data
                val f = File(getRealPath(uri, applicationContext))
                imageUri = Uri.fromFile(f)
            }
            imageUri?.let {
                val imageView = findViewById<View>(R.id.ivSimple) as ImageView
                imageView.setImageBitmap(BitmapFactory.decodeFile(imageUri?.path))
            }
        }
    }

    open fun getRealPath(uri: Uri?, appContext: Context): String? {
        var filePath: String? = null
        val wholeID = DocumentsContract.getDocumentId(uri)
        val id = wholeID.split(":").toTypedArray()[1]
        val column = arrayOf(MediaStore.Images.Media.DATA)
        val sel = MediaStore.Images.Media._ID + "=?"
        val cursor = appContext.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
                sel, arrayOf(id), null)
        cursor?.let {
            val columnIndex = cursor.getColumnIndex(column[0])
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        return filePath
    }
}