package io.github.kabirnayeem99.dumarketingstudent.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var bottomNavBar: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

        setUpBottomNavBar()
        setUpNavigationDrawer()


    }


    private fun setUpBottomNavBar() {

        bottomNavBar = binding.bottomNavBar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fNavHost) as NavHostFragment

        val navController = navHostFragment.navController


        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.enter)
            .setExitAnim(R.anim.exit)
            .setPopEnterAnim(R.anim.enter)
            .setPopExitAnim(R.anim.exit)
            .setPopUpTo(navController.graph.startDestination, false)
            .build()


        bottomNavBar.setOnNavigationItemSelectedListener { menu_item ->
            when (menu_item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment, null, options)
                }
                R.id.noticeFragment -> {
                    navController.navigate(R.id.noticeFragment, null, options)
                }
                R.id.galleryFragment -> {
                    navController.navigate(R.id.galleryFragment, null, options)
                }
                R.id.facultyFragment -> {
                    navController.navigate(R.id.facultyFragment, null, options)
                }
                R.id.aboutFragment -> {
                    navController.navigate(R.id.aboutFragment, null, options)
                }
            }
            true
        }

        bottomNavBar.setOnNavigationItemReselectedListener { return@setOnNavigationItemReselectedListener }

//        NavigationUI.setupWithNavController(bottomNavBar, navHostFragment.navController)
    }

    private fun setUpNavigationDrawer() {
        val navigationView = binding.navDrawerView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fNavHost) as NavHostFragment

        NavigationUI.setupWithNavController(navigationView, navHostFragment.navController)

        drawerLayout = binding.mainDrawerLayout
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_drawer_open_desc,
            R.string.nav_drawer_close_desc
        )

        drawerLayout.addDrawerListener(drawerToggle)

        drawerToggle.syncState()

    }


    private fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    companion object {
        private const val TAG = "MainActivity"
    }
}