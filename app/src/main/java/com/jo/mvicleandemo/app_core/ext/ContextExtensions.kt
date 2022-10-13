package com.jo.mvicleandemo.app_core.ext

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.util.TypedValue
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.app_core.AppConstants
import com.jo.mvicleandemo.app_core.data.remote.exception.NetworkException
import com.jo.mvicleandemo.app_core.data.remote.exception.UnauthorizedException
import com.jo.mvicleandemo.app_core.presentation.bases.AlertDialogFragment
import com.jo.mvicleandemo.auth_flow.presentation.view.AuthenticationActivity
import java.util.*


fun Context.getAppName(): String = applicationInfo.loadLabel(packageManager).toString()

fun Context.handleError(
    shouldNavigateToLogin: Boolean = false,
    shouldShowErrorDialog: Boolean,
    throwable: Throwable,
    retryAction: (() -> Unit)? = null
): Throwable {
    throwable.printStackTrace()
    val customError = throwable.getCustomException(this)
    when (customError) {
        is UnauthorizedException ->
            if (shouldNavigateToLogin) {
                AuthenticationActivity.start(this, isLoggingOut = true)
            }
        is NetworkException ->
            showErrorDialog(customError, retryAction)
        else -> if (shouldShowErrorDialog)
            showErrorDialog(customError, retryAction)
    }
    return customError
}

private fun Context.showErrorDialog(customError: Throwable, retryAction: (() -> Unit)? = null) {
    val fragmentManager: FragmentManager = (this as FragmentActivity).supportFragmentManager
    AlertDialogFragment.show(
        fragmentManager = fragmentManager,
        icon = R.drawable.ic_error_triangle,
        title = getString(R.string.sorry),
        description = customError.message ?: customError.localizedMessage,
        positiveActionButtonText = if (retryAction != null) getString(R.string.retry) else "",
        isPositiveActionVisible = true,
        positiveAction = retryAction
    )

}


tailrec fun Context?.activity(): Activity? = this as? Activity
    ?: (this as? ContextWrapper)?.baseContext?.activity()


// extension property to get screen orientation
val Context.orientation: Int
    get() {
        return when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            Configuration.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }


fun Context.showDatePicker(
    currentDate: String = "",
    maxYearsDate: Int = 0,
    minYearsDate: Int = 0,
    dateFormat: String = AppConstants.APP_DATE_PATTERN,
    dateSetListener: (String) -> Unit
) {

    val calendar = Calendar.getInstance()
    if (currentDate.isNotEmpty()) {
        val date: Date = currentDate.stringToDate(dateFormat)
        calendar.time = date
    }

    val datePickerDialog = DatePickerDialog(
        this,
        { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val formattedSelectedDate = calendar.time.dateToString(dateFormat)
            dateSetListener.invoke(formattedSelectedDate)
        },

        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
//    if (maxyearsDate != 0){
    val calendarMax = Calendar.getInstance()
    calendarMax.add(Calendar.YEAR, -maxYearsDate)
    datePickerDialog.datePicker.maxDate = calendarMax.timeInMillis
//    } else {
//        datePickerDialog.datePicker.maxDate = Date().time
//    }
    val calendarMin = Calendar.getInstance()
    calendarMin.add(Calendar.YEAR, -minYearsDate)
    datePickerDialog.datePicker.minDate = calendarMin.timeInMillis
    datePickerDialog.show()
}

fun Context.getActionBarHeight(): Int {
    val tv = TypedValue()
    return if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    } else 0
}