package martin.scorecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import martin.scorecounter.database.AppDatabase
import martin.scorecounter.database.ScoreDao

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getInstance(this)
        scoreDao = db.scoreDao()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_main_menu -> {supportFragmentManager.beginTransaction()
                .replace(R.id.linearLayout, MainMenuFragment())
                .addToBackStack(null)
                .commit() }
            R.id.action_about -> {supportFragmentManager.beginTransaction()
                .replace(R.id.linearLayout, AppInfoFragment())
                .addToBackStack(null)
                .commit()}
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        lateinit var db: AppDatabase
        lateinit var scoreDao: ScoreDao
    }
}