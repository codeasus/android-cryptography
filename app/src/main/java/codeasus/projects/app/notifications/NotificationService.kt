package codeasus.projects.app.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import codeasus.projects.app.R

class NotificationService(private val context: Context) {

    private val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CONTACT_SYNC_CHANNEL_ID = "contact_sync_channel_id"
        const val CONTACT_SYNC_CHANNEL_NAME = "Contact Sync"
        const val CONTACT_SYNC_CHANNEL_DESC = "Enabled when contacts are synced"

        const val CONTACT_SYNC_ID = 1
        const val CONTACT_SYNC_CONTENT_TITLE = "Synchronizing Contacts"
        const val CONTACT_SYNC_CONTENT_TEXT = "Sync in progress"
    }

    fun getContactSyncNotification(): Notification {
        return NotificationCompat.Builder(context, CONTACT_SYNC_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_contact)
            .setContentTitle(CONTACT_SYNC_CONTENT_TITLE)
            .setContentText(CONTACT_SYNC_CONTENT_TEXT)
            .build()
    }

    fun showContactSyncNotification() {
        mNotificationManager.notify(CONTACT_SYNC_ID, getContactSyncNotification())
    }

    fun cancelContactSyncNotification() {
        mNotificationManager.cancel(CONTACT_SYNC_ID)
    }
}