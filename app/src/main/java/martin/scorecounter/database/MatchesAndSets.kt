package martin.scorecounter.database

import androidx.room.Embedded
import androidx.room.Relation

data class MatchesAndSets(
    @Embedded
    val match: Match,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val sets: List<Set>
)