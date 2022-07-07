package com.mobilesummergames.pomotimer1

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    lateinit var timer: CountDownTimer
    var timerLength = 600L
    var timerState: TimerState = TimerState.STOPPED
    var secondsRemaining = 0L
    val timeRemainingOutput = MutableLiveData<String>()

    fun initTimer() {
        timerState = TimerState.RUNNING
        timer = object : CountDownTimer(timerLength * secondInMills, secondInMills) {
            override fun onTick(timeRemaining: Long) {
                secondsRemaining = timeRemaining / secondInMills
                val minutesInSecondsRemaining = secondsRemaining / 60
                val secondsInMinuteRemaining = secondsRemaining - minutesInSecondsRemaining * 60
                val asStringValues = Pair(
                    formatForDisplay(minutesInSecondsRemaining),
                    formatForDisplay(secondsInMinuteRemaining)
                )
                timeRemainingOutput.postValue("${asStringValues.first}:${asStringValues.second}")

                Log.d("LISTEN!", "Seconds Remaining: $secondsRemaining")
            }

            override fun onFinish() {
                Log.d("LISTEN!", "FINISHED!")
            }
        }
    }

    private fun formatForDisplay(timeValue: Long): String {
        val asString = timeValue.toString()
        return if (asString.length < 2) "0$asString" else asString
    }

    fun startTimer() {
        timer.start()
    }

    private fun stopTimer() {
        timer.cancel()
        timerState = TimerState.STOPPED
    }

    private fun pauseTimer() {
        timer.cancel()
        timerState = TimerState.PAUSED
    }

    companion object {
        private const val secondInMills = 1000L
    }
}