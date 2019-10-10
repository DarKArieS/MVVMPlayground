package com.app.aries.mvvmplayground.model.weather.network

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord")        var coord:Coordinate,
    @SerializedName("main")         var main:Main,
    @SerializedName("wind")         var wind:Wind,
    @SerializedName("dt")           var dt:Long,
    @SerializedName("sys")          var sys:Sys,
    @SerializedName("id")           var id:Double,
    @SerializedName("name")         var name:String,
    @SerializedName("cod")          var cod:Float,
    @SerializedName("weather")      var weather:List<Weather>
){
    data class Coordinate(
        @SerializedName("lon")      var lon : Float,
        @SerializedName("lat")      var lat :Float
    )

    data class Main(
        @SerializedName("temp")     var temp : Float,
        @SerializedName("pressure") var pressure : Float,
        @SerializedName("humidity") var humidity : Float,
        @SerializedName("temp_min") var temp_min : Float,
        @SerializedName("temp_max") var temp_max : Float
    )

    data class Wind(
        @SerializedName("speed")    var speed : Float,
        @SerializedName("deg")      var deg : Float
    )

    data class Clouds(
        @SerializedName("all")      var all : Float
    )

    data class Sys(
        @SerializedName("country")  var country : String
    )

    data class Weather(
        @SerializedName("id")             var id : Int,
        @SerializedName("main")           var main : String,
        @SerializedName("description")    var description : String,
        @SerializedName("icon")           var icon : String
    )

}