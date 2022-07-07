package com.mobilesummergames.pomotimer1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mobilesummergames.pomotimer1.ui.theme.PomoTimer1Theme
import com.mobilesummergames.pomotimer1.ui.theme.PurpleGrey80
import com.mobilesummergames.pomotimer1.ui.theme.dancingScript
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val timerViewModel: TimerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomoTimer1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PurpleGrey80
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MainTimer(timerViewModel)
                    }
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

@Composable
fun MainTimer(viewModel: TimerViewModel, modifier: Modifier = Modifier) {
    val timeRemaining = viewModel.timeRemainingOutput.observeAsState()
    Text(text = "${timeRemaining.value}", fontSize = 52.sp, fontFamily = dancingScript, fontWeight = FontWeight.Bold)
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