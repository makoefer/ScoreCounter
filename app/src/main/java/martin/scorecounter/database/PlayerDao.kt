package martin.scorecounter.database

import androidx.room.*

@Dao
interface PlayerDao {
    // ----------------- Players ----------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player)

    @Update
    suspend fun updatePlayer(player: Player)

    @Query("SELECT * FROM players")
    suspend fun getAllPlayers(): List<Player>
}