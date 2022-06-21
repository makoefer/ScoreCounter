package martin.scorecounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(

    @PrimaryKey(autoGenerate = true) var id: Long,

    @ColumnInfo(name = "firstName") var firstName: String,
    @ColumnInfo(name = "lastName") var lastName: String
) {
    constructor(name: String):this(0, name, "")
    constructor(fname: String, lname:String):this(0, fname, lname)
}
