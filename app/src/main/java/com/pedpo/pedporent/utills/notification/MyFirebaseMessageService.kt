package com.pedpo.pedporent.utills.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pedpo.pedporent.model.notification.NotificationTransfer
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.notificationFirebase.NotificationUtillsKtx
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessageService :FirebaseMessagingService() {

    @Inject
    lateinit var prefApp: PrefApp;

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FireBaseToken", "Notification Click Action:  ${remoteMessage.notification?.clickAction}")
        Log.d("FireBaseToken", "Notification Click Action:  ${remoteMessage.notification?.imageUrl}")
        Log.d("FireBaseToken", "onMessageReceived ${remoteMessage.data["title"]}")
        Log.d("FireBaseToken", "to ${remoteMessage.to}")
        Log.d("FireBaseToken", "messageId ${remoteMessage.messageId}")
        Log.d("FireBaseToken", "messageType ${remoteMessage.messageType}")



        var transfer = NotificationTransfer();
        transfer.title = remoteMessage.notification?.title;
        transfer.message = remoteMessage.notification?.body;
        transfer.imageUri = remoteMessage.notification?.imageUrl;

        when {
            remoteMessage.notification?.clickAction.equals("add") -> {


            }



            remoteMessage.notification?.clickAction.equals("nav") -> {

            }
            else -> {

            }
        }

//        intent?.apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            putExtra("title",remoteMessage.notification?.title )
//            putExtra("message",remoteMessage.notification?.body )
//        }

        NotificationUtillsKtx(applicationContext).showNotification(
            transfer )

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val msg = "onNewToken : "+token
        Log.d("FireBaseToken", msg)
        Log.i("checkLogin", "onNewToken: $token")

        prefApp.setTokenFirebase(token = token)
    }

}