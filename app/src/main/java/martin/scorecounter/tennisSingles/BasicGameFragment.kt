package martin.scorecounter.tennisSingles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import martin.scorecounter.MainActivity
import martin.scorecounter.MainMenuFragment
import martin.scorecounter.R
import martin.scorecounter.database.Game
import martin.scorecounter.database.Match
import martin.scorecounter.database.Player
import martin.scorecounter.database.Set
import martin.scorecounter.databinding.FragmentTennisSinglesBasicgameBinding
import martin.scorecounter.enums.TennisGamePhases
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

    var setsMe: Int = 0
    var setsYou: Int = 0

    var playerDao = MainActivity.playerDao
    var scoreDao = MainActivity.scoreDao

    lateinit var gamePhase: TennisGamePhases
    var maxSets: Int = 2
    var winner: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentTennisSinglesBasicgameBinding.inflate(inflater, container, false)
        points = resources.getStringArray(R.array.tennisPoints)

        binding.tvNameMe.text = if (arguments?.getString("nameMe") == "") "Player 1" else arguments?.getString("nameMe")
        binding.tvNameYou.text = if (arguments?.getString("nameYou") == "") "Player 2" else arguments?.getString("nameYou")

        gamePhase = TennisGamePhases.TENNIS_NORMAL_GAME

        binding.btnPointMe.setOnClickListener{
            pointsMe++

            when (gamePhase){
                TennisGamePhases.TENNIS_NORMAL_GAME -> {
                    //Log.d(TAG, "P1 scored, ${points[pointsMe]}:${points[pointsYou]}")
                    checkGameWin()
                    checkSetWin()
                }
                TennisGamePhases.TENNIS_TIE_BREAK -> {
                    //Log.d(TAG, "Tie Break: P1 scored, $pointsMe:$pointsYou")
                    checkTieBreakWin(7)
                }
                TennisGamePhases.TENNIS_MATCH_TIE_BREAK -> {
                    //Log.d(TAG, "Match Tie Break: P1 scored, $pointsMe:$pointsYou")
                    checkTieBreakWin(10)
                }
            }
            checkMatchWin()
            updateTextViews()
        }

        binding.btnPointYou.setOnClickListener{
            pointsYou++

            when (gamePhase){
                TennisGamePhases.TENNIS_NORMAL_GAME -> {
                    //Log.d(TAG, "P2 scored, ${points[pointsMe]}:${points[pointsYou]}")
                    checkGameWin()
                    checkSetWin()
                }
                TennisGamePhases.TENNIS_TIE_BREAK -> {
                    //Log.d(TAG, "Tie Break: P2 scored, $pointsMe:$pointsYou")
                    checkTieBreakWin(7)
                }
                TennisGamePhases.TENNIS_MATCH_TIE_BREAK -> {
                    //Log.d(TAG, "Match Tie Break: P2 scored, $pointsMe:$pointsYou")
                    checkTieBreakWin(10)
                }
            }
            checkMatchWin()
            updateTextViews()
        }

        runBlocking {
           // databaseAccessTesting()
        }


        return binding.root
    }

    private fun checkMatchWin() {
        if (setsMe == maxSets){
            winner = "Player 1"
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.linearLayout, MainMenuFragment())
                .addToBackStack(null)
                .commit()
        } else if (setsYou == maxSets) {
            winner = "Player 2"
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.linearLayout, MainMenuFragment())
                .addToBackStack(null)
                .commit()
        }
        if (setsMe == maxSets-1 && setsYou == maxSets-1){
            gamePhase = TennisGamePhases.TENNIS_MATCH_TIE_BREAK
        }
    }

    private fun checkTieBreakWin(pts: Int) {
        if (pointsMe >= pts && pointsMe-pointsYou >= 2){
            setsMe++
            gamesMe = 0; gamesYou = 0
            pointsMe = 0; pointsYou = 0
            gamePhase = TennisGamePhases.TENNIS_NORMAL_GAME
        } else if (pointsYou >= pts && pointsYou-pointsMe >= 2){
            setsYou++
            gamesMe = 0; gamesYou = 0
            pointsMe = 0; pointsYou = 0
            gamePhase = TennisGamePhases.TENNIS_NORMAL_GAME
        }
    }

    private fun checkSetWin() {
        if (gamesMe >= 6 && gamesMe-gamesYou >= 2) {
            setsMe++
            gamesMe = 0; gamesYou = 0
        } else if (gamesYou >= 6 && gamesYou-gamesMe >= 2){
            setsYou++
            gamesMe = 0; gamesYou = 0
        }
        if (gamesMe == 6 && gamesYou == 6){
            gamePhase = TennisGamePhases.TENNIS_TIE_BREAK
        }
    }

    private fun checkGameWin() {
        if (points[pointsMe] == "AD" && points[pointsYou] == "AD"){
            pointsMe--; pointsYou--
        } else if ((points[pointsMe] == "AD" && points[pointsYou] != "40") || points[pointsMe] == "W"){
            gamesMe++
            pointsMe = 0; pointsYou = 0
        } else if ((points[pointsYou] == "AD" && points[pointsMe] != "40") || points[pointsYou] == "W"){
            gamesYou++
            pointsMe = 0; pointsYou = 0
        }
    }

    private fun updateTextViews() {
        if (gamePhase == TennisGamePhases.TENNIS_NORMAL_GAME) {
            binding.tvPointsMe.text = points[pointsMe]
            binding.tvPointsYou.text = points[pointsYou]
        } else {
            binding.tvPointsMe.text = pointsMe.toString()
            binding.tvPointsYou.text = pointsYou.toString()
        }
        binding.tvGamesMe.text = gamesMe.toString()
        binding.tvGamesYou.text = gamesYou.toString()
        binding.tvSetsMe.text = setsMe.toString()
        binding.tvSetsYou.text = setsYou.toString()
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