package com.test.native_project.comm

import android.icu.text.DateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale

class Common {

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun DiffToNowDate(date1: String): String {
            val dateTime: LocalDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                LocalDateTime.parse(date1, formatter)
            } else {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = formatter.parse(date1) ?: return "날짜 오류"
                val calendar = Calendar.getInstance()
                calendar.time = date
                LocalDateTime.of(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND)
                )
            }

            val now = LocalDateTime.now()
            val minutesDiff = ChronoUnit.MINUTES.between(dateTime, now)

            return when {
                minutesDiff in -1..1 -> "방금"
                minutesDiff in 2..59 -> "${minutesDiff}분 전"
                minutesDiff in -59..-2 -> "${-minutesDiff}분 후"
                minutesDiff in 60..1439 -> "${minutesDiff / 60}시간 전"
                minutesDiff in -1439..-60 -> "${-minutesDiff / 60}시간 후"
                minutesDiff >= 1440 -> "${minutesDiff / 1440}일 전"
                minutesDiff <= -1440 -> "${-minutesDiff / 1440}일 후"
                else -> "시간 계산 오류"
            }
        }
    }
}