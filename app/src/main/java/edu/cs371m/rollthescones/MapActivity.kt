package edu.cs371m.rollthescones

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*
import android.content.Intent


import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*


class MapActivity
    : AppCompatActivity(),
    OnMapReadyCallback
{
    private lateinit var map: GoogleMap
    private lateinit var geocoder: Geocoder
    private val LOCATION_REQUEST_CODE = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_coordinator)
        setSupportActionBar(findViewById(R.id.toolbar))

        checkGooglePlayServices()

        var layout = findViewById<ConstraintLayout>(R.id.constLayoutWithStuff)
        layout.setBackgroundColor(Color.WHITE)
        var mapView = supportFragmentManager.findFragmentById(R.id.mapFrag) as SupportMapFragment
        mapView.getMapAsync(this)
        geocoder = Geocoder(this)

        var results = listOf<Address>()

        goBut.setOnClickListener {
            var address = mapET.text.toString()
            if (!address.isEmpty()) {
                results = geocoder.getFromLocationName(address, 1)
                if (results.isEmpty()) {
                    Toast.makeText(this, "No results", Toast.LENGTH_LONG).show()
                }
                else {
                    val long = results.get(0).longitude
                    val lat = results.get(0).latitude
                    val location = LatLng(lat, long)
                    val cameraUpdate = CameraUpdateFactory.newLatLng(location)
                    map.moveCamera(cameraUpdate)
                    hideKeyboard()
                }
            }
        }
        cancelBut.isLongClickable = true
        cancelBut.setOnClickListener {
            mapET.setText("")
        }

        selectLocBut.setOnClickListener {
            Toast.makeText(this, "Selected location", Toast.LENGTH_LONG).show()
            val lat = map.cameraPosition.target.latitude
            val long = map.cameraPosition.target.longitude
            val backIntent = Intent()
                .putExtra("lat", lat)
                .putExtra("long", long)
            setResult(RESULT_OK, backIntent)
            finish()
        }

        // This code is correct, but it assumes an EditText in your layout
        // called mapET and a go button called goBut
        mapET.setOnEditorActionListener { /*v*/_, actionId, event ->
            // If user has pressed enter, or if they hit the soft keyboard "send" button
            // (which sends DONE because of the XML)
            if (((event.action == KeyEvent.ACTION_DOWN)
                        &&
                        (event.keyCode == KeyEvent.KEYCODE_ENTER))
                || (actionId == EditorInfo.IME_ACTION_DONE)) {
                hideKeyboard()
                goBut.callOnClick()
            }
            false
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Get permission from user for location and show self-dot
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
            Log.d("help", "mylocation enabled")
        } else {
            requestPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_REQUEST_CODE)
        }

        // Start the map at the Harry Ransom center
        var results = listOf<Address>()

        val lat = intent.getDoubleExtra("lat", 0.0)
        val long = intent.getDoubleExtra("long", 0.0)
        val location = LatLng(lat, long)
        val cameraUpdate = CameraUpdateFactory.newLatLng(location)
        val cameraUpdateZoom = CameraUpdateFactory.zoomTo(15f)
        map.moveCamera(cameraUpdate)
        map.moveCamera(cameraUpdateZoom)
        hideKeyboard()
    }


    // An Android nightmare (HIDES KEYBOARD)
    // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0);
    }

    private fun checkGooglePlayServices() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode =
            googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 257).show()
            } else {
                Log.i(javaClass.simpleName,
                    "This device must install Google Play Services.")
                finish()
            }
        }
    }

    private fun requestPermission(permissionType: String,
                                  requestCode: Int) {
        ActivityCompat.requestPermissions(this,
            arrayOf(permissionType), requestCode
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] !=
                    PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                        "Unable to show location - permission required",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
