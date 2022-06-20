package martin.scorecounter.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = Set::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("set"),
    onDelete = ForeignKey.CASCADE)],
    tableName = "games")
data class Game(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    var gameNubmer: Int,

    var p1Points: String,

    var p2Points: String,

    var servingPlayer: String,

    var gameWinner: String,

    var pointsHistory: String,

    var finished: Boolean,

    var set: Long
)