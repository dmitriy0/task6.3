package com.example.task63

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import androidx.constraintlayout.widget.ConstraintLayout

class BackgroundFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_background, container, false)

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.foreground_container, ForegroundFragment())
            .commit()

        val chronometer = view.findViewById<Chronometer>(R.id.chronometer)
        val buttonStart = view.findViewById<Button>(R.id.button_start_stop)
        val buttonReset = view.findViewById<Button>(R.id.button_reset)
        val background = view.findViewById<ConstraintLayout>(R.id.background)

        var stopTime = 0L

        buttonStart.setOnClickListener{
            if (buttonStart.text == "Start"){

                buttonStart.text = "Stop"

                val bundle = Bundle()
                bundle.putString("state","start")
                fragmentManager.setFragmentResult("requestKey", bundle)

                chronometer.base = SystemClock.elapsedRealtime() + stopTime
                chronometer.start()

            } else {

                buttonStart.text = "Start"

                val bundle = Bundle()
                bundle.putString("state","stop")
                fragmentManager.setFragmentResult("requestKey", bundle)

                stopTime = chronometer.base - SystemClock.elapsedRealtime()
                chronometer.stop()

            }
        }

        buttonReset.setOnClickListener {

            buttonStart.text = "Stop"

            val bundle = Bundle()
            bundle.putString("state","reset")
            fragmentManager.setFragmentResult("requestKey", bundle)

            chronometer.base = SystemClock.elapsedRealtime()
            stopTime = 0L
            chronometer.start()

        }

        chronometer.setOnChronometerTickListener {
            val time = SystemClock.elapsedRealtime() - chronometer.base
            if ((time % 20000 <= 1000) && time > 1000){
                background.setBackgroundColor(Color.rgb((0..255).random(),(0..255).random(),(0..255).random()))
            }
        }

        return view
    }
}