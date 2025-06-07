package com.example.cooksy.data.model
import android.R.string
import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName ("original") val original:string)
