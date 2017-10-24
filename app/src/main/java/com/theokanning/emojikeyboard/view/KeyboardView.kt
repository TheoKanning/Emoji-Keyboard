package com.theokanning.emojikeyboard.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.theokanning.emojikeyboard.R
import com.wonderkiln.camerakit.CameraListener
import com.wonderkiln.camerakit.CameraView


/**
 * Created by Theo on 10/24/2017.
 */
class KeyboardView(context: Context) : FrameLayout(context) {

    private lateinit var cameraView: CameraView
    private lateinit var cameraLayout: View
    private lateinit var permissionsLayout: View

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_keyboard, this, true)
        cameraView = findViewById(R.id.camera_view)
        cameraLayout = findViewById(R.id.camera_layout)
        permissionsLayout = findViewById(R.id.permission_rationale_layout)
        val button: View = findViewById(R.id.take_picture)
        button.setOnClickListener {
            cameraView.captureImage()
        }
        val settingsButton: View = findViewById(R.id.go_to_settings)
        settingsButton.setOnClickListener { goToSettings() }
    }

    /**
     * Show the camera view and hide the permissions instructions
     */
    fun showCamera() {
        cameraLayout.visibility = View.VISIBLE
        permissionsLayout.visibility = View.GONE
    }

    /**
     * Show permissions instructions and hide the camera view
     */
    fun showPermissionsInstructions() {
        cameraLayout.visibility = View.GONE
        permissionsLayout.visibility = View.VISIBLE
    }

    fun setCameraListener(listener: CameraListener) {
        cameraView.setCameraListener(listener)
    }

    fun startCamera() {
        Log.d(TAG, "Starting Camera")
        cameraView.start()
    }

    fun stopCamera() {
        Log.d(TAG, "Stopping Camera")
        cameraView.stop()
    }

    private fun goToSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        startActivity(context, intent, Bundle.EMPTY)
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}