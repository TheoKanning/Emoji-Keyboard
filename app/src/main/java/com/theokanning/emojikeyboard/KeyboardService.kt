package com.theokanning.emojikeyboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.inputmethodservice.InputMethodService
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.wonderkiln.camerakit.CameraListener
import com.wonderkiln.camerakit.CameraView

class KeyboardService : InputMethodService() {

    private lateinit var cameraView: CameraView
    private val visionService = EmojiService()

    override fun onCreateInputView(): View {
        Log.e(TAG, "OnCreateInputView")
        val view = layoutInflater.inflate(R.layout.keyboard, null)
        cameraView = view.findViewById(R.id.camera_view)
        val button : View = view.findViewById(R.id.take_picture)
        button.setOnClickListener { takePicture() }

        if (!checkPermission()) {
            val intent = Intent(applicationContext, PermissionActivity::class.java)
            startActivity(intent)
        }

        cameraView.setCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                val encodedString = Base64.encodeToString(jpeg, Base64.DEFAULT)
                visionService.getEmojiForImage(encodedString, { onEmojiReceived(it) })
            }
        })
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

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun takePicture() {
        Log.d(TAG, "Taking picture")
        cameraView.captureImage()
    }

    private fun onEmojiReceived(emoji: String?) {
        if(emoji != null) {
            currentInputConnection.commitText(emoji, 1)
        } else {
            Toast.makeText(this@KeyboardService.applicationContext, "No matching emoji", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val TAG = KeyboardService::class.java.simpleName
    }
}