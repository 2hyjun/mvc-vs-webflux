package com.example.nonblocking

import kotlinx.coroutines.*
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class NonblockingApplication

fun main(args: Array<String>) {
    runApplication<NonblockingApplication>(*args)
}

fun executor(poolSize: Int) = ThreadPoolTaskExecutor()
        .also {
            it.corePoolSize = poolSize
            it.maxPoolSize = poolSize
            it.setThreadNamePrefix("thread-")
//        it.setQueueCapacity(queueSize)
            it.setAwaitTerminationSeconds(3)
            it.setWaitForTasksToCompleteOnShutdown(true)
//        it.setTaskDecorator(MdcTaskDecorator())
            it.initialize()
        }

suspend fun io() {
    delay(3000)
    delay(3000)
    delay(3000)
}


@RestController
class Controller {
    private val logger = KotlinLogging.logger { }
    val executor32 = executor(32).asCoroutineDispatcher()
    val executor64 = executor(64).asCoroutineDispatcher()

    @GetMapping("/executor/32")
    suspend fun executor32() = withContext(executor32) {
        val startTime = System.currentTimeMillis()
        logger.info { "start $startTime"}
        io()
        logger.info { "end $startTime"}
    }

    @GetMapping("/executor/64")
    suspend fun executor64() = withContext(executor64) {
        val startTime = System.currentTimeMillis()
        logger.info { "start $startTime"}
        io()
        logger.info { "end $startTime"}
    }

    @GetMapping("/io")
    suspend fun ios() {
        val startTime = System.currentTimeMillis()
        logger.info { "prepare $startTime"}
        withContext(Dispatchers.IO) {

            logger.info { "start $startTime"}
            io()
            logger.info { "end $startTime"}
        }
    }

    @GetMapping("/sleep")
    fun sleep() {
        Thread.sleep(3000)
        Thread.sleep(3000)
        Thread.sleep(3000)
    }

}