package com.example.nicolas.coursanizer47


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val Inflater = inflater.inflate(R.layout.fragment_home, container, false)
        val bundle = getArguments()
        if (bundle != null) {
            val string = bundle.getString("data")
        }


        return Inflater
    }


}
