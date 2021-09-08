package com.example.logo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.logo.R
import com.example.logo.database.ArticleDatabase
import com.example.logo.repository.NewsRepository
import com.example.logo.util.hideKeyboard
import com.example.logo.viewmodel.NewsViewModel
import com.example.logo.viewmodel.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory= NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        //controlling the fragments by bottomNavigation
        //bottomNavigation.setupWithNavController(fragmentContainerView.findNavController())

        val navHostFragment= supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController= navHostFragment.navController
        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragmentFilter){
                bottomNavigation.visibility = View.GONE
            }
            else{
                bottomNavigation.visibility = View.VISIBLE
            }
        }


    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard()
        return super.dispatchTouchEvent(ev)
    }
}