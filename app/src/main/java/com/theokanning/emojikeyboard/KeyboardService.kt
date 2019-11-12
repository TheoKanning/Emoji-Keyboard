package com.theokanning.emojikeyboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.theokanning.emojikeyboard.analytics.Analytics
import com.theokanning.emojikeyboard.emoji.EmojiService
import com.theokanning.emojikeyboard.view.KeyboardView
import com.wonderkiln.camerakit.CameraListener
import javax.inject.Inject


class KeyboardService : InputMethodService() {

    @Inject
    lateinit var emojiService: EmojiService

    @Inject
    lateinit var analytics: Analytics

    private lateinit var keyboardView: KeyboardView

    private var checkedForPermission = false

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "OnCreate")
        keyboardView = KeyboardView(applicationContext)
    }

    override fun onCreateInputView(): View {
        Log.d(TAG, "OnCreateInputView")
        // stop previous keyboard
        keyboardView.stopCamera()
        keyboardView = KeyboardView(applicationContext)

        (application as KeyboardApplication).getComponent().inject(this)

        return keyboardView
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        Log.d(TAG, "onStartInputView")

        logOrientation()

        // android 10 doesn't allow starting activities from services
        if (!hasCameraPermission() && !checkedForPermission && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
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

    override fun onFinishInputView(finishingInput: Boolean) {
        Log.d(TAG, "OnFinishInputView")
        keyboardView.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroy")
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun onEmojiReceived(emoji: String?) {
        keyboardView.showButton()
        if (emoji != null) {
            currentInputConnection.commitText(emoji, 1)
        }
        val message = emoji ?: applicationContext.getString(R.string.no_emoji_found)
        showToast(message)
    }

    private fun showToast(message: String) {
        val toastView = layoutInflater.inflate(R.layout.toast, null)
        val text = toastView.findViewById<TextView>(R.id.toast_text)
        text.text = message

        val toast = Toast.makeText(this@KeyboardService.applicationContext, message, Toast.LENGTH_LONG)
        toast.view = toastView
        toast.show()
    }

    private fun logOrientation() {
        analytics.orientation(resources.configuration.orientation)
    }


    private val cameraListener = object : CameraListener() {
        override fun onPictureTaken(jpeg: ByteArray?) {
            val encodedString = Base64.encodeToString(jpeg, Base64.DEFAULT)
            Log.d(TAG, "Received image, ${jpeg?.size} bytes")
            emojiService.getEmojiForImage(encodedString, { onEmojiReceived(it) })
        }
    }

    companion object {
        private val TAG = KeyboardService::class.java.simpleName
    }
}