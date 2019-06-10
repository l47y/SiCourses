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

    val locFactor = 3

    var latAdd = hashMapOf<Int, Double>(
        Pair(2, -0.01),
        Pair(3, + 0.00),
        Pair(4, + 0.01),
        Pair(5, + 0.00),
        Pair(6, - 0.02),
        Pair(7, + 0.00),
        Pair(8, + 0.02)
    )

    var longAdd = hashMapOf<Int, Double>(
        Pair(2, -0.00),
        Pair(3, + 0.01),
        Pair(4, + 0.00),
        Pair(5, - 0.01),
        Pair(6, - 0.00),
        Pair(7, + 0.02),
        Pair(8, + 0.00)
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

        var myCities = mutableListOf<String>()
        for (el in courses) {
            val loc = coords[el.lugar]
            if (loc != null) {
                val howMany = myCities.filter {it == el.lugar}.size + 1

                var long = loc[0]
                var lat = loc[1]
                if (howMany > 1) {
                    val longVal: Double? = longAdd[howMany]
                    val latVal: Double? = latAdd[howMany]
                    if (longVal != null && latVal != null) {
                        long += longVal * locFactor
                        lat += latVal * locFactor
                    }
                }
                myCities.add(el.lugar)

                val startMarker = Marker(map)
                val point = GeoPoint(long, lat)
                startMarker.setPosition(point)
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                startMarker.title = el.nombre + "\n" + el.de + " - " + el.hasta  + "\n" + el.empresa + "\n" +
                        "Media: " + el.media
                //startMarker.setTextIcon(el.nombre +)
                map.overlays.add(startMarker)
            }
        }


        return Inflator

    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(activity))
    }
}
