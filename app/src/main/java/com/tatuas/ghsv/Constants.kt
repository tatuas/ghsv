package com.tatuas.ghsv

import java.util.concurrent.Executor
import java.util.concurrent.Executors

object AppExecutors {
    @JvmField
    val PAGING: Executor = Executors.newFixedThreadPool(4)
}
