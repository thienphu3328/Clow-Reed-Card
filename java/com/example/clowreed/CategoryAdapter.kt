package com.example.clowreed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clowreed.databinding.CategoryReviewBinding

class CategoryAdapter (private var dataList: ArrayList<Clow>, var context: Context):RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){
    inner class ViewHolder(var binding: CategoryReviewBinding ):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryReviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].img).into(holder.binding.img)
        holder.binding.tittle.text= dataList[position].tittle
        holder.binding.next.setOnClickListener {
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