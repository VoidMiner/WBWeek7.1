package com.ands.wb5weekweb.fragments.tinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.ands.wb5weekweb.R
import com.ands.wb5weekweb.databinding.FragmentTinderBinding
import com.ands.wb5weekweb.viewmodels.tinder.TinderViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TinderFragment : Fragment() {

    private lateinit var binding: FragmentTinderBinding
    private val viewModel: TinderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTinderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserveCat()
        setupListeners()
    }

    private fun setupObserveCat() {
        viewModel.currentCat.observe(viewLifecycleOwner) {
            binding.image.setImageURI(it.url)
        }
    }

    private fun setupListeners() {
        binding.apply {
            like.setOnClickListener() { makeVote(LIKE) }
            dislike.setOnClickListener() { makeVote(DISLIKE) }
            openLikedList.setOnClickListener() {
                Navigation.findNavController(binding.root).navigate(R.id.action_tinderFragment_to_likedCatsFragment)
            }
        }
    }

    private fun makeVote(vote: Int) {
        viewModel.createVote(value = vote)
    }

    companion object {
        const val DISLIKE = 0
        const val LIKE = 1
    }

}