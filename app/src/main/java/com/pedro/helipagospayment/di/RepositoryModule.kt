package com.pedro.helipagospayment.di

import com.pedro.helipagospayment.features.paymentrequests.data.api.PaymentApi
import com.pedro.helipagospayment.features.paymentrequests.data.repository.PaymentRepositoryImpl
import com.pedro.helipagospayment.features.paymentrequests.domain.repository.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePaymentRepository(
        paymentApi: PaymentApi
    ): PaymentRepository = PaymentRepositoryImpl(paymentApi)

}