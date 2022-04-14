package ve.com.teeac.svgs.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.data_source.OAuthFirebase
import ve.com.teeac.svgs.authentication.data.data_source.OAuthRemoteUser
import ve.com.teeac.svgs.authentication.data.repository.AuthRepositoryImpl
import ve.com.teeac.svgs.authentication.data.repository.OAuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import ve.com.teeac.svgs.authentication.domain.repositories.OAuthRepository
import ve.com.teeac.svgs.authentication.domain.use_case.OAuthUseCase
import ve.com.teeac.svgs.authentication.domain.use_case.SignInByEmailAndPasswordUseCase
import ve.com.teeac.svgs.authentication.domain.use_case.SignUpByEmailAndPasswordUseCase
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providerAuthFirebase(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

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

    @AuthTwitter
    @Provides
    fun provideOAuthProviderTwitter(): OAuthProvider {
        return OAuthProvider.newBuilder("twitter.com")
            .build()
    }

    @Singleton
    @Provides
    fun providesOAuthFirebase(
        firebaseAuth: FirebaseAuth,
        @AuthTwitter oAuthProvider: OAuthProvider
    ): OAuthFirebase {
        return OAuthFirebase(firebaseAuth, oAuthProvider)
    }

    @Singleton
    @Provides
    fun providesOAuthRepositoryImpl(
        oAuth: OAuthRemoteUser
    ): OAuthRepositoryImpl {
        return OAuthRepositoryImpl(oAuth)
    }

    @Provides
    fun providesOAuthUseCase(
        repository: OAuthRepository
    ): OAuthUseCase {
        return OAuthUseCase(repository)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthTwitter
