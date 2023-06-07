package com.bean.cpucompose

import java.io.BufferedReader
import java.io.InputStreamReader

class CpuUtil {
    val cpuCore: Int
    val cpuMaxFrequency: List<Int>

    init {
        cpuCore = getNumberOfCores()
        cpuMaxFrequency = getMaxFrequency()
    }

    fun getCurFrequency(): List<Int> {
        val command = arrayListOf("/system/bin/cat")
        for (i in 0 until cpuCore) {
            command.add("/sys/devices/system/cpu/cpu$i/cpufreq/scaling_cur_freq")
        }
        return runProcess(command.toTypedArray()).map { it.toInt() }.toList()
    }

    private fun getMaxFrequency(): List<Int> {
        val command = arrayListOf("/system/bin/cat")
        for (i in 0 until cpuCore) {
            command.add("/sys/devices/system/cpu/cpu$i/cpufreq/cpuinfo_max_freq")
        }
        return runProcess(command.toTypedArray()).map { it.toInt() }.toList()
    }

    private fun getNumberOfCores(): Int = Runtime.getRuntime().availableProcessors()

    private fun runProcess(command: Array<String>): List<String> {
        val process = Runtime.getRuntime().exec(command)
        val inputStream = process.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))
        val output = ArrayList<String>()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            output.add(line!!)
        }
        reader.close()
        return output
    }
}