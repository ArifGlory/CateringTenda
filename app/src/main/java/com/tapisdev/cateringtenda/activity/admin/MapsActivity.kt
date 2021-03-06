package com.tapisdev.cateringtenda.activity.admin

import android.Manifest
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.SharedVariable
import com.tapisdev.cateringtenda.model.UserPreference
import com.tapisdev.cateringtenda.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.ArrayList

class MapsActivity : BaseActivity(), OnMapReadyCallback,PermissionHelper.PermissionListener,
    LocationListener {

    private lateinit var mMap: GoogleMap
    lateinit var  permissionHelper : PermissionHelper
    var lat  = 0.0
    var lon  = 0.0
    lateinit var centerMapLatLon :LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mUserPref = UserPreference(this)

        permissionHelper = PermissionHelper(this)
        permissionHelper.setPermissionListener(this)


        permissionLocation()
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
        mMap.setMyLocationEnabled(true)

        var initialLocation = LatLng(-5.382109, 105.257912)
        val zoomLevel = 16.0f //This goes up to 21

        if (mUserPref.getLatlon().equals("none") || mUserPref.getLatlon().equals("")){
            initialLocation = LatLng(-5.382109, 105.257912)
        }else{
            var latlon = mUserPref.getLatlon()!!
            val index   = latlon.indexOf(",")
            val lon     = latlon.substring(index+1)
            val lat     = latlon.substring(0,index)
            Log.d("latlon","lat : "+lat + "| lon : "+lon)
            val lokasi = LatLng(lat.toDouble(), lon.toDouble())
            initialLocation = lokasi
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation,zoomLevel))

        tvSelectLocation.setOnClickListener {
            centerMapLatLon = mMap.projection.visibleRegion.latLngBounds.center
            SharedVariable.lokasiPenyedia = centerMapLatLon
            showSuccessMessage("Lokasi dipilih")
            onBackPressed()
        }
    }

    private fun permissionLocation() {
        var listPermissions: MutableList<String> = ArrayList()
        listPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        listPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionHelper.checkAndRequestPermissions(listPermissions)
    }

    override fun onPermissionCheckDone() {

    }

    override fun onLocationChanged(location: Location?) {
        if (location == null) {
            showErrorMessage("Lokasi Kamu Tidak Dapat Ditemukan")
        } else {
            lat = location.latitude
            lon = location.longitude
            showInfoMessage("lat :"+lat + " | lon:"+lon)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }
}
