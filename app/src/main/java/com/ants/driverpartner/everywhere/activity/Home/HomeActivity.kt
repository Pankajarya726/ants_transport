package com.ants.driverpartner.everywhere.activity.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.ui.AppBarConfiguration
import com.an.customfontview.CustomTextView
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.webViewActivity.WebViewActivity
import com.ants.driverpartner.everywhere.databinding.ActivityHomeBinding
import com.ants.driverpartner.everywhere.fragment.CurrentBookingFragment
import com.ants.driverpartner.everywhere.fragment.history.HistoryFragment
import com.ants.driverpartner.everywhere.fragment.newBooking.NewBooking
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.ScheduleFragment
import com.ants.driverpartner.everywhere.fragment.account.AccountFragment
import com.ants.driverpartner.everywhere.fragment.contactUs.ContactFragmant
import com.ants.driverpartner.everywhere.utils.SnackbarUtils.snackBarBottom

class HomeActivity : AppCompatActivity(), Homeview {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var drawer: DrawerLayout? = null
    private var fragmentManager: FragmentManager? = null
    lateinit var binding: ActivityHomeBinding
    private var imageView: ImageView? = null
    private var toolbar: Toolbar? = null
    private val currentFragment = -1
    private var backPressed = false
    private var previous_fragment = "fragment"
    private var isActivityRefreshed = false
    private var navigationFragment: NavigationFragment? = null
    private var newBookingFragment: NewBooking? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)

        fragmentManager = supportFragmentManager

        init()
        imageView = findViewById(R.id.toolbar_icon_navigation)

        imageView!!.setOnClickListener(View.OnClickListener {
            openCloseDrawer()
        })


    }

    fun init() {
        fragmentManager = supportFragmentManager
        toolbar = findViewById(R.id.layout_toolbar)
        setToolbarTitle("New Booking")
        toolbar!!.navigationIcon = null
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawerLayout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, null,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(0)
        populateNavigationView("0")
    }


    internal fun replaceFragment(position: Int) {

        if (currentFragment == position && position != 0)
            return

        var fragment: Fragment? = null
        val fragmentPosition = position.toString() + ""
        var title: String? = null

        when (position) {
            0 -> {
                newBookingFragment = NewBooking.newInstance()
                fragment = newBookingFragment
                title = "New Booking"
            }

            1 -> {

                fragment = CurrentBookingFragment(this)
                title = "Current Booking"
                isActivityRefreshed = true
            }

            2 -> {
                fragment =
                    ScheduleFragment(this)
                title = "Schedule Booking"
                isActivityRefreshed = true
            }

            3 -> {
                fragment = HistoryFragment(this)
                title = "Booking History"
                isActivityRefreshed = true
            }

            4 -> {
                fragment = AccountFragment(this)
                title = "Account Info"
                isActivityRefreshed = true
            }
//
            5 -> {
                fragment = ContactFragmant(this)
                title = "Contact ants"
                isActivityRefreshed = true
            }

            6 -> {

                fragment = null
                title = "FAQ"
                openCloseDrawer()

                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra(Constant.WEB_VIEW_TITLE, title)
                intent.putExtra(Constant.WEB_URL, "www.google.co.in")
                startActivity(intent)


            }
            7 -> {

                fragment = null
                title = "Term and Condition"
                openCloseDrawer()

                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra(Constant.WEB_VIEW_TITLE, title)
                intent.putExtra(Constant.WEB_URL, "www.google.co.in")
                startActivity(intent)


            }
            8 -> {

                fragment = null
                title = "Privacy Policy"
                openCloseDrawer()

                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra(Constant.WEB_VIEW_TITLE, title)
                intent.putExtra(Constant.WEB_URL, "www.google.co.in")
                startActivity(intent)


            }

            9 -> {
                /**
                 * Logout
                 */
                fragment = null
                openCloseDrawer()


                AlertDialog.Builder(this)
                    .setIcon(R.drawable.ants_icon)
                    .setCancelable(true)
                    .setNegativeButton(getString(R.string.no), null)
                    .setTitle(R.string.app_name)
                    .show()
            }
        }// onResume();
        //onLogout();

        if (fragment != null && fragmentPosition != null) {
            replaceFragment(fragment, fragmentPosition, title!!)
            //mvpPresenter.updateDriverStatus("0");
        } else {
            closeNavigationDrawer()
        }

    }

    fun replaceFragment(fragment: Fragment, fragmentPosition: String, title: String) {
        setToolbarTitle(title)
        closeNavigationDrawer()
        fragmentPullPush(fragmentPosition, fragment)
    }


    fun setToolbarTitle(title: String) {
        var textView = binding.layoutToolbar.findViewById(R.id.toolbar_title) as CustomTextView
        textView.text = title

    }

    fun populateNavigationView(isSelectorChange: String) {
        val bundle = Bundle()
        bundle.putString("isSelectorChange", isSelectorChange)
        navigationFragment = NavigationFragment()
        navigationFragment!!.arguments = bundle

        fragmentManager!!
            .beginTransaction()
            .replace(R.id.flContainerNavigationMenu, navigationFragment!!, "Navigation").commit()
    }

    private fun fragmentPullPush(fragmentPosition: String, fragment: Fragment) {
        backPressed = false
        var isFragmentFound = true

        Log.e(javaClass.simpleName, "fragmentPosition==> $fragmentPosition")
        Log.e(javaClass.simpleName, "flag count==> " + fragmentManager!!.getBackStackEntryCount())

        val transaction = fragmentManager!!.beginTransaction()

        for (entry in 0 until fragmentManager!!.getBackStackEntryCount()) {
            if (fragmentManager!!.getBackStackEntryAt(entry).name.equals(fragmentPosition)) {

                //Log.view(TAG,"Fragment==> "+fragmentManager.getBackStackEntryAt(entry).getName());

                fragmentManager!!.popBackStack(
                    fragmentPosition,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                isFragmentFound = false

                try {
                    navigationFragment!!.changeRowSelector(
                        Integer.parseInt(fragmentManager!!.getBackStackEntryAt(entry).name.toString())
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                break
            }
        }
        if (isFragmentFound && !fragmentPosition.equals("0", ignoreCase = true))
            transaction.add(
                R.id.frm_container,
                fragment
            ).addToBackStack(previous_fragment).commit()

        if (fragmentPosition.equals("0", ignoreCase = true)) {
            for (i in 0 until fragmentManager!!.getBackStackEntryCount()) {
                fragmentManager!!.popBackStack()
            }
            transaction.replace(R.id.frm_container, fragment).commit()

            if (isActivityRefreshed) {
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }


        }

        previous_fragment = fragmentPosition


    }

    fun closeNavigationDrawer() {
        if (drawer!!.isDrawerOpen(Gravity.LEFT)) {
            drawer!!.closeDrawer(Gravity.LEFT)
        }

    }

    private fun openCloseDrawer() {
        if (drawer!!.isDrawerOpen(Gravity.LEFT))
            drawer!!.closeDrawer(Gravity.LEFT)
        else
            drawer!!.openDrawer(Gravity.LEFT)

    }

    override fun setHeaderTitle(s: String) {
        setToolbarTitle(s)
    }

    override fun onBackPressed() {

        if (drawer!!.isDrawerOpen(Gravity.LEFT)) {
            drawer!!.closeDrawer(Gravity.LEFT)
        } else {

            val i = supportFragmentManager.backStackEntryCount

            if (backPressed) {
                finish()
                System.exit(0)

            } else if (i == 0) {

                backPressed = true
                snackBarBottom(findViewById(R.id.drawerLayout), "Press again to Exit.")


            } else {

                replaceFragment(
                    Integer.parseInt(
                        fragmentManager!!.getBackStackEntryAt(
                            fragmentManager!!.backStackEntryCount - 1
                        ).name!!
                    )
                )

            }
        }
    }

}
