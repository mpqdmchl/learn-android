package com.example.notificationtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


// 点击 Notifictation 后通过包装过意图 PendingIntent 跳转到 NotificationActivity
class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
    }
}