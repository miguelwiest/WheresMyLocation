package br.edu.infnet.WheresMyLocation

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import br.edu.infnet.WheresMyLocation.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(), LocationListener {

    val COARSE_REQUEST = 11111
    val FINE_REQUEST = 22222

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGetLocation = this.findViewById<Button>(R.id.btnGetLocation)
        btnGetLocation.setOnClickListener {
            this.getLocationByNetwork()
            this.getLocationByGps()
        }

        val btnSaveLocation = this.findViewById<Button>(R.id.btnSaveLocation)
        btnSaveLocation.setOnClickListener {

            val file = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "text.txt")
            val fos = FileOutputStream(file)
            val lblLat = this.findViewById<TextView>(R.id.lblLat)
            val lblLong = this.findViewById<TextView>(R.id.lblLong)
            fos.write(lblLat.text.toString().toByteArray() + lblLong.text.toString().toByteArray())
            fos.close()
            lblLat.text = null

            val btnRead = findViewById<Button>(R.id.btnRead)
            btnRead.setOnClickListener {
                val file = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "text.txt")
                val fis = FileInputStream(file)
                val bytes = fis.readBytes()
                fis.close()
                val lblLocationSaved = this.findViewById<TextView>(R.id.lblLocationSaved)
                lblLocationSaved.text = String(bytes)
            }

        }
    }

    override fun onLocationChanged(location: Location) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when(requestCode) {

                COARSE_REQUEST -> this.getLocationByNetwork()
                FINE_REQUEST -> this.getLocationByGps()
            }

        } else {

            Toast.makeText(this, "Permission to get Location denied!", Toast.LENGTH_LONG).show()
        }
    }

    private fun getLocationByNetwork() {
        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val isProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(isProviderEnabled) {

            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    2000L,
                    0f,
                    this
                )

                val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                val lblLat = this.findViewById<TextView>(R.id.lblLat)
                lblLat.text = location!!.latitude.toString()
                val lblLong = this.findViewById<TextView>(R.id.lblLong)
                lblLong.text = location.longitude.toString()
            }
            else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), COARSE_REQUEST)


            }
        }
    }

    private fun getLocationByGps() {
        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val isProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(isProviderEnabled) {

            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000L,
                    0f,
                    this
                )

                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                val lblLat = this.findViewById<TextView>(R.id.lblLat)
                lblLat.text = location!!.latitude.toString()
                val lblLong = this.findViewById<TextView>(R.id.lblLong)
                lblLong.text = location.longitude.toString()
            }
            else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_REQUEST)
            }
        }
    }

}