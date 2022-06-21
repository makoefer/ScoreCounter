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

    @Query("SELECT * FROM players WHERE id = :id")
    suspend fun getPlayerById(id: Long): Player

    @Query("SELECT * FROM players WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun getPlayerByName(firstName: String, lastName: String): Player
}