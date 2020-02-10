package com.bradpark.pjhextension.Utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import android.graphics.BitmapFactory
import java.net.HttpURLConnection
import java.net.URL


object NotificationFactory {
    private fun createForDownload(context: Context, channelId: String, channelName: String,
                       notificationManager: NotificationManager): NotificationCompat.Builder {
        val builder: NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)

            with(channel) {
                setShowBadge(false)
                enableLights(false)
                enableVibration(true)
                vibrationPattern = longArrayOf(0)
                setSound(null, null)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                notificationManager.createNotificationChannel(this)
            }

            builder = NotificationCompat.Builder(context, channel.id)
            with(builder) {
                setSound(null)
                setVibrate(longArrayOf(0))
                setOnlyAlertOnce(true)
            }
        } else {
            builder = NotificationCompat.Builder(context)
        }

        return builder
    }

    private fun create(context: Context, channelId: String, channelName: String,
                       notificationManager: NotificationManager): NotificationCompat.Builder {
        val builder: NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

            with(channel) {
                description = "siwonlectureroom_study_helper_noti"
                setShowBadge(false)
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC

                notificationManager.createNotificationChannel(this)
            }

            builder = NotificationCompat.Builder(context, channel.id)
        } else {
            builder = NotificationCompat.Builder(context)
        }

        with(builder) {
            setAutoCancel(true)
//            setSmallIcon(R.mipmap.ic_launcher)
            setDefaults(Notification.DEFAULT_SOUND)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }

        return builder
    }

    fun createProgressNotification(context: Context, title: String, channelId: String,
                           channelName: String, notificationId: Int, actionTitle: String,
                                   pendingIntent: PendingIntent): NotificationCompat.Builder {
        val notificationManager: NotificationManager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = createForDownload(context, channelId, channelName, notificationManager).apply {
            setAutoCancel(true)
            setSmallIcon(android.R.drawable.stat_sys_download)
            setContentTitle(title)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setProgress(100, 0, false)
            setStyle(NotificationCompat.BigTextStyle())

            addAction(0, actionTitle, pendingIntent)
        }

        notificationManager.notify(notificationId, builder.build())

        return builder
    }

    fun updateProgressNotification(context: Context, builder: NotificationCompat.Builder,
                                   notificationId: Int, progressRate: Int) {
        (context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
            builder.let {
                it.setProgress(100, progressRate, false)
                notify(notificationId, it.build())
            }
        }
    }

    fun updateNotification(context: Context, builder: NotificationCompat.Builder, notificationId: Int) {
        (context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
            notify(notificationId, builder.build())
        }
    }

    fun createNotification(context: Context, title: String, msg: String, channelId: String,
                           channelName: String, notificationId: Int, pendingIntent: PendingIntent?) {
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        create(context, channelId, channelName, notificationManager).apply {
            setContentTitle(title)
            setContentText(msg)

            pendingIntent?.let {
                setContentIntent(it)
            }

            notificationManager.notify(notificationId, this.build())
        }
    }

    fun createTextNotification(context: Context, title: String, msg: String, channelId: String,
                           channelName: String, notificationId: Int, pendingIntent: PendingIntent?) {
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        create(context, channelId, channelName, notificationManager).apply {
            setContentTitle(title)
            setContentText(msg)
            setStyle(NotificationCompat.BigTextStyle())
            pendingIntent?.let {
                setContentIntent(it)
            }

            notificationManager.notify(notificationId, this.build())
        }
    }

    fun createBigPictureNotification(context: Context, title: String, msg: String, channelId: String,
                                     channelName: String, notificationId: Int, pendingIntent: PendingIntent?, imgUrl: String?, launcher_Id: Int) {
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            create(context, channelId, channelName, notificationManager).apply {
                setContentTitle(title)
                setContentText(msg)
                var iconBitmap = BitmapFactory.decodeResource(context.resources, launcher_Id)
                if (imgUrl != null) {
                    setLargeIcon(getBitmap(imgUrl)?:iconBitmap)
                    setStyle(NotificationCompat.BigPictureStyle().bigLargeIcon(null).bigPicture(getBitmap(imgUrl)?:iconBitmap))
                } else {
                    setLargeIcon(iconBitmap)
                    setStyle(NotificationCompat.BigPictureStyle().bigLargeIcon(null).bigPicture(iconBitmap))
                }
                pendingIntent?.let {
                    setContentIntent(it)
                }

                notificationManager.notify(notificationId, this.build())
            }
    }

    fun cancelNotification(context: Context, notificationId: Int, channelId: String) {
        (context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
            cancel(notificationId)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                deleteNotificationChannel(channelId)
            }
        }
    }

    private fun getBitmap(imgurl: String): Bitmap? {
        return try {
            val url = URL(imgurl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}