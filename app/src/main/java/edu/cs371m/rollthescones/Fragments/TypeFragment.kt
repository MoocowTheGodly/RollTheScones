package edu.cs371m.rollthescones.Fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.cs371m.rollthescones.MainViewModel
import android.widget.Button
import edu.cs371m.rollthescones.R

import android.app.AlertDialog;
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction

class TypeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    companion object {
        fun newInstance() = TypeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this.activity!!).get(MainViewModel::class.java)

        val root = inflater.inflate(R.layout.type_fragment, container, false)
        val button = root.findViewById<Button>(R.id.buttonSelectType)
        val types = arrayOf("American", "Chinese", "Indian", "Italian", "Mexican", "Pick for me")
        var boolTypes = booleanArrayOf(false, false, false, false, false, false)
        button.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            var boolTypesCopy = boolTypes.copyOf()
            builder.setSingleChoiceItems(types, -1) {dialog, which ->
                boolTypes[which] = true
                //val currentItem = types[which]

            }
            builder.setPositiveButton("OK"){dialog, which ->
                //create restaurantType string based on user choice
                val numberTypes = types.size
                var restaurantTypes = ""
                for (i in 0 until numberTypes) {
                    //if rest type was selected
                    if (boolTypes[i]) {
                        //if select for me was selected
                        if (i == numberTypes-1) {
                            restaurantTypes = types[(0 until numberTypes-1).random()]
                            break
                        }
                        else {
                            restaurantTypes = types[i]
                        }
                    }
                }
                viewModel.restaurantTypeEasier.value = restaurantTypes
                viewModel.restaurantType.value = restaurantTypes
                Toast.makeText(activity, restaurantTypes + " selected", Toast.LENGTH_LONG).show()
                val radiusFragment = RadiusFragment.newInstance()
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.addToBackStack(null)
                    ?.replace(R.id.main_frame, radiusFragment)
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    ?.commit()
            }
            builder.setNeutralButton("Cancel") {dialog, which ->
                boolTypes = boolTypesCopy
            }
            builder.show()
        }

        return root
    }


}
