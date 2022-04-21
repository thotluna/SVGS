package ve.com.teeac.svgs.authentication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import ve.com.teeac.svgs.authentication.domain.use_case.SignInByEmailAndPasswordUseCase
import ve.com.teeac.svgs.authentication.domain.use_case.SignUpByEmailAndPasswordUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignUseCaseModule {

    @Singleton
    @Provides
    fun provideSignUpUseCase(repository: AuthRepository): SignUpByEmailAndPasswordUseCase {
        return SignUpByEmailAndPasswordUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSignInUseCase(repository: AuthRepository): SignInByEmailAndPasswordUseCase {
        return SignInByEmailAndPasswordUseCase(repository)
    }
}
