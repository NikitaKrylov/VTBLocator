package com.example.misisvtbhack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.misisvtbhack.databinding.FragmentBranchDetailBinding


class BranchDetailFragment : Fragment() {
   private lateinit var binding: FragmentBranchDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBranchDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}