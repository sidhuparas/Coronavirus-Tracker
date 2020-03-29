package com.parassidhu.coronavirusapp.ui.main

import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.parassidhu.coronavirusapp.R
import com.parassidhu.coronavirusapp.base.BaseActivity
import com.parassidhu.coronavirusapp.ui.india.IndiaFragment
import com.parassidhu.coronavirusapp.ui.overview.OverviewFragment
import com.parassidhu.coronavirusapp.util.SortEnum
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
        fab.setOnClickListener { showFabOptions() }
    }

    private fun showInitialFragment() {
        activeFragment = overviewFragment

        supportFragmentManager.commit {
            add(R.id.homeContainer, overviewFragment, OVERVIEW)
        }
    }

    private fun showFabOptions() {
        val popupMenu = popupMenu {
            section {
                item {
                    label = SortEnum.ALPHABETICAL.name
                    callback = {
                        indiaFragment.onSort(SortEnum.ALPHABETICAL)
                    }
                }

                item {
                    label = SortEnum.ASCENDING.name
                    callback = {
                        indiaFragment.onSort(SortEnum.ASCENDING)
                    }
                }

                item {
                    label = SortEnum.DESCENDING.name
                    callback = {
                        indiaFragment.onSort(SortEnum.DESCENDING)
                    }
                }
            }
        }

        popupMenu.show(this, fab)
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
        fab.isVisible = false
        when (activeFragment) {
            overviewFragment -> {
                overviewFragment.scrollToTop()
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
        fab.isVisible = true
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

    override fun onBackPressed() {
        if (activeFragment == overviewFragment) {
            if (overviewFragment.handleBackPress()) {

            } else {
                finish()
            }
        } else if (activeFragment == indiaFragment) {
            handleOverviewAction()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val OVERVIEW = "overview"
        private const val INDIA = "india"
    }
}