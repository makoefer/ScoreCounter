package martin.scorecounter.tennis

import android.annotation.SuppressLint
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import martin.scorecounter.MainActivity
import martin.scorecounter.MainMenuFragment
import martin.scorecounter.R
import martin.scorecounter.database.Game
import martin.scorecounter.database.Match
import martin.scorecounter.database.Player
import martin.scorecounter.database.Set
import martin.scorecounter.databinding.FragmentTennisSinglesGameBinding
import martin.scorecounter.enums.TennisGamePhases
import java.util.*


class TennisSinglesGameFragment: Fragment() {

    private val TAG: String = "TENNIS SINGLES GAME"

    private var _binding: FragmentTennisSinglesGameBinding? = null
    private val binding get() = _binding!!

    lateinit var gamePhase: TennisGamePhases
    lateinit var points: Array<String>

    lateinit var currentMatch: TMatch
    lateinit var currentSet: TSet
    lateinit var currentGame: TGame
    lateinit var currentPointHistoryLayout: LinearLayout
    lateinit var currentGameScoreLayout: LinearLayout
    lateinit var currentP1Games: TextView
    lateinit var currentP2Games: TextView

    var playerDao = MainActivity.playerDao
    var scoreDao = MainActivity.scoreDao

    var maxSets: Int = 2
    var firstServeP1 = false
    var matchTieBreak = false

    var finished = false

    lateinit var pointHistoryLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentTennisSinglesGameBinding.inflate(inflater, container, false)

        var p1Name: String? = if (arguments?.getString("nameMe") != "") arguments?.getString("nameMe") else "Player 1"
        var p2Name: String? = if (arguments?.getString("nameYou") != "") arguments?.getString("nameYou") else "Player 2"

        binding.tvNameMe.text = p1Name
        binding.tvNameYou.text = p2Name
        binding.p1Name.text = p1Name
        binding.p2Name.text = p2Name

        firstServeP1 = arguments?.getString("firstServe") == "Player 1"
        maxSets = arguments?.getString("numberOfSets")?.toInt() ?: 2
        matchTieBreak = arguments?.getBoolean("matchTieBreak") ?: false

        gamePhase = TennisGamePhases.TENNIS_NORMAL_GAME
        points = resources.getStringArray(R.array.tennisPoints)


        currentMatch = TMatch("Singles", p1Name!!, p2Name!!, null, null, firstServeP1, maxSets, matchTieBreak)
        currentSet = currentMatch.newSet()
        currentGame = currentSet.newGame()

        newPointHistoryGameLayout()
        newGameScoreLayout()

        binding.btnPointP1.setOnClickListener{

            pointWon(1)
            updateTextViews()
        }

        binding.btnPointP2.setOnClickListener{

            pointWon(2)
            updateTextViews()
        }

        runBlocking {
            // databaseAccessTesting()
        }

