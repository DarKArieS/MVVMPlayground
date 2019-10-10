package com.app.aries.mvvmplayground.model.speedtest.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.aries.mvvmplayground.CustomApplication
import timber.log.Timber
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files.exists




@Database(entities = [SpeedTestPosition::class], version = 1, exportSchema = false)
abstract class SpeedTestPositionDatabase  : RoomDatabase() {
    companion object {
        @Volatile private var INSTANCE: SpeedTestPositionDatabase? = null

        fun getInstance(): SpeedTestPositionDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(CustomApplication.getInstance()).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                SpeedTestPositionDatabase::class.java, "speedtest.db")
                .build().apply{
                    initDatabase(context,"speedtest.db")
                }
    }

    abstract fun speedTestPositionDao(): SpeedTestPositionDAO

    private fun initDatabase(context: Context, databaseName: String) {
        val dbPath = context.getDatabasePath(databaseName)

        Timber.tag("stpDatabase").d("copy database")

        // If the database already exists, return
        if (dbPath.exists()) {
            Timber.tag("stpDatabase").d("we already have such file $databaseName !")
            return
        }

        // Make sure we have a path to the file
        dbPath.parentFile.mkdirs()

        // Try to copy database file
        try {
            val inputStream = context.assets.open("107_speedtest_pos.db")
            val output = FileOutputStream(dbPath)

            val buffer = ByteArray(8192)
            var length: Int = inputStream.read(buffer, 0, 8192)

            while ((length) > 0) {
                Timber.tag("stpDatabase").d("copying, $length")
                output.write(buffer, 0, length)
                length = inputStream.read(buffer, 0, 8192)
            }

            output.flush()
            output.close()
            inputStream.close()
        } catch (e: Throwable) {
            Timber.tag("stpDatabase").d("failed to copy speedtest database")
            Timber.tag("stpDatabase").d("$e")
        }


    }

}