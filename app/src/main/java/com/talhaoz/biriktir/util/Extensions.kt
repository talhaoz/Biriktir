package com.talhaoz.biriktir.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatAsDate(pattern: String = "dd.MM.yyyy"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this))
}
