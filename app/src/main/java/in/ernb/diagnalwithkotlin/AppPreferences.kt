package `in`.ernb.diagnalwithkotlin

import android.content.SharedPreferences

/**
 * Author Nadeem Bhat ,
 * Created by Nadeem Bhat on Tuesday, May, 2020.
 * Copy Right (c) 11:31 AM.
 * Srinagar,Kashmir
 * ennennbee@gmail.com
 * Diagnal WIth Kotlin
 */


class AppPreferences(val sharedPreferences: SharedPreferences) {


    fun getorientation() : Int {
        return sharedPreferences.getInt("orientation", 0)
    }

    fun  setOrientation (orientation: Int) {
        sharedPreferences.edit().putInt("orientation", orientation).apply();
    }

}