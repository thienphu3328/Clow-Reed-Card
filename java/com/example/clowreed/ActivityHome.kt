package com.example.clowreed

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.clowreed.databinding.ActivityHomeBinding

class ActivityHome : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var rvAdapter: ElementalAdapter
    private lateinit var dataList: ArrayList<Clow>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.yourday.setOnClickListener {
            val intent = Intent(this@ActivityHome, RandomCard::class.java)
            startActivity(intent)
        }
        binding.search.setOnClickListener {
            startActivity(Intent(this@ActivityHome, ActivitySearch::class.java))
        }
        binding.purple.setOnClickListener {
            // TODO: Handle purple click
            val myIntent = Intent(this@ActivityHome, ActivityCategory::class.java)
            myIntent.putExtra("TITTLE", "Purple")
            myIntent.putExtra("CATEGORY", "purple")
            startActivity(myIntent)
        }
        binding.red.setOnClickListener {
            // TODO: Handle red click
            val myIntent = Intent(this@ActivityHome, ActivityCategory::class.java)
            myIntent.putExtra("TITTLE", "Red")
            myIntent.putExtra("CATEGORY", "red")
            startActivity(myIntent)
        }
        binding.green.setOnClickListener {
            // TODO: Handle green click
            val myIntent = Intent(this@ActivityHome, ActivityCategory::class.java)
            myIntent.putExtra("TITTLE", "Green")
            myIntent.putExtra("CATEGORY", "green")
            startActivity(myIntent)
        }
        binding.blue.setOnClickListener {
            // TODO: Handle blue click
            val myIntent = Intent(this@ActivityHome, ActivityCategory::class.java)
            myIntent.putExtra("TITTLE", "Blue")
            myIntent.putExtra("CATEGORY", "blue")
            startActivity(myIntent)
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        dataList = ArrayList()
        binding.rvPopular.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val db = Room.databaseBuilder(this@ActivityHome, AppDatabase::class.java, "db_name")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("sakuracards.db")
            .build()
        val daoObject = db.getDao()
        val clows = daoObject.getAll()
        for (i in clows!!.indices) {
            if (clows[i]!!.category.contains("main")) {
                dataList.add(clows[i]!!)
            }
        }
        rvAdapter = ElementalAdapter(dataList, this)
        binding.rvPopular.adapter = rvAdapter
    }
}
