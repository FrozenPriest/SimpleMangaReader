package ru.frozenpriest.simplemangareader.data.local

import android.content.SharedPreferences

object PreferenceHelper {

    const val LOGIN = "LOGIN"
    const val USER_PASSWORD = "PASSWORD"
    const val TOKEN = "TOKEN"
    const val REFRESH_TOKEN = "REFRESH_TOKEN"

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.login
        get() = getString(LOGIN, "")
        set(value) {
            editMe {
                it.putString(LOGIN, value)
            }
        }

    var SharedPreferences.password
        get() = getString(USER_PASSWORD, "")
        set(value) {
            editMe {
                it.putString(USER_PASSWORD, value)
            }
        }

    var SharedPreferences.token
        get() = getString(TOKEN, "")
        set(value) {
            editMe {
                it.putString(TOKEN, value)
            }
        }
    var SharedPreferences.refresh_token
        get() = getString(REFRESH_TOKEN, "")
        set(value) {
            editMe {
                it.putString(REFRESH_TOKEN, value)
            }
        }

}