        return binding.root
    }

    private fun pointWon(byPlayer: Int) {

        currentGame.pointWon(byPlayer)

        if (currentGame.finished){
            currentSet.gameWon(byPlayer)

            currentP1Games.text = currentSet.p1Games.toString()
            currentP2Games.text = currentSet.p2Games.toString()

            if (currentSet.finished){
                currentMatch.setWon(byPlayer, currentGame.p1Serving)
                newPointHistoryDivider(currentSet.setNr.toString())

                var p1color = if (currentSet.p1Games>currentSet.p2Games) resources.getColor(R.color.tblack) else resources.getColor(R.color.tgrey)
                var p2color = if (currentSet.p2Games>currentSet.p1Games) resources.getColor(R.color.tblack) else resources.getColor(R.color.tgrey)
                currentP1Games.setTextColor(p1color)
                currentP2Games.setTextColor(p2color)

                if (currentMatch.finished){

                    Toast.makeText(context, "Player ${currentMatch.matchWinner} is the match winner!", Toast.LENGTH_SHORT).show()
                    finished = true

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.linearLayout, MainMenuFragment())
                        .addToBackStack(null)
                        .commit()

                } else {
                    if (currentMatch.decidingSet && matchTieBreak){
                        gamePhase = TennisGamePhases.TENNIS_MATCH_TIE_BREAK
                    }
                    currentMatch.newSet()
                    currentSet = currentMatch.getCurrentSet()
                    currentSet.newGame()
                    currentGame = currentSet.getCurrentGame()
                    newPointHistoryGameLayout()
                    newGameScoreLayout()
                }
            } else {
                updatePointHistory(byPlayer)
                currentSet.newGame()
                currentGame = currentSet.getCurrentGame()
                newPointHistoryGameLayout()
            }
        }

        createPointView(currentGame.lastPoint, byPlayer)
        updateServingPlayer()
    }

    private fun updateServingPlayer() {
        binding.p1ServingSymbol.visibility = if (currentGame.p1Serving) ImageView.VISIBLE else ImageView.INVISIBLE
        binding.p1ServingSymbol1.visibility = if (currentGame.p1Serving) ImageView.VISIBLE else ImageView.INVISIBLE
        binding.p2ServingSymbol.visibility = if (currentGame.p1Serving) ImageView.INVISIBLE else ImageView.VISIBLE
        binding.p2ServingSymbol1.visibility = if (currentGame.p1Serving) ImageView.INVISIBLE else ImageView.VISIBLE
    }

    private fun updateTextViews() {

        if (!currentGame.tiebreak) {
            binding.p1Points.text = points[currentGame.p1Points]
            binding.tvPointsMe.text = points[currentGame.p1Points]
            binding.p2Points.text = points[currentGame.p2Points]
            binding.tvPointsYou.text = points[currentGame.p2Points]
        } else {
            binding.p1Points.text = currentGame.p1Points.toString()
            binding.tvPointsMe.text = currentGame.p1Points.toString()
            binding.p2Points.text = currentGame.p2Points.toString()
            binding.tvPointsYou.text = currentGame.p2Points.toString()
        }
    }

    private fun updatePointHistory(byPlayer: Int){

        val p1LL = (((currentPointHistoryLayout.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout).getChildAt(0) as TextView)
        p1LL.text = currentSet.p1Games.toString()
        p1LL.visibility = TextView.VISIBLE
        var p1Color = resources.getColor(R.color.tgrey)
        if (currentGame.p1BreakPoint){p1Color = resources.getColor(R.color.tbreak)} else if (byPlayer == 1){p1Color = resources.getColor(R.color.tblack)}
        p1LL.setTextColor(p1Color)

        val p2LL = (((currentPointHistoryLayout.getChildAt(0) as LinearLayout).getChildAt(1) as LinearLayout).getChildAt(0) as TextView)
        p2LL.text = currentSet.p2Games.toString()
        p2LL.visibility = TextView.VISIBLE
        var p2Color = resources.getColor(R.color.tgrey)
        if (currentGame.p2BreakPoint){p2Color = resources.getColor(R.color.tbreak)}
            else if (byPlayer == 2){p2Color = resources.getColor(R.color.tblack)}
        p2LL.setTextColor(p2Color)
    }

    @SuppressLint("ResourceAsColor")
    private fun createPointView(point: TPoint, byPlayer: Int) {

        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(7,0, 7, 0)

        //---create a layout---
        var layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        //---create textviews---
        var tv1= TextView(context)
        tv1.text = point.p1
        tv1.textSize = 15F
        var textColor = resources.getColor(R.color.tgrey)
        if (currentGame.p1BreakPoint){textColor = resources.getColor(R.color.tbreak)} else if (byPlayer == 1){textColor = resources.getColor(R.color.tblack)}
        tv1.setTextColor(textColor)
        tv1.gravity = Gravity.CENTER
        tv1.layoutParams = params

        var tv2 = TextView(context)
        tv2.text = point.p2
        tv2.textSize = 15F
        textColor = resources.getColor(R.color.tgrey)
        if (currentGame.p2BreakPoint){textColor = resources.getColor(R.color.tbreak)} else if (byPlayer == 2){textColor = resources.getColor(R.color.tblack)}
        tv2.setTextColor(textColor)
        tv2.gravity = Gravity.CENTER
        tv2.layoutParams = params

        //---add textviews---
        layout.addView(tv1)
        layout.addView(tv2)

        val parent = currentPointHistoryLayout
        parent.addView(layout, params)

    }

    private fun newPointHistoryGameLayout() {

        val parent = binding.llPointHistory
        var pointHistoryGame = createPointHistoryLayout(currentGame.p1Serving)
        currentPointHistoryLayout = pointHistoryGame.getChildAt(0) as LinearLayout
        parent.addView(pointHistoryGame, 0)
        val mScrollView = binding.nsvPointHistory
        mScrollView.post(Runnable { mScrollView.fullScroll(ScrollView.FOCUS_UP) })
    }

    private fun newGameScoreLayout() {

        val llparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        llparams.gravity = Gravity.RIGHT

        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5F, resources.displayMetrics).toInt()
        val tvparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        tvparams.setMargins(margin,0,margin,0)

        var ll = LinearLayout(context)
        ll.orientation = LinearLayout.VERTICAL
        ll.layoutParams = llparams
        currentGameScoreLayout = ll

        var tv1 = TextView(context)
        tv1.text = "0"
        tv1.textSize = 17F
        tv1.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        tv1.layoutParams = tvparams
        ll.addView(tv1)
        currentP1Games = tv1

        var tv2 = TextView(context)
        tv2.text = "0"
        tv2.textSize = 17F
        tv2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        tv2.layoutParams = tvparams
        ll.addView(tv2)
        currentP2Games = tv2

        val parent = binding.gameScoreLayout
        parent.addView(ll)
    }

    private fun createPointHistoryLayout(serve: Boolean): HorizontalScrollView {

        // ----------- Layout Params ----------------
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5F, resources.displayMetrics).toInt()

        val svparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        svparams.setMargins(margin,0,margin,margin)

        val llparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT)

        val llparams1 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50F, resources.displayMetrics).toInt())

        val llparams2 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        val tvparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        tvparams.setMargins(margin,0,margin,0)
        tvparams.weight = 1F

        val tvparams2 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        tvparams.setMargins(margin,0,margin,0)

        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20F, resources.displayMetrics)
        val ivparams = LinearLayout.LayoutParams(width.toInt(), width.toInt())
        ivparams.gravity = Gravity.CENTER_VERTICAL
        // ------------------------------------------

        var sv = HorizontalScrollView(context)
        sv.layoutParams = svparams
        sv.setBackgroundResource(R.drawable.point_history_background)

        var llv1 = LinearLayout(context)
        llv1.orientation = LinearLayout.VERTICAL
        llv1.layoutParams = llparams

            var llh1 = LinearLayout(context)
            llh1.orientation = LinearLayout.HORIZONTAL
            llh1.layoutParams = llparams2

                var tv1 = TextView(context)
                tv1.text = "1"
                tv1.textSize = 17F
                tv1.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                tv1.visibility = TextView.INVISIBLE
                tv1.layoutParams = tvparams
                llh1.addView(tv1)

                if(serve){
                    var iv1 = ImageView(context)
                    iv1.scaleX = 0.6F
                    iv1.scaleY = 0.6F
                    iv1.layoutParams = ivparams
                    iv1.setImageResource(R.mipmap.tennisball)
                    llh1.addView(iv1)
                }

        llv1.addView(llh1)

            var llh2 = LinearLayout(context)
            llh2.orientation = LinearLayout.HORIZONTAL
            llh2.layoutParams = llparams2

                var tv2 = TextView(context)
                tv2.text = "0"
                tv2.textSize = 17F
                tv2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                tv2.visibility = TextView.INVISIBLE
                tv2.layoutParams = tvparams
                llh2.addView(tv2)

                if(!serve){
                    var iv2 = ImageView(context)
                    iv2.scaleX = 0.6F
                    iv2.scaleY = 0.6F
                    iv2.layoutParams = ivparams
                    iv2.setImageResource(R.mipmap.tennisball)
                    llh2.addView(iv2)
                }

        llv1.addView(llh2)

        llparams.setMargins(margin,0,margin,0)
        var llh3 = LinearLayout(context)
        llh3.orientation = LinearLayout.HORIZONTAL
        llh3.gravity = Gravity.CENTER_VERTICAL
        llh3.layoutParams = llparams1

        llh3.addView(llv1)

        var tv3 = TextView(context)
        tv3.layoutParams = tvparams2
        tv3.setBackgroundResource(R.drawable.divider)
        llh3.addView(tv3)

        sv.addView(llh3)

        return sv
    }

    private fun newPointHistoryDivider(set: String){
        val parent = binding.llPointHistory

        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5F, resources.displayMetrics).toInt()

        val tvparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        tvparams.setMargins(margin, 0, margin, 0)
        tvparams.weight = 0.3F

        val divparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        divparams.setMargins(margin, 0, margin, 0)
        divparams.weight = 1F

        val llparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        llparams.setMargins(margin, margin, margin, margin)


        var llh = LinearLayout(context)
        llh.orientation = LinearLayout.HORIZONTAL
        llh.layoutParams = llparams
        llh.gravity = Gravity.CENTER_VERTICAL

        var tv1 = TextView(context)
        tv1.layoutParams = divparams
        tv1.height = 5
        tv1.setBackgroundResource(R.drawable.divider)

        var tv2 = TextView(context)
        tv2.layoutParams = tvparams
        tv2.text = "v   Set Nr. $set   v"
        tv2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

        var tv3 = TextView(context)
        tv3.layoutParams = divparams
        tv3.height = 5
        tv3.setBackgroundResource(R.drawable.divider)

        llh.addView(tv1)
        llh.addView(tv2)
        llh.addView(tv3)

        parent.addView(llh, 0)
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