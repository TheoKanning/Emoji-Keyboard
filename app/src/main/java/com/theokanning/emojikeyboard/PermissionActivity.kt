package com.theokanning.emojikeyboard

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.widget.Toast

/**
 * Activity that currently only exists to request camera permission
 */
class PermissionActivity : Activity() {

    private val PERMISSION_CAMERA_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
    }

    override fun onResume() {
        super.onResume()
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    finish()
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
