package com.pedro.helipagospayment.di

import com.pedro.helipagospayment.features.paymentrequest.data.api.PaymentApi
import com.pedro.helipagospayment.features.paymentrequest.data.repository.PaymentRepository
import com.pedro.helipagospayment.features.paymentrequest.data.repository.PaymentRepositoryImpl
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