package com.bean.cpucompose

data class UiState(
    val cpuUsageRateList: List<Int> = listOf(),
    val cpuUsageRate: Int = 0,
    val trigger: Boolean = false
)