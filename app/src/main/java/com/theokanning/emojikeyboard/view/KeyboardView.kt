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
import com.camerakit.CameraKitView
import com.theokanning.emojikeyboard.R
import kotlinx.android.synthetic.main.view_keyboard.view.*


class KeyboardView(context: Context) : FrameLayout(context) {

    // todo replace this with a PublishSubject or something
    var callback : CameraKitView.ImageCallback? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_keyboard, this, true)

        initializeButtons()
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

    fun setImageCallback(callback: CameraKitView.ImageCallback) {
        this.callback = callback
    }

    fun startCamera() {
        Log.d(TAG, "Starting Camera")
        cameraView.onStart()
        cameraView.onResume()
    }

    fun stopCamera() {
        Log.d(TAG, "Stopping Camera")
        cameraView.onPause()
        cameraView.onStop()
    }

    fun showButton() {
        progress.visibility = View.GONE
        takePictureButton.visibility = View.VISIBLE
    }

    fun showLoading() {
        progress.visibility = View.VISIBLE
        takePictureButton.visibility = View.GONE
    }

    private fun initializeButtons() {
        takePictureButton.setOnClickListener {
            showLoading()
            Log.d(TAG, "Taking picture")
            callback.let { cameraView.captureImage(it) }
        }

        goToSettingsButton.setOnClickListener { goToSettings() }

        changeCameraButton.setOnClickListener { cameraView.toggleFacing() }
    }

    private fun goToSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        startActivity(context, intent, Bundle.EMPTY)
    }

    companion object {
        private val TAG = KeyboardView::class.java.simpleName
    }
}