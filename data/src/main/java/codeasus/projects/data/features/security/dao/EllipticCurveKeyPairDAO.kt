package codeasus.projects.data.features.security.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import codeasus.projects.data.features.app.util.DatabaseConstants
import codeasus.projects.data.features.security.entity.EllipticCurveKeyPairEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EllipticCurveKeyPairDAO {
    @Query("SELECT * FROM ${DatabaseConstants.ENTITY_ELLIPTIC_CURVE_KEY_PAIR} WHERE :id=id")
    fun getEllipticCurveKeyPairByID(id: Long): EllipticCurveKeyPairEntity

    @Query("SELECT * FROM ${DatabaseConstants.ENTITY_ELLIPTIC_CURVE_KEY_PAIR}")
    fun getEllipticCurveKeyPairs(): List<EllipticCurveKeyPairEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEllipticCurveKeyPair(ellipticCurveKeyPairEntity: EllipticCurveKeyPairEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEllipticCurveKeyPairs(ellipticCurveKeyPairEntitySet: Set<EllipticCurveKeyPairEntity>): List<Long>

    @Query("DELETE FROM ${DatabaseConstants.ENTITY_ELLIPTIC_CURVE_KEY_PAIR} WHERE id=:id")
    suspend fun deleteEllipticCurveKeyPairByID(id: Long)

    @Query("DELETE FROM ${DatabaseConstants.ENTITY_ELLIPTIC_CURVE_KEY_PAIR}")
    suspend fun deleteEllipticCurveKeyPairs()
}