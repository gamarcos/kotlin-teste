package com.gabriel.pokemonandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        LoadPokemon()
    }

    var ACCESS_LOCATION = 123

    fun checkPermission() {
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat
                    .checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_LOCATION)
            }
        }
        getUserLocarion()
    }

    @SuppressLint("MissingPermission")
    fun getUserLocarion() {
        Toast.makeText(this, "User location access on", Toast.LENGTH_LONG).show()
        //TODO: Will implemente letter
        var myLocation = MyLocationListenner()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
        var mythread = myThread()
        mythread.start()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode){
            ACCESS_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else {
                    Toast.makeText(this, "We can not access your location", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
    }

    var location: Location?= null

    //Get use location

    inner class MyLocationListenner: LocationListener {

        constructor(){
            location = Location("Start")
            location!!.longitude = 0.0
            location!!.latitude = 0.0
        }
        override fun onLocationChanged(p0: Location?) {
            location = p0
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(p0: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(p0: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    var oldLocation: Location?= null
    inner class myThread:Thread {
        constructor():super(){
            oldLocation = Location("Start")
            oldLocation!!.longitude = 0.0
            oldLocation!!.latitude = 0.0
        }
        override fun run() {
            while(true){
                try {
                    if(oldLocation!!.distanceTo(location)== 0f){
                        continue
                    }

                    oldLocation=location

                    runOnUiThread{
                        mMap!!.clear()
                        // Add a marker in Sydney and move the camera
                        val currentlyLoc = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                                .position(currentlyLoc)
                                .title("Me")
                                .snippet("Here is my location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlyLoc, 14f))

                        //show me a pokemon
                        for (i in 0..listPokemons.size-1){
                            var newPokemon   = listPokemons[i]

                            if(newPokemon.isCatch == false){

                                val pokemonLoc = LatLng(newPokemon.location!!.latitude, newPokemon.location!!.longitude)
                                mMap.addMarker(MarkerOptions()
                                        .position(pokemonLoc)
                                        .title(newPokemon.name)
                                        .snippet(newPokemon.des)
                                        .icon(BitmapDescriptorFactory.fromResource(newPokemon.img!!)))

                                if (location!!.distanceTo(newPokemon.location)<2){
                                    newPokemon.isCatch = true
                                    listPokemons[i]=newPokemon
                                    playerPower += newPokemon.power!!
                                    Toast.makeText(applicationContext, "You cath a new Pokemon ", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }

                    Thread.sleep(1000)
                }catch (ex:Exception){

                }
            }
        }
    }

    var playerPower = 0.0

    var listPokemons = ArrayList<Pokemon>()

    fun LoadPokemon() {
        listPokemons.add(Pokemon(R.drawable.charmander, "Charmander", "Here is from Japan", 55.0, -23.5881189, -46.6775043))
        listPokemons.add(Pokemon(R.drawable.bulbasaur, "Bulbasaur", "Pokemon type: Plant", 85.8, -23.5848428,-46.6755643))
        listPokemons.add(Pokemon(R.drawable.squirtle, "Squirtle", "Pokemon type: Water", 100.10, -23.5846052, -46.6773905))
    }
}
