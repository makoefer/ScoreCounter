package martin.scorecounter.tennis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import martin.scorecounter.R
import martin.scorecounter.databinding.FragmentTennisDoublesSettingsBinding

class TennisDoublesSettingsFragment: Fragment() {

    private val TAG: String = "TENNIS DOUBLES SETTINGS"

    private var _binding: FragmentTennisDoublesSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentTennisDoublesSettingsBinding.inflate(inflater, container, false)

        binding.btnTsSettingsStart.setOnClickListener{

            val bundle = Bundle()
            bundle.putString("p1name", binding.tiTsPlayer1.text.toString())
            bundle.putString("p2name", binding.tiTsPlayer2.text.toString())
            bundle.putString("p12name", binding.tiTsPlayer12.text.toString())
            bundle.putString("p22name", binding.tiTsPlayer22.text.toString())
            var rB = activity?.findViewById<RadioButton>(binding.rgTsSetsToWin.checkedRadioButtonId)
            bundle.putString("numberOfSets", (rB?.text as String?))
            var rB2 = activity?.findViewById<RadioButton>(binding.rgTsFirstserve.checkedRadioButtonId)
            bundle.putString("firstServe", (rB2?.text as String?))
            bundle.putBoolean("matchTieBreak", binding.swMatchTieBreak.isChecked)

            val bgf = TennisDoublesGameFragment()
            bgf.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.linearLayout, bgf)
                .addToBackStack(null)
                .commit()
        }


        return binding.root
    }
}