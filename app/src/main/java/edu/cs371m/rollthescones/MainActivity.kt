package edu.cs371m.rollthescones

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible



import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var geocoder: Geocoder
    private val LOCATION_REQUEST_CODE = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initButtons()
        //get location perms from user
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            Log.d("help", "mylocation enabled")
        } else {
            requestPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_REQUEST_CODE)
        }

        //val fusedLocationProviderClient = LocationServices.get
        geocoder = Geocoder(this)



    }

    fun initButtons() {
        var buttonMyLoc = findViewById<Button>(R.id.buttonMyLoc)
        var buttonCustLoc = findViewById<Button>(R.id.buttonCustLoc)
        buttonMyLoc.setOnClickListener {
            buttonCustLoc.isInvisible = true
        }
        buttonCustLoc.setOnClickListener {
            buttonMyLoc.isInvisible = true
        }
    }

    private fun requestPermission(permissionType: String,
                                  requestCode: Int) {
        ActivityCompat.requestPermissions(this,
            arrayOf(permissionType), requestCode
        )
    }

}
