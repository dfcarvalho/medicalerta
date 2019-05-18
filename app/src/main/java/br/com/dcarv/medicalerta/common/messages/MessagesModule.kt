package br.com.dcarv.medicalerta.common.messages

import dagger.Binds
import dagger.Module

@Module
abstract class MessagesModule {

    @Binds
    abstract fun bindMessagesRepository(
        messagesRepository: MessagesRepository
    ): Messages.Repository
}