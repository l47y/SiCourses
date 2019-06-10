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
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem




class MapFragment : Fragment(), OnMapReadyCallback {

    var coords  = hashMapOf(
        Pair("Madrid", listOf(40.416775, -3.703790)),
        Pair("Valencia", listOf(39.4697500, -0.3773900)),
        Pair("Bilbao", listOf(43.262985, -2.935013)),
        Pair("Algeciras", listOf(36.13326, -5.45051))

    )


    lateinit var mybundle: Bundle
    override fun onMapReady(p0: GoogleMap?) {

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = arguments
        if (args != null) {
            mybundle = args
        }
        val courses = mybundle.getParcelableArrayList<CourseDataClass>("courses")
        println("HAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        println(courses)

        val Inflator = inflater.inflate(R.layout.fragment_map, container, false)

        val map = Inflator.mapView

        //activity?.setContentView(R.layout.activity_main)

        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        map.setMultiTouchControls(true)
        val mapController = map.getController()
        mapController.setZoom(7)
        val startPoint = GeoPoint(40.416775, -3.703790)
        //val startPoint = GeoPoint(48.8583, 2.2944)
        mapController.setCenter(startPoint)

        //your items
        val items = ArrayList<OverlayItem>()
        items.add(OverlayItem("Title", "Description", startPoint)) // Lat/Lon decimal degrees

//        for (el in coords) {
//            val startMarker = Marker(map)
//            val point = GeoPoint(el.value[0], el.value[1])
//            startMarker.setPosition(point)
//            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//            map.overlays.add(startMarker)
//        }

        for (el in courses) {
            val loc = coords[el.lugar]
            if (loc != null) {
                val startMarker = Marker(map)
                val point = GeoPoint(loc[0], loc[1])
                startMarker.setPosition(point)
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                //startMarker.setTextIcon(el.nombre)
                map.overlays.add(startMarker)
            }
        }


//        val startMarker = Marker(map)
//        startMarker.setPosition(startPoint)
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//        map.overlays.add(startMarker)

        return Inflator

    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(activity))
    }
}
