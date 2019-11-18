package edu.cs371m.rollthescones

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders


import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices




import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    private lateinit var geocoder: Geocoder
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastKnownLocation: Location
    private lateinit var viewModel: MainViewModel

    private val LOCATION_REQUEST_CODE = 101
    var destination = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        geocoder = Geocoder(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val testText = findViewById<TextView>(R.id.testText)
        viewModel.observePlaces().observe(this, Observer {
            var rand = (0 until it.size).random()
            testText.text = it[rand].name
        })

        //get location perms from user
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            Log.d("help", "mylocation enabled")
            var locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener {
                if (it.isSuccessful) {
                    lastKnownLocation = it.result!!
                }
                else {
                    Log.d("Error", "Last location came back with no result")
                }
            }

        } else {
            requestPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_REQUEST_CODE)
        }


        initButtons()
    }

    fun initButtons() {
        //TODO: VERY VERY VERY WRONG USE FRAGMENT INSTEAD OF CHANGING VISIBILITY!!!!!!!
        var buttonMyLoc = findViewById<Button>(R.id.buttonMyLoc)
        var buttonCustLoc = findViewById<Button>(R.id.buttonCustLoc)
        var buttonBack = findViewById<Button>(R.id.buttonBack)
        var editText = findViewById<EditText>(R.id.addressET)
        var buttonGo = findViewById<Button>(R.id.buttonGo)
        var myLocUsed = false
        buttonMyLoc.setOnClickListener {
            buttonMyLoc.isInvisible = true
            buttonCustLoc.isInvisible = true
            buttonGo.isInvisible = false
            buttonBack.isInvisible = false
            myLocUsed = true
        }
        buttonCustLoc.setOnClickListener {
            editText.isInvisible = false
            buttonCustLoc.isInvisible = true
            buttonMyLoc.isInvisible = true
            buttonGo.isInvisible = false
            buttonBack.isInvisible = false
            myLocUsed = false
        }
        buttonGo.setOnClickListener {
            var address = ""
            if (myLocUsed) {
                address = geocoder.getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1)[0].toString()
                //var latLong = lastKnownLocation.latitude.toString() + "," + lastKnownLocation.longitude.toString()
                //placeRepository.getRestaurants("restaurant", latLong, 1)
                viewModel.latLong.value = lastKnownLocation.latitude.toString() + "," + lastKnownLocation.longitude.toString()
                viewModel.netFetchRestaurant()
            }
            else {
                address = editText.text.toString()
            }

        }
        buttonBack.setOnClickListener {
            buttonMyLoc.isInvisible = false
            buttonCustLoc.isInvisible = false
            buttonBack.isInvisible = true
            buttonGo.isInvisible = true
            editText.isInvisible = true
        }
    }

    private fun requestPermission(permissionType: String,
                                  requestCode: Int) {
        ActivityCompat.requestPermissions(this,
            arrayOf(permissionType), requestCode
        )
    }

}
