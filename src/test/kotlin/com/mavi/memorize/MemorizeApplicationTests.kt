package com.mavi.memorize

import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.SpringApplication

@ExtendWith(MockKExtension::class)
class MemorizeApplicationTests {

    @Test
    fun contextLoads() {
        val args = arrayOf("arg1", "arg2")

        mockkStatic(SpringApplication::class)
        every { SpringApplication.run(MemorizeApplication::class.java, *args) } returns mockk()

        main(args)

        verify(exactly = 1) {
            SpringApplication.run(MemorizeApplication::class.java, *args)
        }
        unmockkStatic(SpringApplication::class)
    }

}
