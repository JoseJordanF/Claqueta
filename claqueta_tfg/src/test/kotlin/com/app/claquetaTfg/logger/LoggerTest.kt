package com.app.claquetaTfg.logs


import io.mockk.mockk
import io.mockk.every
import io.mockk.just
import io.mockk.Runs
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoggerTest{

    private lateinit var logger: Logger
    private lateinit var loggerInfoMock: org.slf4j.Logger
    private lateinit var loggerDebugMock: org.slf4j.Logger

    @BeforeEach
    fun onBefore() {
        System.setProperty("CLAQUETA_LOGBACK_CONFIG","src/main/resources/configLogB.xml")
        logger = Logger.instance()

        //Logger mock
        loggerInfoMock =  mockk<org.slf4j.Logger>()
        loggerDebugMock =  mockk<org.slf4j.Logger>()

        //Behavioural Mock
        every { loggerInfoMock.info(any()) } just Runs
        every { loggerDebugMock.debug(any()) } just Runs
    }

    @Test
    fun `When logging at info level`(){

        //When
        logger.logger = loggerInfoMock
        logger.info("Test message at info level")

        //Then
        verify(exactly = 1) { loggerInfoMock.info(any()) }
    }

    @Test
    fun `When logging at debug level`(){

        //When
        logger.logger = loggerDebugMock
        logger.debug("Test message at debug level")

        //Then
        verify(exactly = 1) { loggerDebugMock.debug(any()) }
    }
}