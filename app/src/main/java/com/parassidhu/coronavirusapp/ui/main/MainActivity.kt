package com.parassidhu.coronavirusapp.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.parassidhu.coronavirusapp.R
import com.parassidhu.coronavirusapp.base.BaseActivity
import com.parassidhu.coronavirusapp.ui.india.IndiaFragment
import com.parassidhu.coronavirusapp.ui.overview.OverviewFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private var activeFragment = Fragment()

    private val overviewFragment by lazy { OverviewFragment() }
    private val indiaFragment by lazy { IndiaFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        setupBottomBar()
        showInitialFragment()
    }

    private fun showInitialFragment() {
        activeFragment = overviewFragment

        supportFragmentManager.commit {
            add(R.id.homeContainer, overviewFragment, OVERVIEW)
        }
    }

    private fun setupBottomBar() {
        bottomNavBar.setOnNavigationItemSelectedListener { item: MenuItem ->
            when(item.itemId) {
                R.id.action_overview -> handleOverviewAction()
                R.id.action_india -> handleIndiaAction()
            }

            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun handleOverviewAction() {
        when (activeFragment) {
            overviewFragment -> {
                return
            }
            indiaFragment -> {
                val frag = supportFragmentManager.findFragmentByTag(OVERVIEW)

                if (frag == null) {
                    supportFragmentManager.commit {
                        hide(activeFragment)
                        add(R.id.homeContainer, overviewFragment, OVERVIEW)
                    }
                } else {
                    supportFragmentManager.commit {
                        hide(activeFragment)
                        show(frag)
                    }
                }
            }
        }

        activeFragment = overviewFragment
    }

    private fun handleIndiaAction() {
        when (activeFragment) {
            overviewFragment -> {
                val frag = supportFragmentManager.findFragmentByTag(INDIA)

                if (frag == null) {
                    supportFragmentManager.commit {
                        hide(activeFragment)
                        add(R.id.homeContainer, indiaFragment, INDIA)
                    }
                } else {
                    supportFragmentManager.commit {
                        hide(activeFragment)
                        show(frag)
                    }
                }
            }
            indiaFragment -> {
                return
            }
        }

        activeFragment = indiaFragment
    }

    companion object {
        private const val OVERVIEW = "overview"
        private const val INDIA = "india"
    }
}