package codeasus.projects.data.features.contact.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import codeasus.projects.data.features.contact.model.Contact
import codeasus.projects.data.features.contact.repository.ContactRepository
import codeasus.projects.data.features.contact.util.ContactUtility
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class ContactSyncWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted private val params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    @Inject
    lateinit var contactRepository: ContactRepository

    companion object {
        private val TAG = "DBG@${ContactSyncWorker::class.java.name}"
    }

    override suspend fun doWork(): Result {
        val deviceContactsMap = ContactUtility.getDeviceContactsMappedByPhoneNumber(ctx)
        val appContacts = contactRepository.getContacts()
        val nonExistingAppContacts = mutableSetOf<Contact>()
        return Result.failure()
    }
}