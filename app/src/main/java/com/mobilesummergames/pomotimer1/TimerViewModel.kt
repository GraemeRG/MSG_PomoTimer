package com.mobilesummergames.pomotimer1

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilesummergames.pomotimer1.ui.theme.Purple80
import com.mobilesummergames.pomotimer1.ui.theme.PurpleGrey80
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class TimerViewModel : ViewModel() {
    private lateinit var timer: CountDownTimer
    private var timerLength = 600L
    private var timerState: TimerState = TimerState.STOPPED
    private var secondsRemaining = 0L

    val timeRemainingOutput = MutableLiveData<String>()
    val backgroundColor = MutableLiveData(Purple80)

    fun initTimer() {
        timerState = TimerState.RUNNING
        timer = object : CountDownTimer(timerLength * secondInMills, secondInMills) {
            override fun onTick(timeRemaining: Long) {
                adjustTimeRemaining(timeRemaining)
                updateBackgroundColour()

                Log.d("LISTEN!", "Seconds Remaining: $secondsRemaining")
            }

            override fun onFinish() {
                Log.d("LISTEN!", "FINISHED!")
            }
        }
    }

    fun startTimer() {
        timer.start()
    }

    private fun adjustTimeRemaining(timeRemaining: Long) {
        secondsRemaining = timeRemaining / secondInMills
        val minutesInSecondsRemaining = secondsRemaining / 60
        val secondsInMinuteRemaining = secondsRemaining - minutesInSecondsRemaining * 60
        val asStringValues = Pair(
            formatForDisplay(minutesInSecondsRemaining),
            formatForDisplay(secondsInMinuteRemaining)
        )
        timeRemainingOutput.postValue("${asStringValues.first}:${asStringValues.second}")
    }

    private fun formatForDisplay(timeValue: Long): String {
        val asString = timeValue.toString()
        return if (asString.length < 2) "0$asString" else asString
    }

    private fun updateBackgroundColour() {
        backgroundColor
            .postValue(
                DateTime
                    .now()
                    .let {
                        Color(
                            red = it.hourOfDay,
                            green = it.minuteOfHour,
                            blue = it.secondOfMinute
                        )
                    }
            )
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