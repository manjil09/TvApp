package com.manjil.tvapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.manjil.tvapplication.databinding.FragmentOverviewBinding


private const val ARG_title = "title"
private const val ARG_description = "description"
private const val ARG_background_url = "backgroundUrl"

/**
 * A simple [Fragment] subclass.
 * Use the [OverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OverviewFragment : Fragment() {
    private var title: String? = null
    private var description: String? = null
    private var backgroundUrl: String? = null
    private lateinit var binding: FragmentOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_title)
            description = it.getString(ARG_description)
            backgroundUrl = it.getString(ARG_background_url)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOverviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext()).load(backgroundUrl).centerCrop().into(binding.ivBackground)
        binding.tvTitle.text = title
        binding.tvDescription.text = description
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, description: String, backgroundUrl: String) =
            OverviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_title, title)
                    putString(ARG_description, description)
                    putString(ARG_background_url, backgroundUrl)
                }
            }
    }
}