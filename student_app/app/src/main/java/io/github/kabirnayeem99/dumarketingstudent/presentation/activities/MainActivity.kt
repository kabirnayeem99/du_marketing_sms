package io.github.kabirnayeem99.dumarketingstudent.presentation.activities

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.ActivityMainBinding
import io.github.kabirnayeem99.dumarketingstudent.presentation.base.BaseActivity
import io.github.kabirnayeem99.dumarketingstudent.common.util.isDarkThemeOn
import io.github.kabirnayeem99.dumarketingstudent.common.util.showToast
import nl.joery.animatedbottombar.AnimatedBottomBar
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {


    override val layout: Int
        get() = R.layout.activity_main

    lateinit var bottomNavBar: AnimatedBottomBar
    lateinit var navController: NavController

    var isAtHome = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()
        setUpNavigation()
        setUpAnimatedBottomBar()


    }

    private fun setUpNavigation() {
        setUpAnimatedBottomBar()
        setUpAboutNavigation()
    }

    private fun setUpAboutNavigation() {
        binding.ivMenuIcon.setOnClickListener {
            showToast("About Info Should be shown")
            navController.navigate(R.id.toAboutFragment)
            isAtHome = false
            changeBottomBarBackgroundColor()
            changeAppbarTitle("About")
        }
    }

    private fun changeAppbarTitle(text: String) {
        binding.toolbarTitle.text = text
    }

    private fun setUpToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarTitle.text = "Home"
    }

    private fun setUpAnimatedBottomBar() {
        bottomNavBar = binding.bottomNavBar
        changeBottomBarBackgroundColor()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fNavHost) as NavHostFragment

        navController = navHostFragment.findNavController()


        bottomNavBar.onTabSelected = { tab ->
            when (tab.id) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.toHomeFragment)
                    isAtHome = true
                    changeBottomBarBackgroundColor()
                    changeAppbarTitle("Home")
                }
                R.id.ebookFragment -> {
                    navController.navigate(R.id.toEbookFragment)
                    isAtHome = false
                    changeBottomBarBackgroundColor()
                    changeAppbarTitle("Ebooks")

                }
                R.id.noticeFragment -> {
                    navController.navigate(R.id.toNoticeFragment)
                    isAtHome = false
                    changeBottomBarBackgroundColor()
                    changeAppbarTitle("Notice")
                }
                R.id.facultyFragment -> {
                    navController.navigate(R.id.toFacultyFragment)
                    isAtHome = false
                    changeBottomBarBackgroundColor()
                    changeAppbarTitle("Faculty")

                }
                R.id.galleryFragment -> {
                    navController.navigate(R.id.toGalleryFragment)
                    isAtHome = false
                    changeBottomBarBackgroundColor()
                    changeAppbarTitle("Gallery")
                }
            }
        }

        binding.bottomNavBar.onTabReselected = { tab ->
            Timber.e("The user has selected $tab again")
        }
    }

    private fun changeBottomBarBackgroundColor() {

        if (!this::bottomNavBar.isInitialized) {
            return
        }
        val isDarkThemeOn = this.isDarkThemeOn()

        try {
            if (isDarkThemeOn) {
                if (isAtHome) {
                    bottomNavBar.setBackgroundColor(resources.getColor(R.color.card_view_dark))
                } else {
                    bottomNavBar.setBackgroundColor(resources.getColor(R.color.black))
                }
            } else {
                bottomNavBar.setBackgroundColor(resources.getColor(R.color.white))
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onBackPressed() {
        if (isAtHome) {
            finish()
        } else {
            bottomNavBar.selectTabAt(0)
            changeBottomBarBackgroundColor()
            navController.navigate(R.id.toHomeFragment)
        }
    }


}