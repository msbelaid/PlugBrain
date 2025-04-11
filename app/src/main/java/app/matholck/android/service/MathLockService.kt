package app.matholck.android.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MathLockService : AccessibilityService() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("MathLockService", "service started")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onServiceConnected() {
        Log.e("MathLockService", "service connected")
        super.onServiceConnected()
    }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageName: String = event?.packageName.toString()
        Log.e("MathLockService", packageName)
    }

    override fun onInterrupt() {
        // TODO
    }
}
