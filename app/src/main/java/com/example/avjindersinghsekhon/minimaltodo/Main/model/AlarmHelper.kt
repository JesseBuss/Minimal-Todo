package com.example.avjindersinghsekhon.minimaltodo.Main.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.avjindersinghsekhon.minimaltodo.Utility.ToDoItem
import com.example.avjindersinghsekhon.minimaltodo.Utility.TodoNotificationService
import java.util.*

class AlarmHelper(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarms(items: List<ToDoItem>) {
        if (items != null) {
            for (item in items) {
                if (item.hasReminder() && item.toDoDate != null) {
                    if (item.toDoDate.before(Date())) {
                        item.toDoDate = null
                        continue
                    }
                    val i = Intent(context, TodoNotificationService::class.java)
                    i.putExtra(TodoNotificationService.TODOUUID, item.identifier)
                    i.putExtra(TodoNotificationService.TODOTEXT, item.toDoText)
                    createAlarm(i, item.identifier.hashCode(), item.toDoDate.time)
                }
            }
        }
    }

    fun createAlarmIfNecessary(item: ToDoItem) {
        if (item.hasReminder() && item.toDoDate != null) {
            val i = Intent(context, TodoNotificationService::class.java)
            i.putExtra(TodoNotificationService.TODOTEXT, item.toDoText)
            i.putExtra(TodoNotificationService.TODOUUID, item.identifier)
            createAlarm(i, item.identifier.hashCode(), item.toDoDate.time)
        }
    }

    private fun doesPendingIntentExist(i: Intent, requestCode: Int): Boolean {
        val pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_NO_CREATE)
        return pi != null
    }

    private fun createAlarm(i: Intent, requestCode: Int, timeInMillis: Long) {
        val am = alarmManager
        val pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT)
        am[AlarmManager.RTC_WAKEUP, timeInMillis] = pi
    }

    fun deleteAlarm(item: ToDoItem) {
        val requestCode = item.identifier.hashCode()
        val i = Intent(context, TodoNotificationService::class.java)
        if (doesPendingIntentExist(i, requestCode)) {
            val pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_NO_CREATE)
            pi.cancel()
            alarmManager.cancel(pi)
            Log.d("OskarSchindler", "PI Cancelled " + doesPendingIntentExist(i, requestCode))
        }
    }
}