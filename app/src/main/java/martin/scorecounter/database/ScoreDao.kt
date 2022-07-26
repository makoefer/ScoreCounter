package martin.scorecounter.database

import androidx.room.*

@Dao
interface ScoreDao {

    // ----------------- Matches ----------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: Match): Long

    @Update
    suspend fun updateMatch(match: Match)

    suspend fun updateMatch(matchId: Long, finished: Boolean){
        var match = getMatchById(matchId)
        match.finished = finished
        updateMatch(match)
    }

    @Query("SELECT * FROM matches")
    suspend fun getAllMatches(): List<Match>

    @Query("SELECT * FROM matches WHERE id == :id")
    suspend fun getMatchById(id: Long): Match


    // ----------------- Sets -------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: Set): Long

    @Update
    suspend fun updateSet(set: Set)

    suspend fun updateSet(setid: Long, p1Games: Int, p2Games: Int, setWinner: Int){
        var set = getSetById(setid)
        set.p1Games = p1Games
        set.p2Games = p2Games
        set.setWinner = setWinner
        updateSet(set)
    }

    @Query("SELECT * FROM sets")
    suspend fun getAllSets(): List<Set>

    @Query("SELECT * FROM sets WHERE id == :id")
    suspend fun getSetById(id: Long): Set

    @Query("SELECT * FROM sets WHERE `match` == :matchId")
    suspend fun getSetsForMatch(matchId: Long): List<Set>


    // ----------------- Games ------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(vararg game: Game)

    @Update
    suspend fun updateGame(game: Game)

    @Query("SELECT * FROM games")
    suspend fun getAllGames(): List<Game>

    @Query("SELECT * FROM games WHERE `set` == :setId")
    suspend fun getGamesForSet(setId: Long): List<Game>
}