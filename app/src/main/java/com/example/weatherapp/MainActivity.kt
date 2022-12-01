package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var laT:Double = 0.0
    var lonG:Double = 0.0
    var CITY:String = "Jaipur,ind"
    val API: String = "d9ee6ccb2d340f413af8fe047c2fbf01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val message = intent.getStringExtra("message_key")
        CITY = message.toString()
        //checkLocationPermission()
        weatherask().execute()

    }

//    private fun checkLocationPermission() {
//
//        val ftask = fusedLocationProviderClient.lastLocation
//        //check location permission
//        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
//        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
//            return
//        }
//
//        //get latitude and longitude
//        ftask.addOnSuccessListener {
//            if(it!=null){
//                laT = it.latitude
//                lonG = it.longitude
//            }
//        }
//
//    }

    @SuppressLint("StaticFieldLeak")
    inner class weatherask() : AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorMessage).visibility = View.GONE

        }

        override fun doInBackground(vararg p0: String?): String? {

           //currentCordinates()
            var response:String? = try{
               URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8)
//                URL("https://api.openweathermap.org/data/2.5/onecall?lat=${laT}&lon=${lonG}&units=metric&appid={API key}").readText(
//                    Charsets.UTF_8
//                )
            } catch(e : Exception) {
                null
            }

            return response
        }

        @SuppressLint("DefaultLocale")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name")+", "+sys.getString("country")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.minTemp).text = tempMin
                findViewById<TextView>(R.id.maxTemp).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.humidity).text = humidity

                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                findViewById<ConstraintLayout>(R.id.mainContainer).visibility = View.VISIBLE
            }
            catch (e: Exception)
            {
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                findViewById<TextView>(R.id.errorMessage).visibility = View.VISIBLE
            }
        }
    }

}