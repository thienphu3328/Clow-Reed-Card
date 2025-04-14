package com.example.clowreed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.clowreed.databinding.ActivityCategoryBinding

class ActivityCategory  : AppCompatActivity(){

    private lateinit var rvAdapter: CategoryAdapter
    private lateinit var dataList: ArrayList<Clow>
    private lateinit var binding: ActivityCategoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tittle.text=intent.getStringExtra("TITTLE")
        setUpRecyclerView()
        binding.goBackHome.setOnClickListener {
            finish()
        }
    }

    private fun setUpRecyclerView() {
        dataList = ArrayList()
        binding.rvCategory.layoutManager = LinearLayoutManager(this)
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "db_name")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("sakuracards.db")
            .build()
        val daoObject = db.getDao()
        val clows = daoObject.getAll()
        for (i in clows!!.indices) {
            if (clows[i]!!.category.contains(intent.getStringExtra("CATEGORY")!!)) {
                dataList.add(clows[i]!!)
            }
        }
        rvAdapter = CategoryAdapter(dataList, this)
        binding.rvCategory.adapter = rvAdapter
    }
}