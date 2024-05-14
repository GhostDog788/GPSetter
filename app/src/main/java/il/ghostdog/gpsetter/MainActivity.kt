package il.ghostdog.gpsetter

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSetLocation = findViewById<Button>(R.id.btn_set_location)
        btnSetLocation.setOnClickListener { onSetLocation() }

        val btnResetLocation = findViewById<Button>(R.id.btn_reset_location)
        btnResetLocation.setOnClickListener { onResetLocation() }

        val btnPrintLocation = findViewById<Button>(R.id.btn_print_location)
        btnPrintLocation.setOnClickListener { onPrintLocation() }
    }

    private fun onPrintLocation() {
        if (!PermissionUtils.checkLocationPermission(this)) {
            PermissionUtils.requestLocationPermission(this)
        }

        val location = LocationUtils.getLastKnownLocation(this)
        Toast.makeText(this, "${location?.altitude}, ${location?.longitude}", Toast.LENGTH_SHORT).show()
    }

    private fun onResetLocation() {
        if (!PermissionUtils.checkLocationPermission(this)) {
            PermissionUtils.requestLocationPermission(this)
            return
        }

        LocationUtils.resetLocation(this)
        Toast.makeText(this, "Location has been reset back to normal", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun onSetLocation() {
        if (!PermissionUtils.checkLocationPermission(this)) {
            PermissionUtils.requestLocationPermission(this)
            return
        }

        val etNorth = findViewById<EditText>(R.id.et_north)
        val etEast = findViewById<EditText>(R.id.et_east)

        if(etNorth.text.isEmpty() || etEast.text.isEmpty())
        {
            Toast.makeText(this, "North or East fields are empty", Toast.LENGTH_SHORT).show()
            return
        }

        val north = etNorth.text.toString().toDoubleOrNull()
        val east = etEast.text.toString().toDoubleOrNull()
        if (north == null
            || east == null)
        {
            Toast.makeText(this, "North or East fields are not doubles", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            LocationUtils.setCustomLocation(this, north, east)
            Toast.makeText(this, "Location has been set", Toast.LENGTH_SHORT).show()
        }catch (e: SecurityException){
            Toast.makeText(this, "Can not set location", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}