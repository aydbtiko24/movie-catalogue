package com.dicodingjetpackpro.moviecatalogue.data.source.remote

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Format default date format yyyy-MM-dd to EEE d MMM yyyy
 * @return formatted date string
 */
internal fun String.asReadableDate(): String {
    val unknownDate = "-"

    if (this.trim().isEmpty()) return unknownDate
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = simpleDateFormat.parse(this)?.time ?: return unknownDate

    return SimpleDateFormat("EEE d MMM yyyy", Locale.getDefault()).format(date)
}
