package com.example.nicolas.sicourses

import Helpers.roundOnDecimal
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
        Pair("Barcelona", listOf(41.390205, 2.154007)),
        Pair("Algeciras", listOf(36.13326, -5.45051)),
        Pair("Zaragoza", listOf(41.649693, -0.887712)),
        Pair("Sevilla", listOf(37.388489,	-5.995174)),
        Pair("Málaga", listOf(36.72016, -4.42034)),
        Pair("Murcia", listOf(37.98704, -1.13004)),
        Pair("Palma", listOf(39.56939, 2.65024)),
        Pair("Las Palmas de Gran Canaria", listOf(28.09973, -15.41343)),
        Pair("Alicante", listOf(38.34517, -0.48149)),
        Pair("Córdoba", listOf(37.89155, -4.77275)),
        Pair("Valladolid", listOf(41.65518, -4.72372)),
        Pair("Vigo", listOf(42.23282, -8.72264)),
        Pair("Barcelona", listOf(41.390205, 2.154007)),
        Pair("Gijón", listOf(43.53573, -5.66152)),
        Pair("Hospitalet de Llobregat", listOf(41.35967, 2.10028)),
        Pair("Vitoria", listOf(42.84998, -2.67268)),
        Pair("A Coruña", listOf(43.37135, -8.396)),
        Pair("Granada", listOf(37.18817, -3.60667)),
        Pair("Elche", listOf(38.26218, -0.70107)),
        Pair("Oviedo", listOf(43.36029, -5.84476)),
        Pair("Terrasa", listOf(41.56667, 2.01667)),
        Pair("Badalona", listOf(41.45004, 2.24741)),
        Pair("Cartagena", listOf(37.60512, -0.98623)),
        Pair("Jerez de la Frontera", listOf(36.68645, -6.13606)),
        Pair("Sabadell", listOf(41.54329, 2.10942)),
        Pair("Móstoles", listOf(40.32234, -3.86496)),
        Pair("Santa Cruz de Tenerife", listOf(28.46824, -16.25462)),
        Pair("Pamplona", listOf(42.81687, -1.64323)),
        Pair("Almería", listOf(36.83814, -2.45974)),
        Pair("Alcalá de Henares", listOf(40.48205, -3.35996)),
        Pair("Fuenlabrada", listOf(40.28419, -3.79415)),
        Pair("Leganés", listOf(40.32718, -3.7635)),
        Pair("San Sebastián", listOf(43.31283, -1.97499)),
        Pair("Getafe", listOf(40.30571, -3.73295)),
        Pair("Burgos", listOf(42.35022, -3.67527)),
        Pair("Albacete", listOf(38.99424, -1.85643)),
        Pair("Santander", listOf(43.46472, -3.80444)),
        Pair("Castellón de la Plana", listOf(39.98333, -0.03333)),
        Pair("San Cristóbal de la Laguna", listOf(28.4874009,  -16.3159061)),
        Pair("Alcorcón", listOf(40.34582, -3.82487)),
        Pair("Logroñö", listOf(42.46667, -2.45)),
        Pair("Badajoz", listOf(38.87789, -6.97061)),
        Pair("León", listOf(42.60003, -5.57032)),
        Pair("Teruel", listOf(40.3456, -1.10646)),
        Pair("Soria", listOf(41.76401, -2.46883)),
        Pair("Aranda de Duero", listOf(41.67041, -3.6892))
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

        val Inflator = inflater.inflate(R.layout.fragment_map, container, false)

        val map = Inflator.mapView

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

                startMarker.title = el.nombre + "\n" + el.de + " - " + el.hasta  + "\n" + el.empresa + ", " +
                        el.lugar + "\n" + "Media: " + roundOnDecimal(el.media)
                //startMarker.setTextIcon(el.nombre +)
                map.overlays.add(startMarker)
            }
        }

//        val lugares = courses.map {it.lugar}.distinct()
//
//        for (el in lugares) {
//            val loc = coords[el]
//            if (loc != null) {
//                val sub = courses.filter {it.lugar == el}
//
//                var long = loc[0]
//                var lat = loc[1]
//
//                val poiMarkers = RadiusMarkerClusterer(context)
//                for (c in sub) {
//                    val startMarker = Marker(map)
//                    val point = GeoPoint(long, lat)
//                    startMarker.setPosition(point)
//                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//
//                    startMarker.title = c.nombre + "\n" + c.de + " - " + c.hasta  + "\n" + c.empresa + "\n" +
//                            "Media: " + c.media
//                    poiMarkers.add(startMarker)
//
//                }
//                map.overlays.add(poiMarkers)
//                //map.overlays.add(poiMarkers)
//
//            }
//            map.invalidate()
//        }



        return Inflator

    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(activity))
    }
}
