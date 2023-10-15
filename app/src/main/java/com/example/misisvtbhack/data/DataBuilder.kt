package com.example.misisvtbhack.data

import android.content.Context
import com.google.gson.Gson

class DataBuilder {

    companion object{
        fun getOfficeData(context: Context): Array<Office> {
            val jsonText = context.assets.open("offices.json").bufferedReader().use { it.readText() }
            return Gson().fromJson(jsonText, Array<Office>::class.java)
        }

        fun getTAtmsData(context: Context): Array<Atm> {
            val jsonText = context.assets.open("atms.json").bufferedReader().use { it.readText() }
            return Gson().fromJson(jsonText, Array<Atm>::class.java)
        }
    }

}