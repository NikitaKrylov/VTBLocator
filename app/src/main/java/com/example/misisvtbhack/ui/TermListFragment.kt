package com.example.misisvtbhack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.misisvtbhack.R
import com.example.misisvtbhack.databinding.FragmentTermListBinding


class TermListFragment : Fragment() {

    private lateinit var binding: FragmentTermListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTermListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}