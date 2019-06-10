package com.example.nicolas.sicourses

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint


class MapFragment : Fragment(), OnMapReadyCallback {


    override fun onMapReady(p0: GoogleMap?) {

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val Inflator = inflater.inflate(R.layout.fragment_map, container, false)

        val map = Inflator.mapView

        //activity?.setContentView(R.layout.activity_main)

        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        val mapController = map.getController()
        mapController.setZoom(7)
        val startPoint = GeoPoint(40.416775, -3.703790)
        //val startPoint = GeoPoint(48.8583, 2.2944)
        mapController.setCenter(startPoint)

        return Inflator

    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(activity))
    }
}
