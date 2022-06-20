package martin.scorecounter.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class GameConverter {

    companion object{
        val gson = Gson()

        @TypeConverter
        @JvmStatic
        fun stringToGameList(data: String?): List<Game> {
            if (data == null) {
                return Collections.emptyList()
            }
            val listType = object : TypeToken<List<Game?>?>() {}.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun gameListToString(games: List<Game?>?): String {
            return gson.toJson(games)
        }
    }

}