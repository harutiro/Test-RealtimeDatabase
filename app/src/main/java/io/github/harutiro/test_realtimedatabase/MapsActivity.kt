package io.github.harutiro.test_realtimedatabase

import android.Manifest
import android.content.pm.PackageManager
import android.icu.util.TimeUnit.values
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.model.FieldIndex
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import io.github.harutiro.test_realtimedatabase.databinding.ActivityMapsBinding
import java.time.chrono.JapaneseEra.values

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        デフォルトのやつ
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        大阪の表示
        findViewById<Button>(R.id.testMapButton).setOnClickListener {
            val osakaStation = LatLng(34.702423,135.495972)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(osakaStation, 10.0f))
        }

//        現在地のカメラ移動　ピン立て
        findViewById<Button>(R.id.testMapButton2).setOnClickListener {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//            パーミッション確認
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
//                TODO:ほんとはここでパーミッションの表示をするようにする
            }else{
//                現在地の表示
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        // Got last known location. In some rare situations this can be null.
                        Log.d("debag", "緯度:"+ location?.latitude.toString())
                        Log.d("debag", "経度:"+ location?.longitude.toString())

//                        カメラ移動
                        val osakaStation = LatLng(location!!.latitude,location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(osakaStation, 16.0f))
//                        ピン立て
                        val tokyo = LatLng(location!!.latitude, location.longitude)
                        mMap.addMarker(MarkerOptions().position(tokyo).title("現在地点"))


                    }

            }

        }



//        パーミッション確認
        parmission()


    }

    fun parmission(){
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        // Precise location access granted.
                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        // Only approximate location access granted.
                    } else -> {
                    // No location access granted.
                }
                }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //シドニーの表示
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

//        ツールバーの表示
        mMap.uiSettings.isMapToolbarEnabled = true

//        現在地点の出力
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //                TODO:ほんとはここでパーミッションの表示をするようにする

        }else{
            mMap.isMyLocationEnabled = true

        }


//        data class Place(
//            val name: String,
//            val address: String,
//            val picture: Uri,
//            val location: LatLng,
//        ) : ClusterItem {
//
//            override fun getTitle() = name
//            override fun getPosition() = location
//            override fun getSnippet() = null
//        }
//
////        クラスタマネージャー
//        val placeListClusterManager = ClusterManager<Place>(this, googleMap)
//        googleMap.setOnCameraIdleListener(placeListClusterManager)
//        googleMap.setOnMarkerClickListener(placeListClusterManager)
//        googleMap.setOnInfoWindowClickListener(placeListClusterManager)
//
//        // 前提として、placeRepository.listPlaces() でAPIからPlaceのリストが取得できるものとする。
//        placeRepository.listPlaces().observe { placeList ->
//            placeListClusterManager.apply {
//                clearItems()
//                addItems(placeList)
//                cluster()
//            }
//        }

    }
}