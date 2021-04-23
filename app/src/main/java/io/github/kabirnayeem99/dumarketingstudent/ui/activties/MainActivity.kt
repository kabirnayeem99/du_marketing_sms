package io.github.kabirnayeem99.dumarketingstudent.ui.activties

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.kabirnayeem99.dumarketingstudent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}