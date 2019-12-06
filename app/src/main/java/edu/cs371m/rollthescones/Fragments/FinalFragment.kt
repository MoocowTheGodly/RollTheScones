package edu.cs371m.rollthescones.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import edu.cs371m.rollthescones.MainViewModel

import edu.cs371m.rollthescones.R
import androidx.lifecycle.Observer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast



class FinalFragment : Fragment() {

    companion object {
        fun newInstance() = FinalFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this.activity!!).get(MainViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_final, container, false)
        val textViewAddress = root.findViewById<TextView>(R.id.textViewAddress)
        var numPlaces = 0
        var placesSeen = mutableListOf<Int>()
        viewModel.observePlaces().observe(this, Observer {
            numPlaces = it.size
            if (numPlaces == 0) {
                if (viewModel.easierRestUsed.value!!) {
                    textViewAddress.text = "no results. maybe increase the radius or stop looking in the middle of the desert?"
                }
                else {
                    viewModel.easierRestUsed.value = true
                    viewModel.netFetchRestaurantEasier()
                }
            }
            else {
                var rand = (0 until it.size).random()
                while (it[rand].rating < viewModel.minRating.value!!) {
                    if (!placesSeen.contains(rand)) {
                        placesSeen.add(rand)
                    }
                    if (placesSeen.size >= it.size) {
                        viewModel.netFetchRestaurantEasier()
                        break
                    }
                    rand = (0 until it.size).random()
                }
                textViewAddress.text = it[rand].address
            }
        })
        viewModel.netFetchRestaurant()

        return root
    }


}
