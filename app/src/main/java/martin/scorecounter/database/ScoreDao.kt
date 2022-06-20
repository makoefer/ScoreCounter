package martin.scorecounter.database

import androidx.room.*

@Dao
interface ScoreDao {

    // ----------------- Matches ----------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: Match)

    @Update
    suspend fun updateMatch(match: Match)

    @Query("SELECT * FROM matches")
    suspend fun getAllMatches(): List<Match>


    // ----------------- Sets -------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: Set)

    @Update
    suspend fun updateSet(set: Set)

    @Query("SELECT * FROM sets")
    suspend fun getAllSets(): List<Set>


    // ----------------- Games ------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(vararg game: Game)

    @Update
    suspend fun updateGame(game: Game)

    @Query("SELECT * FROM games")
    suspend fun getAllGames(): List<Game>

}