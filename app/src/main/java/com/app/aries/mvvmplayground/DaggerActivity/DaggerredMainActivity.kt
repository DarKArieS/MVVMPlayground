package com.app.aries.mvvmplayground.DaggerActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.aries.mvvmplayground.DaggerActivity.di.*
import com.app.aries.mvvmplayground.DaggerActivity.viewmodel.DaggerredMainViewModel
import com.app.aries.mvvmplayground.DaggerActivity.viewmodel.DaggerredViewModelFactory
import com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeShop
import com.app.aries.mvvmplayground.R
import kotlinx.android.synthetic.main.dagger_activity_main.*
import javax.inject.Inject

class DaggerredMainActivity : AppCompatActivity() {

    init {
        Log.d("lifecycle","DaggerMainActivity init")
    }

    @Inject
    @field:DaggerredMainViewModelFactory
    lateinit var viewModelFactory: DaggerredViewModelFactory

    private lateinit var viewModel: DaggerredMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("lifecycle","DaggerMainActivity onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dagger_activity_main)

        setupViewModel()
        showMessageButton.setOnClickListener { clickButton() }
    }

    private fun setupViewModel(){
        DaggerMainActivityComponent.builder().mainActivityModule(
            MainActivityModule(
                "yup"
            )
        ).build().inject(this)

        //AndroidInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(DaggerredMainViewModel::class.java)

        viewModel.helloString.observe(this, Observer{
            Log.d("livedata","observer: helloString updated! $it")
            showTextView.text = it
        })
    }

    private fun clickButton(){
        viewModel.updateMessage()
        showTextView2.text = "i'm updated!"
        CoffeeShop.getCoffee()
    }
}
