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

    var gamesMe: Int = 0
    var gamesYou: Int = 0

    var playerDao = MainActivity.playerDao
    var scoreDao = MainActivity.scoreDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentTennisSinglesBasicgameBinding.inflate(inflater, container, false)
        points = resources.getStringArray(R.array.tennisPoints)

        binding.tvNameMe.text = if (arguments?.getString("nameMe") == "") "Player 1" else arguments?.getString("nameMe")
        binding.tvNameYou.text = if (arguments?.getString("nameYou") == "") "Player 2" else arguments?.getString("nameYou")


        binding.btnPointMe.setOnClickListener{
            pointsMe++
            Log.d(TAG, "P1 scored, ${points[pointsMe]}:${points[pointsYou]}")


            if (points[pointsMe] == "AD" && points[pointsYou] == "AD"){
                pointsMe--
                pointsYou--
            } else if ((points[pointsMe] == "AD" && points[pointsYou] != "40") || points[pointsMe] == "W"){
                gamesMe++
                pointsMe = 0
                pointsYou = 0
            }

            updateTextViews()
        }

        binding.btnPointYou.setOnClickListener{
            pointsYou++
            Log.d(TAG, "P2 scored, ${points[pointsMe]}:${points[pointsYou]}")

            if (points[pointsYou] == "AD" && points[pointsMe] == "AD"){
                pointsMe--
                pointsYou--
            } else if ((points[pointsYou] == "AD" && points[pointsMe] != "40") || points[pointsYou] == "W"){
                gamesYou++
                pointsMe = 0
                pointsYou = 0
            }

            updateTextViews()

        }

        runBlocking {
            databaseAccessTesting()
        }


        return binding.root
    }

    private fun updateTextViews() {
        binding.tvPointsMe.text = points[pointsMe]
        binding.tvGamesMe.text = gamesMe.toString()
        binding.tvPointsYou.text = points[pointsYou]
        binding.tvGamesYou.text = gamesYou.toString()
    }

    private suspend fun databaseAccessTesting() {
        playerDao.insertPlayer(Player("Martin", "Köfer"))
        playerDao.insertPlayer(Player("Laura"))
        Log.d(TAG, playerDao.getAllPlayers().toString())

        scoreDao.insertMatch(Match(0,"Tennis", playerDao.getPlayerByName("Martin", "Köfer"), playerDao.getPlayerByName("Laura", ""), null,null, "p1", 2, true, false, Collections.emptyList()))
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