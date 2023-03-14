package codeasus.projects.encryption.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import codeasus.projects.encryption.data.entity.EllipticCurveKeyPairEntity
import codeasus.projects.encryption.data.util.DatabaseConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptographyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEllipticCurveKeyPairs(ellipticCurveKeyPairs: Set<EllipticCurveKeyPairEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEllipticCurveKeyPair(ellipticCurveKeyPair: EllipticCurveKeyPairEntity)

    @Query("SELECT * FROM ${DatabaseConstants.ENTITY_ELLIPTIC_CURVE_KEY_PAIR}")
    fun getAllEllipticCurveKeyPairs(): Flow<List<EllipticCurveKeyPairEntity>>

    @Query("DELETE FROM ${DatabaseConstants.ENTITY_ELLIPTIC_CURVE_KEY_PAIR} WHERE id=:id")
    suspend fun deleteEllipticCurveKeyPairByID(id: Long)
}