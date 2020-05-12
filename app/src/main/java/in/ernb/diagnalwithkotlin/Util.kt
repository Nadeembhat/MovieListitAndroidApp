package `in`.ernb.diagnalwithkotlin

import android.content.Context
import android.util.DisplayMetrics
import org.json.JSONObject

/**
 * Author Nadeem Bhat ,
 * Created by Nadeem Bhat on Tuesday, May, 2020.
 * Copy Right (c) 11:57 AM.
 * Srinagar,Kashmir
 * ennennbee@gmail.com
 * Diagnal WIth Kotlin
 */


 class Util {
    companion object {
        fun readJsonObjectFromAPI(context: Context, filename: String): JSONObject {
            val json_string = context.assets.open(filename).bufferedReader().use {
                it.readText()
            }
            return JSONObject(json_string)
        }
    fun calculateNoOfColumns(context:Context,columnWidthDP:Float):Int{
        var displayMetrics  = context.resources.displayMetrics
        var screenWidthDp  = displayMetrics.widthPixels / displayMetrics.density
        var noOfColumns = (screenWidthDp / columnWidthDP + 0.5 ).toInt()
        return  noOfColumns
    }
    }

}