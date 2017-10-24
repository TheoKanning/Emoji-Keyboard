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
import com.theokanning.emojikeyboard.emoji.EmojiService
import com.theokanning.emojikeyboard.view.KeyboardView
import com.wonderkiln.camerakit.CameraListener
import javax.inject.Inject


class KeyboardService : InputMethodService() {

    @Inject
    lateinit var emojiService: EmojiService

    private lateinit var keyboardView: KeyboardView

    private var checkedForPermission = false

    override fun onCreateInputView(): View {
        Log.d(TAG, "OnCreateInputView")
        keyboardView = KeyboardView(applicationContext)

        (application as KeyboardApplication).getComponent().inject(this)

        return keyboardView
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        Log.d(TAG, "onStartInputView: restarting=$restarting")

        if (!hasCameraPermission() && !checkedForPermission) {
            checkedForPermission = true
            val intent = Intent(applicationContext, PermissionActivity::class.java)
            startActivity(intent)
        } else if (!hasCameraPermission()) {
            keyboardView.showPermissionsInstructions()
        } else {
            keyboardView.showCamera()
            keyboardView.startCamera()
            keyboardView.setCameraListener(cameraListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroy")
        keyboardView.stopCamera()
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun onEmojiReceived(emoji: String?) {
        if (emoji != null) {
            currentInputConnection.commitText(emoji, 1)
        } else {
            Toast.makeText(this@KeyboardService.applicationContext, "No matching emoji", Toast.LENGTH_LONG).show()
        }
    }

    private val cameraListener = object : CameraListener() {
        override fun onPictureTaken(jpeg: ByteArray?) {
            val encodedString = Base64.encodeToString(jpeg, Base64.DEFAULT)
            emojiService.getEmojiForImage(encodedString, { onEmojiReceived(it) })
        }
    }

    companion object {
        private val TAG = KeyboardService::class.java.simpleName
    }
}