package martin.scorecounter.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.reflect.typeOf

class SetConverter {

    companion object{
        val gson = Gson()

        @TypeConverter
        @JvmStatic
        fun stringToSetList(data: String?): List<Set> {
            if (data == null) {
                return Collections.emptyList()
            }
            val listType = object : TypeToken<List<Set?>?>() {}.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun setListToString(sets: List<Set?>?): String {
            return gson.toJson(sets)
        }
    }
}