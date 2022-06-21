package martin.scorecounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "matches")
data class Match(

    @PrimaryKey(autoGenerate = true) var id: Long,

    @ColumnInfo(name = "Match Type") var type: String,

    @ColumnInfo(name = "Player 1") @TypeConverters(PlayerConverter::class) var p1: Player,

    @ColumnInfo(name = "Player 2") @TypeConverters(PlayerConverter::class) var p2: Player,

    @ColumnInfo(name = "Player 1-2") @TypeConverters(PlayerConverter::class) var p12: Player? = null,

    @ColumnInfo(name = "Player 2-2") @TypeConverters(PlayerConverter::class) var p22: Player? = null,

    @ColumnInfo(name = "First serving player") var firstServe: String,

    @ColumnInfo(name = "Nr of sets to win")var setsToWin: Int,

    @ColumnInfo(name = "Match Tie Break")var matchTieBreak: Boolean,

    @ColumnInfo(name = "finished")var finished: Boolean,

    @TypeConverters(SetConverter::class)
    var sets: List<Set>
){
    constructor(type: String, p1: Player, p2: Player, firstServe: String, setsToWin: Int, matchTieBreak: Boolean):
            this(0, type, p1, p2, null, null, firstServe, setsToWin, matchTieBreak, false, Collections.emptyList())
    constructor(type: String, p1: Player, p2: Player, p12: Player, p22: Player, firstServe: String, setsToWin: Int, matchTieBreak: Boolean):
            this(0, type, p1, p2, p12, p22, firstServe, setsToWin, matchTieBreak, false, Collections.emptyList())
}