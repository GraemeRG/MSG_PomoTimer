package com.mobilesummergames.pomotimer1

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilesummergames.pomotimer1.TimerState.*
import com.mobilesummergames.pomotimer1.ui.theme.PomoTimer1Theme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    val timerViewModel: TimerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomoTimer1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainTimer(timerViewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        timerViewModel.initTimer()
        timerViewModel.startTimer()
    }
}

class TimerViewModel : ViewModel() {
    lateinit var timer: CountDownTimer
    var timerLength = 600L
    var timerState: TimerState = STOPPED
    var secondsRemaining = 0L
    val timeRemainingOutput = MutableLiveData<String>()

    fun initTimer() {
        timerState = RUNNING
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

enum class TimerState {
    STOPPED, PAUSED, RUNNING
}

@Composable
fun MainTimer(viewModel: TimerViewModel) {
    val timeRemaining = viewModel.timeRemainingOutput.observeAsState()
    Text(text = "${timeRemaining.value}")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val timerViewModel = TimerViewModel()
    timerViewModel.timeRemainingOutput.postValue("10:00")
    PomoTimer1Theme {
        MainTimer(timerViewModel)
    }
}