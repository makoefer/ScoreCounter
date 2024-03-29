package martin.scorecounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import martin.scorecounter.databinding.FragmentMainMenuBinding
import martin.scorecounter.tennis.GameHistoryFragment
import martin.scorecounter.tennis.TennisDoublesSettingsFragment
import martin.scorecounter.tennis.TennisSinglesSettingsFragment

class MainMenuFragment: Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)

        binding.btnMmTennisSingles.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.linearLayout, TennisSinglesSettingsFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnMmTennisDoubles.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.linearLayout, TennisDoublesSettingsFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnMmTennisHistory.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.linearLayout, GameHistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}