package br.com.dcarv.medicalerta.medlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import br.com.dcarv.medicalerta.RxSchedulersRule
import br.com.dcarv.medicalerta.common.authentication.Authentication
import br.com.dcarv.medicalerta.common.messages.Messages
import br.com.dcarv.medicalerta.common.model.User
import br.com.dcarv.medicalerta.common.ui.ErrorMessage
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

private const val USER_ID = "USER_ID"
private const val USER_EMAIL = "USER_EMAIL"
private const val USER_NAME = "USER_NAME"
private const val AUTH_ERROR_MSG = "AUTH_ERROR_MSG"

class MedicationListViewModelTest {

    @get:Rule
    val rxSchedulersRule = RxSchedulersRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var authManager: Authentication.Manager

    @MockK
    private lateinit var messagesRepository: Messages.Repository

    @InjectMockKs
    private lateinit var viewModel: MedicationListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test_onActivityCreated_success() {
        // given
        val fragment = mockk<Fragment>(relaxed = true)
        val user = User(USER_ID, USER_EMAIL, USER_NAME)
        every { authManager.authenticateIfNecessary(fragment) } returns Single.just(user)

        // when
        viewModel.onActivityCreated(fragment)

        // then
        viewModel.showProgressBar.observeForever {
            assertEquals(false, it)
        }

        viewModel.showError.observeForever {
            assertEquals(ErrorMessage.Hidden, it)
        }
    }

    @Test
    fun test_onActivityCreated_fail() {
        // given
        val fragment = mockk<Fragment>(relaxed = true)
        every { authManager.authenticateIfNecessary(fragment) } returns Single.error(Exception("test"))
        every { messagesRepository.get(Messages.Key.AUTHENTICATION_ERROR_MESSAGE) } returns AUTH_ERROR_MSG

        // when
        viewModel.onActivityCreated(fragment)

        // then
        viewModel.showProgressBar.observeForever {
            assertEquals(false, it)
        }

        viewModel.showError.observeForever {
            assertEquals(ErrorMessage.Displayed(AUTH_ERROR_MSG), it)
        }
    }
}