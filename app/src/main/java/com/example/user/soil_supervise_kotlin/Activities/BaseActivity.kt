package com.example.user.soil_supervise_kotlin.Activities

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.user.soil_supervise_kotlin.Interfaces.FragmentMenuItemClickListener
import com.example.user.soil_supervise_kotlin.R

abstract class BaseActivity : AppCompatActivity()
{
    val TAG: String = this.javaClass.simpleName
    private var _contentView: LinearLayout? = null
    private var _mToolbar: Toolbar? = null
    private var _toolBarTitle: TextView? = null
    //private var amRightTv : TextView? = null
    private var _toolbarImage: ImageView? = null
    //
    private var _onMenuItemClickListener: ((MenuItem) -> Boolean)? = null
    private var _onNavigationClickListener: View.OnClickListener? = null
    private val _invalidMenu = -1
    private var _menuRes = _invalidMenu

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        Log.e("baseActivity", "onCreate")
    }

    override fun setContentView(@LayoutRes layoutResID: Int)
    {
        if (_contentView == null && R.layout.activity_base == layoutResID)
        {
            super.setContentView(R.layout.activity_base)
            _contentView = findViewById<LinearLayout>(R.id.layout_center) as LinearLayout
            _toolBarTitle = findViewById<TextView>(R.id.toolbar_title) as TextView
            //amRightTv = findViewById<TextView>(R.id.am_right_tv) as TextView
            _toolbarImage = findViewById<ImageView>(R.id._toolbarImage) as ImageView
            _contentView!!.removeAllViews()
        }
        else if (layoutResID != R.layout.activity_base)
        {
            val nullViewGroup: ViewGroup? = null
            val addView = LayoutInflater.from(this).inflate(layoutResID, nullViewGroup)
            _contentView!!.addView(addView, ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT))

            //do not change sequence
            FindView()
            InitView()

            //do not change sequence
            BeforeSetActionBar()
            InitActionBar()
            AfterSetActionBar()
        }
        Log.e("baseActivity", "setContentView")
    }

    override fun onAttachFragment(fragment: Fragment?)
    {
        super.onAttachFragment(fragment)
        Log.e("baseActivity", "onAttachFragment")
    }

    override fun onStart()
    {
        super.onStart()
        Log.e("baseActivity", "onStart")
    }

    override fun onResume()
    {
        super.onResume()
        Log.e("baseActivity", "onResume")
    }

    override fun onPause()
    {
        super.onPause()
        Log.e("baseActivity", "onPause")
    }

    override fun onStop()
    {
        super.onStop()
        Log.e("baseActivity", "onStop")
    }

    override fun onRestart()
    {
        super.onRestart()
        Log.e("MainActivity", "onRestart")
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean
//    {
//        if (_menuRes != _invalidMenu)
//        {
//            menuInflater.inflate(_menuRes, menu)
//        }
//
//        return true
//    }

    override fun onBackPressed()
    {
        Log.e("baseActivity", "onBackPressed")
    }

    override fun onDestroy()
    {
        super.onDestroy()
        Log.e("baseActivity", "onDestroy")
    }

    fun SetMenuID(@MenuRes menuRes: Int)
    {
        this._menuRes = menuRes
    }

    fun SetMenuClickListener(onMenuItemClickListener: (MenuItem) -> Boolean)
    {
//        this._onMenuItemClickListener =
        _mToolbar!!.setOnMenuItemClickListener(onMenuItemClickListener)
    }

    fun SetMenuClickListener(onMenuItemClickListener: FragmentMenuItemClickListener)
    {
        _mToolbar!!.setOnMenuItemClickListener(onMenuItemClickListener.FragmentMenuItemClickListenerObject())
    }

    fun SetMenuInstance(@MenuRes menuRes: Int, onMenuItemClickListener: (MenuItem) -> Boolean)
    {
        _mToolbar!!.inflateMenu(menuRes)
        _mToolbar!!.setOnMenuItemClickListener(onMenuItemClickListener)
    }

    fun SetMenu(@MenuRes menuRes: Int, onMenuItemClickListener: (MenuItem) -> Boolean)
    {
        this._menuRes = menuRes
        this._onMenuItemClickListener = onMenuItemClickListener
    }

    fun SetLeftImg(@DrawableRes imgId: Int)
    {
        _mToolbar!!.setNavigationIcon(imgId)
    }

    fun SetActivityTitle(text: String)
    {
        _toolBarTitle!!.text = text
    }

    fun SetActivityTitle(@StringRes textId: Int)
    {
        _toolBarTitle!!.setText(textId)
    }

    fun SetRightText(text: String)
    {
        //amRightTv!!.text = text
    }

    fun SetRightTextAndClickListener(text: String, listener: View.OnClickListener)
    {
//        amRightTv!!.text = text
//        amRightTv!!.setOnClickListener(listener)
    }

    fun SetRightImageAndClickListener(@DrawableRes imgId: Int, listener: View.OnClickListener)
    {
        _toolbarImage!!.setImageDrawable(ContextCompat.getDrawable(this, imgId))
        _toolbarImage!!.setOnClickListener(listener)
    }

    fun SetRightImg(@DrawableRes imgId: Int)
    {
//        amRightTv!!.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, imgId, 0)
    }

    fun SetOnNavigationClickListener(onNavigationClickListener: View.OnClickListener)
    {
        this._onNavigationClickListener = onNavigationClickListener
    }

    private fun BeforeSetActionBar()
    {
        _mToolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        _mToolbar!!.setNavigationIcon(R.mipmap.btn_back)
        _mToolbar!!.setTitleTextColor(Color.WHITE)
        _mToolbar!!.title = ""
        _mToolbar!!.isEnabled = true
    }

    private fun AfterSetActionBar()
    {
        setSupportActionBar(_mToolbar)
        if (supportActionBar != null)
        {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        _mToolbar!!.setNavigationOnClickListener(_onNavigationClickListener)
//        _mToolbar!!.setOnMenuItemClickListener(_onMenuItemClickListener)
    }

    abstract fun InitActionBar()

    abstract fun FindView()

    abstract fun InitView()
}