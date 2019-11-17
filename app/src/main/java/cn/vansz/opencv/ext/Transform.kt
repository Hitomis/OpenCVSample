package cn.vansz.opencv.ext

import android.content.Context
import android.widget.Toast

/**
 * Created by Vans Z on 2019-11-17.
 */
fun Context.shortShow(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Context.longShow(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}