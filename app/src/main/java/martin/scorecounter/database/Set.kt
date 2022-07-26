package martin.scorecounter.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = Match::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("match"),
    onDelete = ForeignKey.CASCADE)],
    tableName = "sets")
data class Set(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    var setNumber: Int,

    var p1Games: Int,

    var p2Games: Int,

    var setWinner: Int,

    var match: Long

){
    constructor(setNumber: Int, match:Long):
            this(0, setNumber, 0,0,0, match)
}