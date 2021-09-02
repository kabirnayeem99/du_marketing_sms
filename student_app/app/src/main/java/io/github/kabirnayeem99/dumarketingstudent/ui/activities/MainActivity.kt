package io.github.kabirnayeem99.dumarketingstudent.ui.activities

import android.os.Bundle
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.ActivityMainBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.base.BaseActivity
import io.github.kabirnayeem99.dumarketingstudent.util.isDarkThemeOn
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.GalleryViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.NoticeViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.RoutineViewModel
import nl.joery.animatedbottombar.AnimatedBottomBar
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {


    override val layout: Int
        get() = R.layout.activity_main

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
        setUpToolbar()
        setUpAnimatedBottomBar()


        binding.ivMenuIcon.setOnClickListener {
            Toast.makeText(this, "About Info Should be shown", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.toAboutFragment)
            isInHome = false
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
        bottomNavBar.startAnimation(scale)
        changeBottomBarBackgroundColor()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fNavHost) as NavHostFragment

        navController = navHostFragment.findNavController()


        bottomNavBar.onTabSelected = { tab ->
            when (tab.id) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.toHomeFragment)
                    isInHome = true
                    changeBottomBarBackgroundColor()
                    changeAppbarTitle("Home")
                }
                R.id.ebookFragment -> {
                    navController.navigate(R.id.toEbookFragment)
                    isInHome = false
                    changeBottomBarBackgroundColor()
                    changeAppbarTitle("Ebooks")

                }
                R.id.noticeFragment -> {
                    navController.navigate(R.id.toNoticeFragment)
                    isInHome = false
                    changeBottomBarBackgroundColor()
                    changeAppbarTitle("Notice")
                }
                R.id.facultyFragment -> {
                    navController.navigate(R.id.toFacultyFragment)
                    isInHome = false
                    changeBottomBarBackgroundColor()
                    changeAppbarTitle("Faculty")

                }
                R.id.galleryFragment -> {
                    navController.navigate(R.id.toGalleryFragment)
                    isInHome = false
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
                if (isInHome) {
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

        if (isInHome) {
            finish()
        } else {
            bottomNavBar.selectTabAt(0)
            changeBottomBarBackgroundColor()
            navController.navigate(R.id.toHomeFragment)
        }
    }


}