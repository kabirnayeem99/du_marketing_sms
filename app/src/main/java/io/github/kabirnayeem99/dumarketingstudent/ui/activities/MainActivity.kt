package io.github.kabirnayeem99.dumarketingstudent.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.ActivityMainBinding
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.GalleryViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.NoticeViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.RoutineViewModel
import nl.joery.animatedbottombar.AnimatedBottomBar
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var bottomNavBar: AnimatedBottomBar
    lateinit var navController: NavController

    var isInHome = true

    @Inject
    lateinit var scale: ScaleAnimation


    val galleryViewModel: GalleryViewModel by viewModels()
    val routineViewModel: RoutineViewModel by viewModels()
    val noticeViewModel: NoticeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(
            layoutInflater
        )

        setContentView(binding.root)

        binding.bottomNavBar.startAnimation(scale)

//        setUpBottomNavBar()
        setUpAnimatedBottomBar()
        setUpNavigationDrawer()
    }


    private fun setUpAnimatedBottomBar() {

        bottomNavBar = binding.bottomNavBar

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fNavHost) as NavHostFragment

        navController = navHostFragment.findNavController()


        binding.bottomNavBar.onTabSelected = { tab ->
            when (tab.id) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.toHomeFragment)
                    isInHome = true
                }
                R.id.aboutFragment -> {
                    navController.navigate(R.id.toAboutFragment)
                    isInHome = false
                }
                R.id.noticeFragment -> {
                    navController.navigate(R.id.toNoticeFragment)
                    isInHome = false
                }
                R.id.facultyFragment -> {
                    navController.navigate(R.id.toFacultyFragment)
                    isInHome = false
                }
                R.id.galleryFragment -> {
                    navController.navigate(R.id.toGalleryFragment)
                    isInHome = false
                }
            }
        }

        binding.bottomNavBar.onTabReselected = { tab ->
            Toast.makeText(this, "You are already in ${tab.title}", Toast.LENGTH_SHORT).show()
        }
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

    override fun onBackPressed() {

        closeDrawer()

        if (isInHome) {
            finish()
        } else {
            bottomNavBar.selectTabAt(0)
            navController.navigate(R.id.toHomeFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}