package com.jo.mvicleandemo.app_core.ext

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.text.Html
import android.text.Spanned
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import com.jo.mvicleandemo.BuildConfig
import com.jo.mvicleandemo.app_core.AppConstants
import java.io.*
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.*


fun String.getNameAbbreviation(limit: Int = 2): String {
    var abbreviation = ""
    return if (isEmpty())
        abbreviation
    else {
        val names = split(" ")
        for (name in names) {
            if (name.isNotEmpty())
                abbreviation += name[0]
        }
        abbreviation.uppercase(Locale.getDefault()).take(limit)
    }
}

fun String.reformatDate(
    inputFormat: String = AppConstants.SERVER_DATE_TIME_PATTERN,
    outputFormat: String = AppConstants.APP_DATE_TIME_PATTERN
): String {
    if (isEmpty()) return ""
    val inputFormatter = SimpleDateFormat(inputFormat, Locale.getDefault())
    val serverDate = inputFormatter.parse(this) ?: Date()
    return serverDate.dateTimeToString(outputFormat)
}

fun String.stringToDateTime(format: String = AppConstants.APP_DATE_TIME_PATTERN): Date {
    if (isEmpty()) return Date()
    val outputFormatter = SimpleDateFormat(format, Locale.getDefault())
    return outputFormatter.parse(this) ?: Date()
}

fun String.stringToDate(format: String = AppConstants.APP_DATE_PATTERN): Date {
    if (isEmpty()) return Date()
    val outputFormatter = SimpleDateFormat(format, Locale.getDefault())
    return outputFormatter.parse(this) ?: Date()
}


fun Date.dateTimeToString(format: String = AppConstants.APP_DATE_TIME_PATTERN): String {
    val outputFormatter = SimpleDateFormat(format, Locale.getDefault())
    return outputFormatter.format(this)
}

fun Date.dateToString(format: String = AppConstants.APP_DATE_PATTERN): String {
    val outputFormatter = SimpleDateFormat(format, Locale.getDefault())
    return outputFormatter.format(this)
}


fun String.calculateAgeFromDate(
    serverFormat: String = AppConstants.SERVER_DATE_TIME_PATTERN
): Int {
    if (isEmpty()) return 0
    val inputFormatter = SimpleDateFormat(serverFormat, Locale.getDefault())
    val serverDate = inputFormatter.parse(this) ?: Date()
    return serverDate.age()
}

//'1970' is, that's the Unix Epoch. The timestamp is 0 on January 1, 1970.
fun Date.age(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = Date(time - Date().time)
    return 1970 - (calendar.get(Calendar.YEAR) + 1)
}

fun Context.getFormattedStringFromResources(
    @StringRes resId: Int,
    vararg content: String
): Spanned {
    val stringFormat = getString(resId)
    return Html.fromHtml(
        String.format(
            stringFormat,
            *content
        )
    )
}

fun String.getResourceId(c: Class<*>): Int {
    return try {
        val idField: Field = c.getDeclaredField(this)
        idField.getInt(idField)
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }
}


fun Uri.getFile(context: Context, prefix: String = "", fileExtension: String): File {
    val inputStream: InputStream? = context.contentResolver.openInputStream(this)
    val file = File.createTempFile(prefix, fileExtension)
    val outStream: OutputStream = FileOutputStream(file)
    outStream.write(inputStream?.readBytes())
    return file
}

fun Uri.getVideoDuration(context: Context): Long {
    var durationTime = 0L
    MediaPlayer.create(context, this)?.also {
        durationTime = (it.duration / 1000).toLong()
        it.reset()
        it.release()
    }
    return durationTime
}

fun File.getUri(context: Context): Uri {
    return FileProvider.getUriForFile(
        context.applicationContext,
        "${BuildConfig.APPLICATION_ID}.provider",
        this
    )
}

// Copy the source file to target file.
// In case the dst file does not exist, it is created
fun File.rename(newName: String): File {
    val newPath: String = absolutePath.replace(nameWithoutExtension, newName)
    val target = File(newPath)
    val input: InputStream = FileInputStream(this)
    val output: OutputStream = FileOutputStream(target)

    // Copy the bits from inStream to outStream
    val buf = ByteArray(1024)
    var len: Int
    while (input.read(buf).also { len = it } > 0) {
        output.write(buf, 0, len)
    }
    input.close()
    output.close()
    return target
}

fun Int.durationToMinutesSeconds(): String {
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return String.format("%02d:%02d", minutes, seconds)
}