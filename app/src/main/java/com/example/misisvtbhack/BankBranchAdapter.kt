package com.example.misisvtbhack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.misisvtbhack.data.BankBranch
import com.example.misisvtbhack.databinding.BankBranchCardBinding

class BankBranchAdapter : ListAdapter<BankBranch, BankBranchAdapter.BankBranchViewHolder>(diffCalculator) {

    inner class BankBranchViewHolder(binding: BankBranchCardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: BankBranch){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankBranchViewHolder {
        return BankBranchViewHolder(BankBranchCardBinding.inflate(LayoutInflater.from(parent.context)) )
    }

    override fun onBindViewHolder(holder: BankBranchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffCalculator = object : DiffUtil.ItemCallback<BankBranch>(){
            override fun areItemsTheSame(oldItem: BankBranch, newItem: BankBranch): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: BankBranch, newItem: BankBranch): Boolean = oldItem == newItem

        }
    }
}