package martin.scorecounter.tennisSingles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import martin.scorecounter.R
import martin.scorecounter.databinding.FragmentTennisSinglesBasicgameBinding

class BasicGameFragment: Fragment() {

    private val TAG: String = "TENNIS BASIC SINGLES"

    private var _binding: FragmentTennisSinglesBasicgameBinding? = null
    private val binding get() = _binding!!

    lateinit var points: Array<String>
    var pointsMe: Int = 0
    var pointsYou: Int = 0

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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}