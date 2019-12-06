package edu.cs371m.rollthescones.Fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.cs371m.rollthescones.MainViewModel
import edu.cs371m.rollthescones.MapActivity
import androidx.lifecycle.Observer


import edu.cs371m.rollthescones.R

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var geocoder: Geocoder
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastKnownLocation: Location

    private val LOCATION_REQUEST_CODE = 101
    var destination = ""
    //TODO: GIVE THIS ACTUAL NAME
    var sizeOfStuff = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        geocoder = Geocoder(this.activity!!)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.activity!!)
        viewModel = ViewModelProviders.of(this.activity!!).get(MainViewModel::class.java)


        //get location perms from user
        val permission = ContextCompat.checkSelfPermission(this.activity!!,
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

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var buttonMyLoc = view.findViewById<Button>(R.id.buttonMyLoc)
        var buttonCustLoc = view.findViewById<Button>(R.id.buttonCustLoc)
        var buttonGo = view.findViewById<ImageButton>(R.id.buttonGo)

        buttonMyLoc.setOnClickListener {
            Toast.makeText(activity, "Location set to current location", Toast.LENGTH_LONG).show()
            viewModel.latLong.value = lastKnownLocation.latitude.toString() + "," + lastKnownLocation.longitude.toString()
        }
        //start map activity to let user select location
        buttonCustLoc.setOnClickListener {
            val mapIntent = Intent(this.activity, MapActivity::class.java)
            mapIntent.putExtra("lat", lastKnownLocation.latitude)
            mapIntent.putExtra("long", lastKnownLocation.longitude)
            startActivityForResult(mapIntent, 1)
        }

        buttonGo.setOnClickListener {
            val ratingFragment = TypeFragment.newInstance()
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.addToBackStack(null)
                ?.replace(R.id.main_frame, ratingFragment)
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.commit()
        }
    }

    private fun requestPermission(permissionType: String,
                                  requestCode: Int) {
        ActivityCompat.requestPermissions(this.activity!!,
            arrayOf(permissionType), requestCode
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val lat = data?.getDoubleExtra("lat", 0.0).toString()
        val long = data?.getDoubleExtra("long", 0.0).toString()
        viewModel.latLong.value = lat + "," + long
    }
}
