package com.jo.core.utils

import java.util.*
import java.util.regex.Pattern

object ValidationUtils {

    fun isValidImageURL(url: String?): Boolean {
        return if (url != null) {
            url.lowercase(Locale.getDefault()).contains("jpg") || url.lowercase(Locale.getDefault())
                .contains("jpeg") ||
                    url.lowercase(Locale.getDefault()).contains("png")
        } else false
    }

    fun isValidEmail(email: String): Boolean {
        if (email.trim { it <= ' ' }.isEmpty()) {
            return false
        }
        val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
        val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email.trim())
        return matcher.matches()
    }

    /*
    ^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$
    ^                 # start-of-string
    (?=.*[0-9])       # a digit must occur at least once
    (?=.*[a-z])       # a lower case letter must occur at least once
    (?=.*[A-Z])       # an upper case letter must occur at least once
    (?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
    (?=\\S+$)          # no whitespace allowed in the entire string
    .{6,}             # anything, at least six places though
    $                 # end-of-string
     */
    //Minimum 8 characters,Maximum 10 characters, at least one uppercase letter, one lowercase letter,
    // one number and one special character:
    fun isValidPassword(
        password: String?, ignoreSpecialChars: Boolean = true,
        minNumberOfCharacters: Int = 6, maxNumberOfCharacters: Int = 10
    ): Boolean {
        return if (password != null) {
            val regexWithoutSpecialChars =
                ("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{$minNumberOfCharacters,$maxNumberOfCharacters}$")
            val regexWithSpecialChars =
                ("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{$minNumberOfCharacters,$maxNumberOfCharacters}\$")
            val regex = if (ignoreSpecialChars)
                regexWithoutSpecialChars
            else
                regexWithSpecialChars
            val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(password.toString().trim())
            (matcher.matches())
        } else false
    }

    fun isValidMobileNumber(mobile: String?): Boolean {
        if (mobile != null) {
            val _mobileNumber = mobile.removePrefix("+2")
            return (_mobileNumber.trim { it <= ' ' }.length == 11 &&
                    _mobileNumber.trim()[0] == '0' &&
                    _mobileNumber.trim()[1] == '1')
        }
        return false
    }

    fun isValidMobileNumberWithRegex(mobile: String?): Boolean {
        mobile?.let {
            val regex = "^(\\(\\+2\\) 01)[0-9]{9}$"
            val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(it.trim())
            return (matcher.matches())
        }
        return false
    }


    fun isValidLandLineNumber(homePhone: String?): Boolean {
        if (homePhone != null) {
            if (homePhone.trim { it <= ' ' }.length in 8..11) return true
        }
        return false
    }

    fun isValidNationalIDNumber(idNumber: String?): Triple<Boolean, String, Int> {
        var dateOfBirth = ""
        if (idNumber != null) {
            if (idNumber.trim { it <= ' ' }.length == 14) {
                if (idNumber.trim { it <= ' ' }[0] == '2' || idNumber.trim { it <= ' ' }[0] == '3') {
                    dateOfBirth = "" + idNumber.trim { it <= ' ' }[5] + "" +
                            idNumber.trim { it <= ' ' }[6] + "/" + "" + idNumber.trim { it <= ' ' }[3] +
                            "" + idNumber.trim { it <= ' ' }[4] + "/"
                    if (idNumber.trim { it <= ' ' }[0] == '2') {
                        dateOfBirth += ("19" + idNumber.trim { it <= ' ' }[1]
                                + "" + idNumber.trim { it <= ' ' }[2])
                    } else if (idNumber.trim { it <= ' ' }[0] == '3') {
                        dateOfBirth += ("20" + idNumber.trim { it <= ' ' }[1]
                                + "" + idNumber.trim { it <= ' ' }[2])
                    }
                }
                if (validateDateOfBirth(dateOfBirth)) {
                    return Triple(
                        true, dateOfBirth,
                        if (idNumber[12].code % 2 == 0) 2 else 1
                    )
                }

            }
        }
        return Triple(false, dateOfBirth, 1)
    }


    fun isValidText(text: String?): Boolean {
        if (text != null) {
            if (text.trim { it <= ' ' }.isNotEmpty()) return true
        }
        return false
    }


    private val DATE_PATTERN = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$"


    /**
     * Validate date format with regular expression
     *
     * @param date date address for validation
     * @return true valid date format, false invalid date format
     */
    fun validateDateOfBirth(date: String): Boolean {

        /** dd/MM/yyyy  */
        val matcher =
            Pattern.compile(DATE_PATTERN, Pattern.CASE_INSENSITIVE)
                .matcher(date.trim { it <= ' ' })

        if (matcher.matches()) {
            matcher.reset()

            if (matcher.find()) {
                val day = date.substring(0, 2)
                val month = date.substring(3, 5)
                val year = Integer.parseInt(date.substring(6))

                return if (day == "31" && (month == "4" || month == "6" || month == "9" ||
                            month == "11" || month == "04" || month == "06" ||
                            month == "09")
                ) {
                    false // only 1,3,5,7,8,10,12 has 31 days
                } else if (month == "2" || month == "02") {
                    //leap year
                    if (year % 4 == 0) {
                        !(day == "30" || day == "31")
                    } else {
                        !(day == "29" || day == "30" || day == "31")
                    }
                } else {
                    true
                }
            } else {
                return false
            }
        } else {
            return false
        }
    }


    fun findMaxChar(str: String): Int {

        val count = IntArray(256)

        for (i in 0 until str.length) {
            count[str[i].code]++
        }

        var max = -1
        var result = ' '

        for (j in 0 until str.length) {
            if (max < count[str[j].code] && count[str[j].code] > 1) {
                max = count[str[j].code]
                result = str[j]
            }
        }

        return max

    }

    fun isValidCVC(cvc: String): Boolean {
        return cvc.length >= 3
    }

    val USERNAME_REGEX = "^[a-zA-Z\\u0621-\\u064A].{2,30}$"
    //public static final String USERNAME_REGEX = "/^[a-zA-Z]+._-/i{3,30}$";

    fun isValidUserName(name: String): Boolean {
        val pattern = Pattern.compile(USERNAME_REGEX, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(name)
        return matcher.matches()
    }
}