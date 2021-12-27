package io.github.kabirnayeem99.dumarketingstudent.presentation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.common.base.BaseActivity
import io.github.kabirnayeem99.dumarketingstudent.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {


    override val layout: Int
        get() = R.layout.activity_main

    lateinit var bottomNavBar: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpNavigation()
    }

    private fun setUpNavigation() {
        bottomNavBar = binding.bottomNavBar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fNavHost) as NavHostFragment

        navController = navHostFragment.findNavController()

        binding.bottomNavBar.setupWithNavController(navController)
    }


}