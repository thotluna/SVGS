package ve.com.teeac.svgs.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.repository.AuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import ve.com.teeac.svgs.authentication.domain.use_case.SignInByEmailAndPasswordUseCase
import ve.com.teeac.svgs.authentication.domain.use_case.SignUpByEmailAndPasswordUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providerAuthFirebase(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

//    @DefaultDispatcher
//    @Provides
//    fun providesCoroutineScope(): CoroutineScope {
//        // Run this code when providing an instance of CoroutineScope
//        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
//    }
//
//    @IoDispatcher
//    @Provides
//    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
//
//    @MainDispatcher
//    @Provides
//    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    fun providesAuthRemoteApi(): AuthRemoteUser {
        return AuthRemoteUser(FirebaseAuth.getInstance())
    }

    @Singleton
    @Provides
    fun provideAuthRepository(repositoryRemote: AuthRemoteUser): AuthRepository {
        return AuthRepositoryImpl(repositoryRemote)
    }

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

// @Retention(AnnotationRetention.BINARY)
// @Qualifier
// annotation class DefaultDispatcher
//
// @Retention(AnnotationRetention.BINARY)
// @Qualifier
// annotation class IoDispatcher
//
// @Retention(AnnotationRetention.BINARY)
// @Qualifier
// annotation class MainDispatcher
