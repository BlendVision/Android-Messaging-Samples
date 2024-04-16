package com.example.messagesdksampleapp.activity

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.messagesdksampleapp.R
import com.example.messagesdksampleapp.ChatroomUser
import com.example.messagesdksampleapp.fragment.StartChatroomFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.textfield.TextInputEditText

private const val ACTIVITY: String = "MainActivity"
class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    var tabChatroomUser1: ChatroomUser = ChatroomUser("Vivian_1")
    var tabChatroomUser2: ChatroomUser = ChatroomUser("Vivian_2")
    var tabChatroomUser3: ChatroomUser = ChatroomUser("Vivian_3")
    private var firstUserChatroomFragment: Fragment? = null
    private var secondChatroomFragment: Fragment? = null
    private var thirdChatroomFragment: Fragment? = null
    private var bottomNavigationView: BottomNavigationView? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView?.setOnItemSelectedListener(this)
        bottomNavigationView?.selectedItemId = R.id.bottom_navigation_button_1
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        Log.d(ACTIVITY, item.itemId.toString())
        when(item.itemId) {
            R.id.bottom_navigation_button_1 -> {
                if (firstUserChatroomFragment == null && !tabChatroomUser1.isConnected) {
                    firstUserChatroomFragment = StartChatroomFragment()
                }
                secondChatroomFragment?.let { fragmentTransaction.hide(it) }
                thirdChatroomFragment?.let { fragmentTransaction.hide(it) }
                if (firstUserChatroomFragment?.isAdded == true) {
                    firstUserChatroomFragment?.let { fragmentTransaction.show(it) }
                }
                else {
                    firstUserChatroomFragment?.let { fragmentTransaction.add(R.id.layout_fragment, it) }
                }
                fragmentTransaction.commit()
                Log.d(ACTIVITY, supportFragmentManager.fragments.toString())
                return true
            }
            R.id.bottom_navigation_button_2 -> {
                if (secondChatroomFragment == null && !tabChatroomUser2.isConnected) {
                    secondChatroomFragment = StartChatroomFragment()
                }
                firstUserChatroomFragment?.let { fragmentTransaction.hide(it) }
                thirdChatroomFragment?.let { fragmentTransaction.hide(it) }
                if (secondChatroomFragment?.isAdded == true) {
                    secondChatroomFragment?.let { fragmentTransaction.show(it) }
                }
                else {
                    secondChatroomFragment?.let { fragmentTransaction.add(R.id.layout_fragment, it) }
                }
                fragmentTransaction.commit()
                Log.d(ACTIVITY, supportFragmentManager.fragments.toString())
                return true
            }
            R.id.bottom_navigation_button_3 -> {
                if (thirdChatroomFragment == null && !tabChatroomUser3.isConnected) {
                    thirdChatroomFragment = StartChatroomFragment()
                }
                firstUserChatroomFragment?.let { fragmentTransaction.hide(it) }
                secondChatroomFragment?.let { fragmentTransaction.hide(it) }
                if (thirdChatroomFragment?.isAdded == true) {
                    thirdChatroomFragment?.let { fragmentTransaction.show(it) }
                }
                else {
                    thirdChatroomFragment?.let { fragmentTransaction.add(R.id.layout_fragment, it) }
                }
                fragmentTransaction.commit()
                Log.d(ACTIVITY, supportFragmentManager.fragments.toString())
                return true
            }
        }
        return false
    }

    fun replaceFragments(fromFragment: Fragment, toFragment: Fragment, addToBack: Boolean = false) {
        when(bottomNavigationSelectedItemId()) {
            bottomNavigationMenuItemId(0) -> firstUserChatroomFragment = toFragment
            bottomNavigationMenuItemId(1) -> secondChatroomFragment = toFragment
            bottomNavigationMenuItemId(2) -> thirdChatroomFragment = toFragment
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.layout_fragment, toFragment)
        if (addToBack) {
            fragmentTransaction.hide(fromFragment)
        }
        else {
            fragmentTransaction.remove(fromFragment)
        }
        fragmentTransaction.commit()
    }

    fun backToChatroom(infoFragment: Fragment, chatroomFragment: Fragment) {
        when(bottomNavigationSelectedItemId()) {
            bottomNavigationMenuItemId(0) -> firstUserChatroomFragment = chatroomFragment
            bottomNavigationMenuItemId(1) -> secondChatroomFragment = chatroomFragment
            bottomNavigationMenuItemId(2) -> thirdChatroomFragment = chatroomFragment
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(infoFragment)
        fragmentTransaction.show(chatroomFragment)
        fragmentTransaction.commit()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            currentFocus?.let {
                if (it is TextInputEditText) {
                    val outRect = Rect()
                    it.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0)
                        it.clearFocus()
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun bottomNavigationSelectedItemId(): Int? {
        return bottomNavigationView?.selectedItemId
    }

    fun bottomNavigationMenuItemId(index: Int = 0): Int? {
        return bottomNavigationView?.menu?.getItem(index)?.itemId
    }
}