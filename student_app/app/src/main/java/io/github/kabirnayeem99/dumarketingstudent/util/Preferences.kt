package io.github.kabirnayeem99.dumarketingstudent.util

import android.content.Context
import android.content.SharedPreferences
import io.github.kabirnayeem99.dumarketingstudent.util.enums.BatchYear


/**
 * Class for shared preferences
 */
class Preferences(context: Context) {

    private var sharedPreferences =
        context.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)


    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    init {
        editor.apply()
    }

    fun setBatchYear(batchYear: String) {
        editor.putString(Config.BATCH_YEAR, batchYear)
        editor.apply()
    }

    fun getBatchYear(): String? {
        return sharedPreferences.getString(Config.BATCH_YEAR, BatchYear.Null.value)
    }

}