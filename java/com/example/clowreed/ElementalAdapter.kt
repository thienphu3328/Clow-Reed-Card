package com.example.clowreed

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clowreed.databinding.ElementalReviewItemBinding

class ElementalAdapter (private var dataList:ArrayList<Clow>, var context: Context ):RecyclerView.Adapter<ElementalAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ElementalReviewItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= ElementalReviewItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sakuracards = dataList[position]

        Glide.with(context).load(sakuracards.img).into(holder.binding.popularImg)
        holder.binding.popularTxt.text = sakuracards.tittle

    }
}