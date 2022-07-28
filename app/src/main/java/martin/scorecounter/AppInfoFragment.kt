package martin.scorecounter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import martin.scorecounter.databinding.FragmentAppInfoBinding
import martin.scorecounter.databinding.FragmentMainMenuBinding
import martin.scorecounter.tennis.TennisDoublesSettingsFragment
import martin.scorecounter.tennis.TennisSinglesSettingsFragment

class AppInfoFragment: Fragment() {

    private var _binding: FragmentAppInfoBinding? = null
    private val binding get() = _binding!!

    private val EMAIL = "makoefer@edu.aau.at"
    private val GITHUB = "https://github.com/makoefer/ScoreCounter"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentAppInfoBinding.inflate(inflater, container, false)

        binding.aboutBtnEmail.setOnClickListener(){

            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$EMAIL"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Development - APP")
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        }

        binding.aboutBtnGithub.setOnClickListener(){
            val webIntent: Intent = Uri.parse(GITHUB).let { webpage ->
                Intent(Intent.ACTION_VIEW, webpage)
            }
            startActivity(webIntent)
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}