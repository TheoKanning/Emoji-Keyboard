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
import android.widget.LinearLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.theokanning.emojikeyboard.R
import com.wonderkiln.camerakit.CameraListener
import kotlinx.android.synthetic.main.view_keyboard.view.*


class KeyboardView(context: Context) : LinearLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_keyboard, this, true)
        orientation = VERTICAL

        initializeButtons()
        initializeAdView()
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

    private fun initializeButtons() {
        takePictureButton.setOnClickListener {
            showLoading()
            cameraView.captureImage()
        }

        goToSettingsButton.setOnClickListener { goToSettings() }
    }

    private fun initializeAdView() {
        val adRequest = AdRequest.Builder().build()
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                adView.requestLayout() // required to make ad appear in landscape mode for some reason
            }
        }

        adView.loadAd(adRequest)
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