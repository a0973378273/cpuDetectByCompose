package com.bean.cpucompose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bean.cpucompose.ui.theme.CpuComposeTheme

@Composable
fun Content(commandViewModel: CommandViewModel = viewModel()) {
    val uiState by commandViewModel.topCommandFlow.collectAsState()
    var second by remember { mutableStateOf("10") }
    var cpuUsageRate by remember { mutableStateOf("10") }
    CpuComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(Modifier.padding(10.dp)) {
                Button(onClick = { commandViewModel.sendIntent(CpuIntent.StartCommand) }) {
                    Text(text = "開始偵測")
                }
                Button(onClick = { commandViewModel.sendIntent(CpuIntent.StopCommand) }) {
                    Text(text = "結束偵測")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "持續幾秒 : ",
                        Modifier.weight(1f)
                    )
                    TextField(
                        value = second,
                        onValueChange = {
                            second = it
                            runCatching {
                                commandViewModel.sendIntent(CpuIntent.SetContinuousSecond(second.toInt()))
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Cpu使用率 : ",
                        Modifier.weight(1f)
                    )
                    TextField(
                        value = cpuUsageRate,
                        onValueChange = {
                            cpuUsageRate = it
                            runCatching {
                                commandViewModel.sendIntent(
                                    CpuIntent.SetCpuUsageLimitRate(
                                        cpuUsageRate.toInt()
                                    )
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                uiState.cpuUsageRateList.forEachIndexed { index, i ->
                    Text(text = "CPU $index : $i")
                }
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "CPU average rate: ${uiState.cpuUsageRate} %"
                )
                if (uiState.trigger) {
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = "CPU is trigger",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Box {
                Text(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    text = "只要小於 Cpu 使用率，即會停止偵測",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Content()
}