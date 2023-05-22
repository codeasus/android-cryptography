package codeasus.projects.data.features.app.util;

import androidx.room.withTransaction
import codeasus.projects.data.features.app.db.AndroidCryptographyDatabase
import javax.inject.Inject

class TransactionalInterface @Inject constructor(private val db: AndroidCryptographyDatabase) {
    suspend fun <R> runAsTransaction(block: suspend () -> R): R {
        return db.withTransaction(block)
    }
}