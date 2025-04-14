package com.example.clowreed

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.clowreed.databinding.SearchActivityBinding

class ActivitySearch : AppCompatActivity() {
    private lateinit var binding: SearchActivityBinding
    private lateinit var rvAdapter: SearchAdapter
    private lateinit var dataList: ArrayList<Clow>
    private lateinit var clows: List<Clow>

    private fun filterData(filterText: String) {
        val filterData = ArrayList<Clow>()
        for (i in clows.indices) {
            val clow = clows[i]
            if (clow.tittle.lowercase().contains(filterText.lowercase())) {
                filterData.add(clow)
            }
        }
        rvAdapter.filterList(filterData)
    }

    @SuppressLint("ServiceCast", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.search.requestFocus()

        val db = Room.databaseBuilder(this, AppDatabase::class.java, "db_name")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("sakuracards.db")
            .build()
        val daoObject = db.getDao()
        clows = daoObject.getAll()!!.filterNotNull()
        setUpRecyclerView()
        binding.goBackHome.setOnClickListener{
            finish()
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "") {
                    filterData(s.toString())
                }else{
                    setUpRecyclerView()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.rvSearch.setOnTouchListener { v, event ->
            imm.hideSoftInputFromWindow(v.windowToken, 0)
            true
        }
    }

    private fun setUpRecyclerView() {
        dataList = ArrayList()
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        for (i in clows.indices) {
            val clow = clows[i]
            if (clow.category.contains("main")) {
                dataList.add(clow)
            }
        }
        rvAdapter = SearchAdapter(dataList, this)
        binding.rvSearch.adapter = rvAdapter
    }
}