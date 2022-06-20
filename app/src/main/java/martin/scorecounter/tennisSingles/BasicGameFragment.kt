package martin.scorecounter.tennisSingles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import martin.scorecounter.MainActivity
import martin.scorecounter.R
import martin.scorecounter.database.Game
import martin.scorecounter.database.Match
import martin.scorecounter.database.Player
import martin.scorecounter.database.Set
import martin.scorecounter.databinding.FragmentTennisSinglesBasicgameBinding
import java.util.*

class BasicGameFragment: Fragment() {

    private val TAG: String = "TENNIS BASIC SINGLES"

    private var _binding: FragmentTennisSinglesBasicgameBinding? = null
    private val binding get() = _binding!!

    lateinit var points: Array<String>
    var pointsMe: Int = 0
    var pointsYou: Int = 0

    var playerDao = MainActivity.playerDao
    var scoreDao = MainActivity.scoreDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentTennisSinglesBasicgameBinding.inflate(inflater, container, false)

        points = resources.getStringArray(R.array.tennisPoints)

        binding.btnPointMe.setOnClickListener{
            pointsMe++
            Log.d(TAG, "P1 scored, ${points[pointsMe]}:${points[pointsYou]}")
            binding.tvPointsMe.text = points[pointsMe]
        }

        binding.btnPointYou.setOnClickListener{
            pointsYou++
            Log.d(TAG, "P2 scored, ${points[pointsMe]}:${points[pointsYou]}")
            binding.tvPointsYou.text = points[pointsYou]
        }

        runBlocking {
            databaseAccessTesting()
        }


        return binding.root
    }

    private suspend fun databaseAccessTesting() {
        playerDao.insertPlayer(Player(0, "Martin"))
        playerDao.insertPlayer(Player(0, "Laura"))
        Log.d(TAG, playerDao.getAllPlayers().toString())

        scoreDao.insertMatch(Match(0,"Tennis", playerDao.getAllPlayers()[0], playerDao.getAllPlayers()[1], null,null, "p1", 2, true, false, Collections.emptyList()))
        Log.d(TAG, scoreDao.getAllMatches().toString())

        scoreDao.insertSet(Set(0, 1, 2,1, "", Collections.emptyList(), 1))
        Log.d(TAG, scoreDao.getAllSets().toString())

        scoreDao.insertGame(Game(0, 1, "0", "0", "p1","", "", false, 1))
        Log.d(TAG, scoreDao.getAllGames().toString())
        scoreDao.updateGame(Game(1, 1, "30", "15", "p1", "", "1234", false, 1))
        Log.d(TAG, scoreDao.getAllGames().toString())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}