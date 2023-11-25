package com.example.techexac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.example.techexac.Adapter.PagerAdapter
import com.example.techexac.CommonUtils.Utlis.snakeBarPopUp
import com.example.techexac.View.ApplicationsFragment
import com.example.techexac.View.SettingsFragment
import com.example.techexac.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var backToExit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClickOnUi()
    }

    private fun onClickOnUi() {
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(ApplicationsFragment(), "Application")
        adapter.addFragment(SettingsFragment(), "Settings")

        binding.viewpager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewpager)

        binding.btnBack.setOnClickListener {
            if (backToExit) ActivityCompat.finishAffinity(this)
            this.backToExit = true
            snakeBarPopUp(binding.llDashBoard, "Press BACK again to exit")
            Handler().postDelayed(Runnable { backToExit = false }, 2000)
        }

    }

    override fun onBackPressed() {
        if (backToExit) ActivityCompat.finishAffinity(this)
        this.backToExit = true
        snakeBarPopUp(binding.llDashBoard, "Press BACK again to exit")
        Handler().postDelayed(Runnable { backToExit = false }, 2000)
    }
}