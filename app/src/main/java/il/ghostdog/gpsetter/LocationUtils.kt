package il.ghostdog.gpsetter

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.location.provider.ProviderProperties
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object LocationUtils {

    private const val GPS_PROVIDER = "My GPS"//LocationManager.GPS_PROVIDER
    private const val REAL_GPS_PROVIDER = LocationManager.GPS_PROVIDER

    private var locationManager: LocationManager? = null

    // Function to set the phone location to the given point
    @RequiresApi(Build.VERSION_CODES.S)
    fun setCustomLocation(context: Context, latitude: Double, longitude: Double) {
        if (locationManager == null) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        /// Create a new Location object with desired coordinates
        val customLocation = Location(GPS_PROVIDER)
        customLocation.latitude = latitude
        customLocation.longitude = longitude
        customLocation.accuracy = 5.0f // Set a default accuracy value
        customLocation.elapsedRealtimeNanos = 1L
        customLocation.time = System.currentTimeMillis() // Set the current time as the timestamp


        // Add the GPS provider as a test provider if it doesn't already exist
        val provider = locationManager?.getProvider(GPS_PROVIDER)
        if (provider == null) {
            locationManager?.addTestProvider(
                GPS_PROVIDER,
                false,
                false,
                false,
                false,
                true,
                true,
                true,
                ProviderProperties.POWER_USAGE_LOW,
                ProviderProperties.ACCURACY_FINE
            )
        }

        // Enable the GPS provider as a test provider
        locationManager?.setTestProviderEnabled(GPS_PROVIDER, true)

        // Set the custom location
        locationManager?.setTestProviderLocation(GPS_PROVIDER, customLocation)
    }

    // Function to reset the location to normal
    fun resetLocation(context: Context) {
        if (locationManager == null) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        // Disable the test provider to reset the location
        locationManager?.apply {
            //setTestProviderEnabled(GPS_PROVIDER, false)
            removeTestProvider(GPS_PROVIDER)
        }
    }

    fun getLastKnownLocation(context: Context): Location? {
        if (locationManager == null) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        var location: Location? = null

        // Get last known location from GPS provider
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            return null
        }
        location = locationManager?.getLastKnownLocation(REAL_GPS_PROVIDER)

        return location
    }
}
