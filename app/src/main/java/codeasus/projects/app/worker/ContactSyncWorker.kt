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
import codeasus.projects.data.features.security.repository.HybridCryptoParameterRepository
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

    @Inject
    lateinit var hybridCryptoParameterRepository: HybridCryptoParameterRepository

    private var mNotificationService = NotificationService(ctx)

    companion object {
        private val TAG = "DBG@${ContactSyncWorker::class.java.name}"
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NotificationService.CONTACT_SYNC_ID,
            mNotificationService.getContactSyncNotification()
        )
    }

    override suspend fun doWork(): Result {
        val deviceContactsMap = ContactUtility.getDeviceContactsMappedByPhoneNumber(ctx)
        val appContactsMap = contactRepository.getContactsMappedByPhoneNumber()

        updateContacts(deviceContactsMap)
        cleanUpHybridCryptoParameters(deviceContactsMap, appContactsMap)

        return Result.success()
    }

    private suspend fun updateContacts(deviceContactsMap: HashMap<String, Contact>) {
        contactRepository.completeUpdate(deviceContactsMap.values.toList())
    }

    private suspend fun cleanUpHybridCryptoParameters(
        deviceContactsMap: HashMap<String, Contact>,
        appContactsMap: Map<String, Contact>
    ) {
        val removedContacts = appContactsMap.keys.filter { !deviceContactsMap.containsKey(it) }
        hybridCryptoParameterRepository.deleteHybridCryptoParametersByPhoneNumbers(removedContacts)
    }
}