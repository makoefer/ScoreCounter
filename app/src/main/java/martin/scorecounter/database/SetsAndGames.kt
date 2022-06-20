package martin.scorecounter.database

import androidx.room.Embedded
import androidx.room.Relation

data class SetsAndGames (
    @Embedded
    val set: Set,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val games: List<Game>
)