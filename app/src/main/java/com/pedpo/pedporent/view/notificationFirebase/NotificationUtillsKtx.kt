package com.pedpo.pedporent.view.notificationFirebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.pedpo.pedporent.R
import com.pedpo.pedporent.model.notification.NotificationTransfer
import com.pedpo.pedporent.view.nav.NavActivity
import java.io.IOException
import java.io.InputStream
import java.net.URL


class NotificationUtillsKtx {

    private var context: Context? = null;

    constructor(context: Context) {
        this.context = context;
    }

    val NOTIFICATION_CHANNEL_ID = "10001"
    private val default_notification_channel_id = "default"

    fun showNotification(transfer:NotificationTransfer){
        detailsNotification(transfer)
    }

    private fun detailsNotification(transfer:NotificationTransfer) {
        val sound: Uri =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context?.packageName + "/raw/quite_impressed.mp3")
            showNotificationOld(sound = sound ,transfer)
    }

    private fun showNotificationOld(sound: Uri,transfer: NotificationTransfer) {
//        intent?.apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            putExtra("message", title)
//        }
//        var remote_picture: Bitmap? = null
        var iconLarge: Bitmap? = null
        try {
            iconLarge = BitmapFactory.decodeStream(
                URL(
                    transfer.imageUri.toString()
                ).content as InputStream
            )

//                remote_picture = BitmapFactory.decodeStream(
//                    URL(
//                        transfer.image.toString()
//                    ).content as InputStream
//                )


        } catch (e: IOException) {
            Log.i("FireBaseToken", "IOException: ${e.message}")
            e.printStackTrace()
        }
        Log.i("FireBaseToken", "showNotificationOld: ${transfer.imageUri}")

        var intent = Intent(context, NavActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("title", transfer.title.toString())
            putExtra("message", transfer.message.toString())
        }
        Log.i("FireBaseToken", "intent result : ${intent.getStringExtra("title")}")

        var pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)


        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            context!!,
            default_notification_channel_id
        )
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_pedpo_graphic_primary)
            .setTicker(transfer.title)
            .setContentTitle(transfer.title)
//            .setStyle(NotificationCompat.MessagingStyle("Me")
//                .setConversationTitle("Team lunch")
//                .addMessage(transfer.message, 10L, "fea") // Pass in null for user.
//                .addMessage("What's up?", 10L, "Coworker")
//                .addMessage("Not much", 10L, "fa")
//                .addMessage("How about lunch?", 10L, "Coworker"))
            .setSound(sound)
            .setContentText(transfer.message)
            .setColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setWhen(3L)
//            .setSmallIcon(R.drawable.ic_pedpo_graphic)
            .setSmallIcon(R.drawable.ic_pedpo_graphic_primary)
//            .setLargeIcon(BitmapFactory.decodeResource(context?.resources, R.drawable.ic_pedpo_graphic_primary))
//            .setLargeIcon(remote_picture)

        if (iconLarge!=null)
        builder.setLargeIcon(iconLarge)


        if (iconLarge!=null)
        builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(iconLarge).bigLargeIcon(null))

        var notificationManager =
            context?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

        createChannel(builder, notificationManager,sound = sound);

    }

    private fun createChannel(
        builder: NotificationCompat.Builder,
        notificationManager: NotificationManager,
        sound: Uri
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
            var audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            var importance = NotificationManager.IMPORTANCE_HIGH
            var notificationChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, "name", importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationChannel.setSound(sound, audioAttributes)
            builder.setChannelId(NOTIFICATION_CHANNEL_ID)

            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        assert(notificationManager != null)
        notificationManager!!.notify(System.currentTimeMillis().toInt(), builder.build())

    }

    fun show(context: Context) {
        val sound: Uri =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/raw/quite_impressed.mp3")
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            context,
            default_notification_channel_id
        )
            .setSmallIcon(R.drawable.ic_pedpo_graphic_primary)
            .setContentTitle("Test")
            .setSound(sound)
            .setContentText("Hello! This is my first push notification")
        val mNotificationManager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            var importance = NotificationManager.IMPORTANCE_HIGH
            var notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationChannel.setSound(sound, audioAttributes)
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            assert(mNotificationManager != null)
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
        assert(mNotificationManager != null)
        mNotificationManager!!.notify(System.currentTimeMillis().toInt(), mBuilder.build())
    }

}