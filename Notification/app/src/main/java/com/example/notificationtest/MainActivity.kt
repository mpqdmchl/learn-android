package com.example.notificationtest

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.notificationtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var mBinding: ActivityMainBinding
    private lateinit var manager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 判断android系统的SDK版本号（此处SDK_INT的API level=34）与API等级（VERSION_CODES.O代表的API level = 26）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("normal", "Normal", NotificationManager.IMPORTANCE_HIGH)
            channel.setBypassDnd(true)  //是否绕过免打扰
            channel.enableLights(true)
            manager.createNotificationChannel(channel)
        }

        // ContextCompat.checkSelfPermission 检查权限 返回值为常量 有权限: PackageManager.PERMISSION_GRANTED，无权限: PackageManager.PERMISSION_DENIED
        mBinding.sendNotice.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
                    )
                } else {
                    notice(manager)
                }
            } else {
                notice(manager)
            }
        }

    }

    //需要重写onRequestPermissionsResult函数以处理权限请求的返回结果
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You can send notice", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun notice(manager: NotificationManager) {
        val intent = Intent(this, NotificationActivity::class.java)
        val pi = PendingIntent.getActivity(
            this, 0, intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                0
            }
        )
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.test, options)
        options.inSampleSize = calculateInSampleSize(options, 200, 200)
        options.inJustDecodeBounds = false

        val notification =
            NotificationCompat.Builder(this, "normal").setContentTitle("This is content title")
//            .setContentText("This is content text")
                .setSmallIcon(androidx.core.R.drawable.ic_call_answer)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources, R.drawable.ic_launcher_foreground
                    )
                )
                .setFullScreenIntent(pi, true)
//            .setStyle(
//                NotificationCompat.BigTextStyle().bigText(
//                    "Learn how to build " +
//                            "notifications , send and sync data , and use voice actions . Get the official" +
//                            " Android IDE and developer tools to build apps for Android."
//                )
//            )
                .setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(
                        BitmapFactory.decodeResource(resources, R.drawable.test, options)
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .build()

        manager.notify(1, notification)
    }

    // 自定义方法，用于计算缩放比例
    private fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
    ): Int {
        // 获取原始图片的宽高
        val width = options.outWidth
        val height = options.outHeight
        var inSampleSize = 1

        // 如果原始图片的宽或高大于所需的宽或高，则计算缩放比例
        if (width > reqWidth || height > reqHeight) {
            val halfWidth = width / 2
            val halfHeight = height / 2

            // 计算最大的 inSampleSize，保证缩放后的图片宽高都不小于所需的宽高
            while ((halfWidth / inSampleSize) >= reqWidth && (halfHeight / inSampleSize) >= reqHeight) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

}