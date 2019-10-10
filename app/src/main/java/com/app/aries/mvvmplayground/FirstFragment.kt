package com.app.aries.mvvmplayground


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.aries.mvvmplayground.model.speedtest.persistence.SpeedTestPositionDatabase
import com.app.aries.mvvmplayground.model.weather.persistence.DataStatus
import com.app.aries.mvvmplayground.viewmodel.FirstViewModel
import com.app.aries.mvvmplayground.viewmodel.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_first.view.*
import timber.log.Timber

class FirstFragment : Fragment() {
    private lateinit var rootView: View

    private val viewModelFactory = ViewModelFactory()
    private lateinit var firstViewModel : FirstViewModel

    override fun onStart() {
        Timber.tag("FirstViewModel").d("onStart")
        checkViewModel()
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag("FirstViewModel").d("onCreate")
        //checkViewModel()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.tag("FirstViewModel").d("onCreateView")
        rootView = inflater.inflate(R.layout.fragment_first, container, false)

        firstViewModel = ViewModelProviders
            .of(this,viewModelFactory)
            .get(FirstViewModel::class.java)

        checkViewModel()

        firstViewModel.weatherData.observe(this, Observer{
            Timber.tag("FirstViewModel").d("we get data from Repository and update UI! $it")
            when (it.dataResource){
                (DataStatus.FromCache)->{
                    rootView.showSource.text = "from cache"
                    rootView.showWeather.text = it.weather
                }

                (DataStatus.FromNetwork)->{
                    rootView.showSource.text = "from network"
                    rootView.showWeather.text = it.weather
                    rootView.progressBar.visibility = View.INVISIBLE
                }

                (DataStatus.NetworkError)->{
                    rootView.progressBar.visibility = View.INVISIBLE
                }

                else->{
                    rootView.showSource.text = "From ..."
                    rootView.showWeather.text = "???"
                }
            }
        })

        firstViewModel.listData.observe(this,Observer{
            Timber.tag("FirstViewModel").d("listData modified!")
        })

        rootView.getWeatherButton.setOnClickListener {
            firstViewModel.getWeather()
            rootView.progressBar.visibility = View.VISIBLE
        }


        rootView.testButton.setOnClickListener {
//            val db = SpeedTestPositionDatabase.getInstance()
////            a.initDatabase(this.context!!,"speedtest.db")
//            db.speedTestPositionDao().getAll()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe {result->
//                Timber.tag("stpDatabase").d("$result")
//            }

            checkViewModel()
            rootView.showSource.text = "click Button"
            //rootView.showSource.isSaveEnabled = true
            rootView.showSource.freezesText = true

            firstViewModel.modifyListData()
        }

        return rootView
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Timber.tag("FirstViewModel").d("onViewStateRestored")
        checkViewModel()
        super.onViewStateRestored(savedInstanceState)
    }

    private fun checkViewModel(){
        if(null!=firstViewModel)
            Timber.tag("FirstViewModel").d("check View Model: ${firstViewModel.weatherData.value.toString()}")
    }


    companion object {
        @JvmStatic
        fun newInstance() = FirstFragment()
    }
}
