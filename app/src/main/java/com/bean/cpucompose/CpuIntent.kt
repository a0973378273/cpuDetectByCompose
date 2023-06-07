package com.bean.cpucompose

sealed class CpuIntent{
    object StartCommand : CpuIntent()
    object StopCommand : CpuIntent()
    class SetContinuousSecond(val continuousSecond: Int) : CpuIntent()
    class SetCpuUsageLimitRate(val cpuUsageLimitRate : Int) : CpuIntent()
}
