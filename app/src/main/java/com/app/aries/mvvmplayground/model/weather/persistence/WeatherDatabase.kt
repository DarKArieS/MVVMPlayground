package com.app.aries.mvvmplayground.model.weather.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [WeatherData::class], version = 1, exportSchema = false)
abstract class WeatherDatabase  : RoomDatabase() {
    companion object {
        @Volatile private var INSTANCE: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                WeatherDatabase::class.java, "weather.db")
                .build()
    }

    abstract fun weatherDao(): WeatherDAO

}