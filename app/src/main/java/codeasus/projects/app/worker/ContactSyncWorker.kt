package codeasus.projects.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import codeasus.projects.app.notifications.NotificationService
import codeasus.projects.data.features.contact.model.Contact
import codeasus.projects.data.features.contact.repository.ContactRepository
import codeasus.projects.data.features.contact.util.ContactUtility
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class ContactSyncWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    @Inject
    lateinit var contactRepository: ContactRepository

    private val mNotificationService = NotificationService(ctx)

    companion object {
        private const val TAG = "DBG@ContactSyncWorker"
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NotificationService.CONTACT_SYNC_ID,
            mNotificationService.getContactSyncNotification()
        )
    }

    override suspend fun doWork(): Result {
        val deviceContactsMap = ContactUtility.getDeviceContactsMappedByPhoneNumber(ctx)
        updateContacts(deviceContactsMap)
        return Result.success()
    }

    private suspend fun updateContacts(deviceContactsMap: HashMap<String, Contact>) {
        contactRepository.completeUpdate(deviceContactsMap.values.toList())
    }
}