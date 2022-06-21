package martin.scorecounter.tennisSingles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import martin.scorecounter.R
import martin.scorecounter.databinding.FragmentTennisSinglesBasicgameBinding
import martin.scorecounter.databinding.FragmentTennisSinglesSettingsBinding

class TennisSinglesSettingsFragment: Fragment() {

    private val TAG: String = "TENNIS SINGLES SETTINGS"

    private var _binding: FragmentTennisSinglesSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentTennisSinglesSettingsBinding.inflate(inflater, container, false)

        binding.btnTsSettingsStart.setOnClickListener{

            val bundle = Bundle()
            bundle.putString("nameMe", binding.tiTsPlayer1.text.toString())
            bundle.putString("nameYou", binding.tiTsPlayer2.text.toString())

            val bgf = BasicGameFragment()
            bgf.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.linearLayout, bgf)
                .addToBackStack(null)
                .commit()
        }


        return binding.root
    }
}