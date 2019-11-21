package cn.vansz.opencv.ui

import android.view.View
import cn.vansz.opencv.R
import kotlinx.android.synthetic.main.activity_cv_simple.*
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import kotlin.experimental.and

class CvSimpleActivity : BaseActivity(), View.OnClickListener {
    override fun layoutResID(): Int = R.layout.activity_cv_simple

    override fun init() {
        btnGray.setOnClickListener(this)
        btnSqrt.setOnClickListener(this)
        btnArithmetic.setOnClickListener(this)
        btnStdDev.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnGray -> imageGray()
            btnSqrt -> imageNegate()
            btnStdDev -> imageStdDev()
            btnArithmetic -> imageArithmetic()
        }
    }

    /**
     * 图像二值化
     */
    private fun imageStdDev() {
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)

        // 转为灰度图像
        val gray = Mat()
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY)

        // 计算均值和标准方差
        val means = MatOfDouble()
        val stddevs = MatOfDouble()
        Core.meanStdDev(gray, means, stddevs)
        println("stddevs[0] ====> ${stddevs.toArray()[0]}")

        // 将 mat 转化为 byte 数组
        val data = ByteArray(gray.width() * gray.height())
        gray.get(0, 0, data)

        // 图像二值化
        var pv: Byte
        val t = means.toArray()[0]
        for (i in data.indices) {
            pv = data[i] and 0xff.toByte()
            if (pv > t) {
                data[i] = 255.toByte()
            } else {
                data[i] = 0.toByte()
            }
        }

        gray.put(0, 0, data)
        Utils.matToBitmap(gray, bitmap)
        ivSimple.setImageBitmap(bitmap)
        gray.release()
        src.release()
    }

    private fun imageArithmetic() {
        val src1 = Mat()
        Utils.bitmapToMat(bitmap, src1)

        val src2 = Mat.zeros(src1.rows(), src1.cols(), src1.type())
        val cx = (src1.cols() - 160).toDouble()
        val cy = 160.0
        // Scalar 表示 B G R 顺序三通道的颜色
        Imgproc.circle(src2, Point(cx, cy), 150, Scalar(90.0, 95.0, 234.0), -1, 8, 0)

        // 算数运算 add subtract multiply divide
        val dst = Mat()
        Core.add(src1, src2, dst)

        Utils.matToBitmap(dst, bitmap)
        ivSimple.setImageBitmap(bitmap)
    }

    /**
     * 图片底片（胶片）化
     */
    private fun imageNegate() {
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)
        val mv = arrayListOf<Mat>()
        Core.split(src, mv)
        for (m in mv) {
            var pv: Int
            val width = m.cols()
            val height = m.rows()
            val data = ByteArray(width * height)
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
        ivSimple.setImageBitmap(bitmap)
        src.release()
    }

    /**
     * 图像灰度化
     */
    private fun imageGray() {
        val src = Mat()
        val dst = Mat()
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY)
        Utils.matToBitmap(dst, bitmap)
        ivSimple.setImageBitmap(bitmap)
        src.release()
        dst.release()
    }
}
