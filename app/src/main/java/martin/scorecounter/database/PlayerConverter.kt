package martin.scorecounter.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class PlayerConverter {

    companion object {
        val gson = Gson()

        @TypeConverter
        @JvmStatic
        fun stringToPlayer(data: String?): Player? {
            if (data == null) {
                return null
            }
            val listType: Type = object : TypeToken<Player?>() {}.getType()
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun playerToString(someObjects: Player?): String {
            return gson.toJson(someObjects)
        }
    }
}