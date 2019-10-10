package com.app.aries.mvvmplayground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.MapFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    init {
        Timber.tag("lifecycle").d("MainActivity init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupFragment()
    }

    private fun setupFragment(){
        val homeFrag = MapsFragment

        val homeFragment = supportFragmentManager.findFragmentByTag(homeFrag::class.java.name)

        if(null == homeFragment){
            Timber.tag("FirstViewModel").d("create a new home fragment")

            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.FragmentContainer,
                homeFrag.newInstance(),
                homeFrag::class.java.name
            )
            transaction.commit()
        }
    }
}
