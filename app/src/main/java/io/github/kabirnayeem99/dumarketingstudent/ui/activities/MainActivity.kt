package io.github.kabirnayeem99.dumarketingstudent.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottomNavBar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fNavHost) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNavBar, navHostFragment.navController)
    }

    private fun setUpNavigationDrawer() {
        val navigationView = binding.navDrawerView

        drawerLayout = binding.mainDrawerLayout
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_drawer_open_desc,
            R.string.nav_drawer_close_desc
        )

        drawerLayout.addDrawerListener(drawerToggle)

        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener { menu_item ->
            when (menu_item.itemId) {
                R.id.menu_item_book -> {
                    Log.d(TAG, "setUpNavigationDrawer: selected book")
                    closeDrawer()
                }
                R.id.menu_item_website -> {
                    Log.d(TAG, "setUpNavigationDrawer: selected website")
                    closeDrawer()
                }
                R.id.menu_item_review -> {
                    Log.d(TAG, "setUpNavigationDrawer: review us")
                    closeDrawer()
                }
                R.id.menu_item_developer -> {
                    Log.d(TAG, "setUpNavigationDrawer: developer")
                    closeDrawer()
                }
            }
            true
        }
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