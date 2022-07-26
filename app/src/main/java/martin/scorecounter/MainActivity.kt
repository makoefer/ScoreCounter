package martin.scorecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import martin.scorecounter.database.AppDatabase
import martin.scorecounter.database.ScoreDao

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getInstance(this)
        scoreDao = db.scoreDao()
    }
    companion object {
        lateinit var db: AppDatabase
        lateinit var scoreDao: ScoreDao
    }
}