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

    @ColumnInfo(name = "Date") var date: String,

    @ColumnInfo(name = "Player 1")  var p1name: String,

    @ColumnInfo(name = "Player 2") var p2name: String,

    @ColumnInfo(name = "Player 1-2") var p12name: String? = null,

    @ColumnInfo(name = "Player 2-2") var p22name: String? = null,

    @ColumnInfo(name = "Player 1 serves first") var firstServeP1: Boolean,

    @ColumnInfo(name = "Nr of sets to win") var setsToWin: Int,

    @ColumnInfo(name = "Match Tie Break") var matchTieBreak: Boolean,

    @ColumnInfo(name = "Sets won Player 1") var p1Sets: Int,

    @ColumnInfo(name = "Sets won Player 2") var p2Sets: Int,

    @ColumnInfo(name = "finished") var finished: Boolean
){
    constructor(type: String, date: String, p1name: String, p2name: String, firstServeP1: Boolean, setsToWin: Int, matchTieBreak: Boolean):
            this(0, type, date, p1name, p2name, null, null, firstServeP1, setsToWin, matchTieBreak, 0, 0, false)
    constructor(type: String, date: String, p1name: String, p2name: String, p12name: String, p22name: String, firstServeP1: Boolean, setsToWin: Int, matchTieBreak: Boolean):
            this(0, type, date, p1name, p2name, p12name, p22name, firstServeP1, setsToWin, matchTieBreak, 0, 0, false)
}