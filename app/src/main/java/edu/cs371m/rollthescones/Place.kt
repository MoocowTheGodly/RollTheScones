package edu.cs371m.rollthescones

import com.google.android.gms.common.api.Result
import com.google.gson.annotations.SerializedName

data class Place (
    @SerializedName("html_attributions")
    val html_attributions: ArrayList<Object>,
    @SerializedName("results")
    val results: ArrayList<Restaurant>,
    @SerializedName("status")
    val status: String
)