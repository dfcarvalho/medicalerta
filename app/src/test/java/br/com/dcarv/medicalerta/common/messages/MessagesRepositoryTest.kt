package br.com.dcarv.medicalerta.common.messages

import android.content.Context
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class MessagesRepositoryTest {

    @MockK
    lateinit var context: Context

    lateinit var messagesRepository: MessagesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { context.applicationContext } returns context
        messagesRepository = MessagesRepository(context)
    }

    @Test
    fun test_get_success() {
        // given
        val mockMessage = "MESSAGE"
        every { context.getString(Messages.Key.GENERIC_ERROR_MESSAGE.resId) } returns mockMessage

        // when
        val message = messagesRepository.get(Messages.Key.GENERIC_ERROR_MESSAGE)

        // then
        assertEquals(mockMessage, message)
    }
}