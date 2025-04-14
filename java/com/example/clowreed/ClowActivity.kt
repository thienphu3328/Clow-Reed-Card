package com.example.clowreed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.clowreed.databinding.ClowActivityBinding


class ClowActivity : AppCompatActivity() {
    private lateinit var binding: ClowActivityBinding
    private var imgCrop = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ClowActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load(intent.getStringExtra("img")).into(binding.itemImage)
        binding.tittle.text=intent.getStringExtra("tittle")
        binding.ingData.text=intent.getStringExtra("ing")
        binding.infoData.text=intent.getStringExtra("des")
        val ing = intent.getStringExtra("ing")?.split("\n".toRegex())?.dropLastWhile { it.isEmpty() }
            ?.toTypedArray()
        val stringBuilder = StringBuilder()
        for (i in 1 until ing!!.size) {
            stringBuilder.append("- ${ing[i]}\n")
        }
        binding.ingData.text = stringBuilder.toString().trimIndent()

        // Set initial visibility of text views
        binding.ingData.visibility = View.GONE
        binding.infoData.visibility = View.GONE
        binding.fullScreen.setOnClickListener {
            if (imgCrop) {
                binding.itemImage.scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(this).load(intent.getStringExtra("img")).into(binding.itemImage)
                binding.shade.visibility=View.GONE
            } else {
                binding.itemImage.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this).load(intent.getStringExtra("img")).into(binding.itemImage)
                binding.shade.visibility=View.GONE
            }
            imgCrop = !imgCrop
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.ing.setOnClickListener {
            // Toggle visibility of the "ing" text
            binding.ingData.visibility = if (binding.ingData.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            binding.infoData.visibility = View.GONE
        }

        binding.step.setOnClickListener {
            // Toggle visibility of the "step" text
            binding.ingData.visibility = View.GONE
            binding.infoData.visibility = if (binding.infoData.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }
}