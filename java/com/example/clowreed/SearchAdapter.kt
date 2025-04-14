package com.example.clowreed

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clowreed.databinding.SearchReviewBinding

class SearchAdapter(private var dataList: ArrayList<Clow>, private var context: Context) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: SearchReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchReviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filerList:ArrayList<Clow>){
        dataList=filerList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].img).into(holder.binding.searchImg)
        holder.binding.searchTxt.text = dataList[position].tittle
        holder.itemView.setOnClickListener{ holder.itemView.setOnClickListener {
            val intent = Intent(context, ClowActivity::class.java)
            intent.putExtra("img", dataList[position].img)
            intent.putExtra("tittle", dataList[position].tittle)
            intent.putExtra("des", dataList[position].des)
            intent.putExtra("ing", dataList[position].ing)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
        }
    }
}