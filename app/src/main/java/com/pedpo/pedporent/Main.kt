package com.pedpo.pedporent

import kotlinx.coroutines.*

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            main()
        }

        //sampleStart
//        fun main()  = runBlocking{
//            doWorld()
//        }

        var acquired = 0

        class Resource {
            init {
                ++acquired

            } // Acquire the resource
            fun close() { acquired-- } // Release the resource
        }

        fun main() = runBlocking {
            repeat(100_000) { // launch a lot of coroutines
                launch {
                    delay(5000L)
                    print(".")
                }
            }
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun somethingUsefulOneAsync() = GlobalScope.async {
            doSomethingUsefulOne()
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun somethingUsefulTwoAsync() = GlobalScope.async {
            doSomethingUsefulTwo()
        }

        suspend fun doSomethingUsefulOne(): Int {
            delay(1000L) // pretend we are doing something useful here
            return 13
        }

        suspend fun doSomethingUsefulTwo(): Int {
            delay(1000L) // pretend we are doing something useful here, too
            return 29
        }

        // Concurrently executes both sections
        suspend fun doWorld() = coroutineScope { // this: CoroutineScope
            val job = launch { // launch a new coroutine and keep a reference to its Job
                delay(1000L)
                println("World!")
            }
            println("Hello")
            job.join() // wait until child coroutine completes
            println("Done")
        }

    }


}