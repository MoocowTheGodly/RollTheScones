package edu.cs371m.rollthescones.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RatingBar
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import edu.cs371m.rollthescones.MainViewModel

import android.widget.ImageButton

import edu.cs371m.rollthescones.R

class RatingFragment : Fragment() {

    companion object {
        fun newInstance() = RatingFragment()
    }
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this.activity!!).get(MainViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_rating, container, false)
        val rateBar = root.findViewById<RatingBar>(R.id.ratingBar)
        val button = root.findViewById<ImageButton>(R.id.buttonGONEXT)
        rateBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            viewModel.minRating.value = rating.toInt()
            startAnimation(rateBar)
        }
        val priceFragment = PriceFragment.newInstance()
        button.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.addToBackStack(null)

                ?.replace(R.id.main_frame, priceFragment)
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.commit()
        }
        return root
    }
    fun startAnimation(rateView: RatingBar) {
        val animation = AnimationUtils.loadAnimation(activity, R.anim. anim)
        rateView.startAnimation(animation)
    }

}
