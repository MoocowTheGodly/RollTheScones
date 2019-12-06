package edu.cs371m.rollthescones.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProviders
import edu.cs371m.rollthescones.MainViewModel
import androidx.lifecycle.Observer
import android.widget.TextView
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import android.widget.ImageButton


import edu.cs371m.rollthescones.R

class RadiusFragment : Fragment() {
    companion object {
        fun newInstance() = RadiusFragment()
    }
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this.activity!!).get(MainViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_radius, container, false)
        val textView = root.findViewById<TextView>(R.id.textViewMiles)
        val button = root.findViewById<ImageButton>(R.id.buttonNext)
        val seekBar = root.findViewById<SeekBar>(R.id.seekBar)
        seekBar.max = 27

        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                //convert from miles to meters
                viewModel.radius.value = (progress + 3) * 1609
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
                startAnimation(textView)
            }
        })
        viewModel.observeRadius().observe(this, Observer {
            textView.text = (seekBar.progress+3).toString() + " miles"
        })

        val ratingFragment = RatingFragment.newInstance()
        button.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.addToBackStack(null)
                ?.replace(R.id.main_frame, ratingFragment)
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.commit()
        }
        return root
    }

    fun startAnimation(textView: TextView) {
        val animation = AnimationUtils.loadAnimation(activity, R.anim. anim)
        textView.startAnimation(animation)
    }
}
