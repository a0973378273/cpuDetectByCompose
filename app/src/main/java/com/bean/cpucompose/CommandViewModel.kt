package com.bean.cpucompose

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow

class CommandViewModel : ViewModel() {
    private val intent = Channel<CpuIntent>()
    private val _topCommandFlow = MutableStateFlow(UiState())
    val topCommandFlow = _topCommandFlow.asStateFlow()

    private val cpuUtil = CpuUtil()
    private var second = 10
    private var cpuLimitRete = 10
    private var runJob: Job? = null

    init {
        handleIntent()
    }

    fun sendIntent(topIntent: CpuIntent) = viewModelScope.launch {
        intent.send(topIntent)
    }

    private fun handleIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            intent.consumeAsFlow().collect {
                when (it) {
                    is CpuIntent.StartCommand -> {
                        runJob?.cancel()
                        runJob = launch { runTopCommand() }
                    }
                    is CpuIntent.StopCommand -> runJob?.cancel()
                    is CpuIntent.SetContinuousSecond -> second = it.continuousSecond
                    is CpuIntent.SetCpuUsageLimitRate -> cpuLimitRete = it.cpuUsageLimitRate
                }
            }
        }
    }

    private suspend fun runTopCommand() = withContext(Dispatchers.Unconfined) {
        for (s in 0 until second) {
            val cupRateArrayList = arrayListOf<Int>()
            var cupRateAverage = 0
            var trigger = false
            cpuUtil.getCurFrequency().forEachIndexed { index, i ->
                cupRateArrayList.add(i.times(100).div(cpuUtil.cpuMaxFrequency[index]))
            }
            cupRateArrayList.forEach {
                cupRateAverage += it
            }
            cupRateAverage.div(cpuUtil.cpuCore).also { cupRateAverage = it }
            if (cupRateAverage >= cpuLimitRete) { // 達到標準
                if (s == second.dec()) { // 達到次數
                    trigger = true
                }
                _topCommandFlow.value =
                    UiState(
                        cpuUsageRateList = cupRateArrayList,
                        cpuUsageRate = cupRateAverage,
                        trigger = trigger
                    )
            } else { // 不達標準則取消
                _topCommandFlow.value =
                    UiState(
                        cpuUsageRateList = cupRateArrayList,
                        cpuUsageRate = cupRateAverage,
                        trigger = false
                    )
                break
            }
            delay(1000)
        }
    }
}