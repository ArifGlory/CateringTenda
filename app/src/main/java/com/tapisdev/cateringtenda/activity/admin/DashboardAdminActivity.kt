package com.tapisdev.cateringtenda.activity.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.tapisdev.cateringtenda.MainActivity
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.fragment.AdminCateringFragment
import com.tapisdev.cateringtenda.fragment.AdminPesananFragment
import com.tapisdev.cateringtenda.fragment.AdminTendaFragment
import com.tapisdev.cateringtenda.model.UserModel
import com.tapisdev.cateringtenda.model.UserPreference
import kotlinx.android.synthetic.main.activity_dashboard_admin.*
import kotlinx.android.synthetic.main.nav_header_dashboard_admin.*

class DashboardAdminActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mToggle : ActionBarDrawerToggle
    private lateinit var tvProfileName : TextView
    private lateinit var ivProfile : RoundedImageView
    private lateinit var rlNavHeader : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_admin)
        mUserPref = UserPreference(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        // memunculkan tombol burger menu
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // untuk toggle open dan close navigation
        mToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(mToggle)
        mToggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val headerView = nav_view.getHeaderView(0)
        tvProfileName = headerView.findViewById(R.id.tvProfileName)
        ivProfile = headerView.findViewById(R.id.ivProfile)
        rlNavHeader = headerView.findViewById(R.id.rlNavHeader)
        rlNavHeader.setOnClickListener {
            val i = Intent(applicationContext,EditProfilAdminActivity::class.java)
            startActivity(i)
        }

        val fragment = AdminCateringFragment.newInstance()
        addFragment(fragment)


        updateUI()
    }

    fun updateUI(){
        tvProfileName.setText(mUserPref.getName())
        if (mUserPref.getFoto().equals("")){
            ivProfile.setImageResource(R.drawable.ic_placeholder)
        }else{
            Glide.with(this)
                .load(mUserPref.getFoto())
                .into(ivProfile)
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return mToggle.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_catering -> {
                val fragment = AdminCateringFragment.newInstance()
                addFragment(fragment)
            }
            R.id.nav_tenda -> {
                val fragment = AdminTendaFragment.newInstance()
                addFragment(fragment)
            }
            R.id.nav_pesanan -> {
                val fragment = AdminPesananFragment.newInstance()
                addFragment(fragment)
            }
            R.id.nav_logout -> {
                auth.signOut()
                clearSession()
                val i = Intent(applicationContext,MainActivity::class.java)
                startActivity(i)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    fun clearSession(){
        mUserPref.saveName("")
        mUserPref.saveEmail("")
        mUserPref.saveFoto("")
        mUserPref.saveJenisUser("none")
        mUserPref.savePhone("")
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

}
