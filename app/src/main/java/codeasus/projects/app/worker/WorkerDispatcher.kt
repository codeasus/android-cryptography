package codeasus.projects.app.worker

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import codeasus.projects.app.util.Constants


fun enqueueContactSyncWork(ctx: Context) {
    val contactSyncWorker = OneTimeWorkRequestBuilder<ContactSyncWorker>()
        .addTag(Constants.WorkManager.CONTACT_SYNC)
        .build()

    WorkManager
        .getInstance(ctx)
        .beginUniqueWork(
            Constants.WorkManager.CONTACT_UNIQUE_WORK,
            ExistingWorkPolicy.KEEP,
            contactSyncWorker
        )
        .enqueue()
}