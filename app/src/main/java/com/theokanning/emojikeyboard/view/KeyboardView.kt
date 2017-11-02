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
import kotlinx.android.synthetic.main.view_keyboard.view.*


class KeyboardView(context: Context) : FrameLayout(context) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_keyboard, this, true)

        takePictureButton.setOnClickListener {
            showLoading()
            cameraView.captureImage()
        }

        goToSettingsButton.setOnClickListener { goToSettings() }
    }

    /**
     * Show the camera view and hide the permissions instructions
     */
    fun showCamera() {
        cameraLayout.visibility = View.VISIBLE
        permissionRationale.visibility = View.GONE
    }

    /**
     * Show permissions instructions and hide the camera view
     */
    fun showPermissionsInstructions() {
        cameraLayout.visibility = View.GONE
        permissionRationale.visibility = View.VISIBLE
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

    fun showButton() {
        progress.visibility = View.GONE
        takePictureButton.visibility = View.VISIBLE
    }

    fun showLoading() {
        progress.visibility = View.VISIBLE
        takePictureButton.visibility = View.GONE
    }

    private fun goToSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        startActivity(context, intent, Bundle.EMPTY)
    }

    companion object {
        private val TAG = KeyboardView::class.java.simpleName
    }
}