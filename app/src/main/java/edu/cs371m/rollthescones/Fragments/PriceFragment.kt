package edu.cs371m.rollthescones.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import edu.cs371m.rollthescones.MainViewModel
import androidx.lifecycle.Observer
import android.widget.ImageButton

import edu.cs371m.rollthescones.R
import org.florescu.android.rangeseekbar.RangeSeekBar


class PriceFragment : Fragment() {

    companion object {
        fun newInstance() = PriceFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        viewModel = ViewModelProviders.of(this.activity!!).get(MainViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_price, container, false)

        val textViewMin = root.findViewById<TextView>(R.id.textViewMin)
        val textViewMax = root.findViewById<TextView>(R.id.textViewMax)
        val button = root.findViewById<ImageButton>(R.id.buttonGetMeOut)
        button.setOnClickListener {
            val finalFragment = FinalFragment.newInstance()
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.addToBackStack(null)
                ?.replace(R.id.main_frame, finalFragment)
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.commit()

        }
        var rangeSeekBar = root.findViewById<RangeSeekBar<Int>>(R.id.rangeSeekBar)
        rangeSeekBar.setRangeValues(1, 4)
        rangeSeekBar.setOnRangeSeekBarChangeListener { rangeSeekBar, minProg, maxProg ->
            viewModel.minPrice.value = minProg
            viewModel.maxPrice.value = maxProg
        }

        viewModel.observeMinPrice().observe(this, Observer {
            val numDollar = rangeSeekBar.selectedMinValue
            var dollarString = ""
            for (i in 0 until numDollar) {
                dollarString += "$"
            }
            textViewMin.text = dollarString
        })
        viewModel.observeMaxPrice().observe(this, Observer {
            val numDollar = rangeSeekBar.selectedMaxValue
            var dollarString = ""
            for (i in 0 until numDollar) {
                dollarString += "$"
            }
            textViewMax.text = dollarString
        })

        return root
    }


}
