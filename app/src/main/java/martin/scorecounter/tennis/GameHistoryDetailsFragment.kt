package martin.scorecounter.tennis

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import martin.scorecounter.MainActivity
import martin.scorecounter.R
import martin.scorecounter.database.Game
import martin.scorecounter.database.Match
import martin.scorecounter.database.Set
import martin.scorecounter.databinding.FragmentGameHistoryDetailsBinding

class GameHistoryDetailsFragment: Fragment() {

    private var _binding: FragmentGameHistoryDetailsBinding? = null
    private val binding get() = _binding!!

    var scoreDao = MainActivity.scoreDao

    lateinit var match: Match
    lateinit var sets: List<Set>
    lateinit var currentPointHistoryLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentGameHistoryDetailsBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.linearLayout, GameHistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        var matchId: Long = arguments?.getLong("match_id") ?: -1

        if (matchId.equals(-1)) {
            Toast.makeText(context, "Match not found!", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.linearLayout, GameHistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        runBlocking {
            match = scoreDao.getMatchById(matchId)
            sets = scoreDao.getSetsForMatch(matchId)
        }

        binding.lblThdDate.text = match.date

        for (set in sets){
            newGameScoreLayout(set.p1Games, set.p2Games)
            newGameHistory(set.id)
            newPointHistoryDivider(set.setNumber.toString())
        }

        return binding.root
    }

    private fun newGameScoreLayout(p1Games: Int, p2Games: Int) {

        val llparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        llparams.gravity = Gravity.RIGHT

        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5F, resources.displayMetrics).toInt()
        val tvparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        tvparams.setMargins(margin,0,margin,0)

        var ll = LinearLayout(context)
        ll.orientation = LinearLayout.VERTICAL
        ll.layoutParams = llparams

        var tv1 = TextView(context)
        tv1.text = p1Games.toString()
        tv1.textSize = 17F
        tv1.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        tv1.layoutParams = tvparams
        ll.addView(tv1)

        var tv2 = TextView(context)
        tv2.text = p2Games.toString()
        tv2.textSize = 17F
        tv2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        tv2.layoutParams = tvparams
        ll.addView(tv2)

        val parent = binding.ghdGameScoreLayout
        parent.addView(ll)
    }

    private fun newGameHistory(setId: Long){
        var games: List<Game>
        runBlocking { games = scoreDao.getGamesForSet(setId) }

        for (game in games){
            newPointHistoryGameLayout(game)

            var temp = game.pointsHistory.length
            var pointHistory = game.pointsHistory.substring(1, temp-1).split(",")

            for (point in pointHistory){
                var mpoint = point.trim()
                mpoint = mpoint.substring(1, mpoint.length-1)

                var pointP1 = mpoint.split("-")[0]
                var pointP2 = mpoint.split("-")[1]
                var curPoint = TPoint(pointP1, pointP2)
                createPointView(curPoint)
            }

        }
    }

    @SuppressLint("ResourceAsColor")
    private fun createPointView(point: TPoint) {

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
        tv1.gravity = Gravity.CENTER
        tv1.layoutParams = params

        var tv2 = TextView(context)
        tv2.text = point.p2
        tv2.textSize = 15F
        tv2.gravity = Gravity.CENTER
        tv2.layoutParams = params

        //---add textviews---
        layout.addView(tv1)
        layout.addView(tv2)

        val parent = currentPointHistoryLayout
        parent.addView(layout, params)

    }

    private fun newPointHistoryGameLayout(game: Game) {

        val parent = binding.llPointHistory
        var pointHistoryGame = createPointHistoryLayout(game.servingPlayerP1, game.currentGamesP1, game.currentGamesP2)
        currentPointHistoryLayout = pointHistoryGame.getChildAt(0) as LinearLayout
        parent.addView(pointHistoryGame, 0)
        val mScrollView = binding.nsvPointHistory
        mScrollView.post(Runnable { mScrollView.fullScroll(ScrollView.FOCUS_UP) })
    }

    private fun createPointHistoryLayout(serve: Boolean, currentGamesP1: Int, currentGamesP2: Int): HorizontalScrollView {

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
        tv1.text = currentGamesP1.toString()
        tv1.textSize = 17F
        tv1.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
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
        tv2.text = currentGamesP2.toString()
        tv2.textSize = 17F
        tv2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}