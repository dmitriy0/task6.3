package com.example.task63

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import java.math.BigDecimal

@OptIn(DelicateCoroutinesApi::class)
class ForegroundFragment : Fragment() {

    private lateinit var job: Job
    private lateinit var textPi: TextView
    private var k = 1.0
    private var sum = BigDecimal(0.0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @OptIn(InternalCoroutinesApi::class)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_foreground, container, false)

        textPi = view.findViewById<TextView>(R.id.text_pi)
        job = Job()

        parentFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            when (bundle.getString("state")!!) {

                "start" -> {
                    job = GlobalScope.launch(Dispatchers.IO){
                        countPi().collect {}
                    }

                }

                "stop" -> {
                    job.cancel()
                }

                "reset" -> {
                    job.cancel()
                    job = GlobalScope.launch(Dispatchers.IO) {
                        k = 1.0
                        sum = BigDecimal(0.0)
                        countPi().collect {}
                    }
                }
            }
        }

        return view
    }

    private fun countPi() = flow {
        emit(0)

        while(job.isActive){

            if (k % 2 != 0.0) {
                sum += BigDecimal(4.0).divide(BigDecimal(k*2 - 1), 1000, 0)
            } else {
                sum -= BigDecimal(4.0).divide(BigDecimal(k*2 - 1), 1000, 0)
            }

            if (k % 1000 == 0.0) {
                withContext(Dispatchers.Main){
                    textPi.text = sum.toString()
                }
            }

            k += 1.0
        }
    }

}