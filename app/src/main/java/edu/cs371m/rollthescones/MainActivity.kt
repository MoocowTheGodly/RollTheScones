package edu.cs371m.rollthescones

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction


import edu.cs371m.rollthescones.Fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var homeFrag: HomeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeFrag = HomeFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_frame, homeFrag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}
