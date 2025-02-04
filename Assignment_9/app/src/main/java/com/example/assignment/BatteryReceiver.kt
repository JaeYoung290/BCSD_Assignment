package com.example.assignment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.widget.Toast

class BatteryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level * 100 / scale.toFloat()

            when (batteryPct) {
                15f -> Toast.makeText(context, "배터리가 15% 이하입니다. 충전이 필요합니다.", Toast.LENGTH_SHORT).show()
                50f -> Toast.makeText(context, "배터리가 50% 남았습니다.", Toast.LENGTH_SHORT).show()
                90f -> Toast.makeText(context, "배터리가 90% 남았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}