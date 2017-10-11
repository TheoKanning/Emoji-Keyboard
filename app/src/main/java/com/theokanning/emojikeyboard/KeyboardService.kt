package com.theokanning.emojikeyboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.inputmethodservice.InputMethodService
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import com.wonderkiln.camerakit.CameraView

class KeyboardService : InputMethodService() {

    private lateinit var cameraView: CameraView

    override fun onCreateInputView(): View {
        Log.e(TAG, "OnCreateInputView")
        val view = layoutInflater.inflate(R.layout.not_keyboard, null)
        cameraView = view.findViewById(R.id.camera_view)

        if(!checkPermission()) {
            val intent = Intent(applicationContext, PermissionActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        Log.e(TAG, "onStartInputView")
        cameraView.start()
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        Log.e(TAG, "OnFinishInputView")
        cameraView.stop()
        super.onFinishInputView(finishingInput)
    }

    private fun checkPermission() : Boolean{
        return ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val TAG = "KEYBOARD"
    }
}