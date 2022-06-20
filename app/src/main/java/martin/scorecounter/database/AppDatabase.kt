package martin.scorecounter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [Match::class, Set::class, Game::class, Player::class],
    version = AppDatabase.DB_VERSION
)
@TypeConverters(PlayerConverter::class, SetConverter::class, GameConverter::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun scoreDao(): ScoreDao
    abstract fun playerDao(): PlayerDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "application.db"


        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context)
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                .build()
    }
}