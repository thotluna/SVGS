package ve.com.teeac.svgs.authentication.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ve.com.teeac.svgs.authentication.data.data_source.OAuthFirebase
import ve.com.teeac.svgs.authentication.data.data_source.OAuthRemoteUser
import ve.com.teeac.svgs.authentication.data.repository.OAuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.OAuthRepository
import ve.com.teeac.svgs.authentication.domain.use_case.OAuthUseCase
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object OAuthModule {

    @ViewModelScoped
    @AuthTwitter
    @Provides
    fun provideOAuthProviderTwitter(): OAuthProvider {
        return OAuthProvider.newBuilder("twitter.com")
            .build()
    }

    @ViewModelScoped
    @Provides
    fun providesOAuthFirebase(
        firebaseAuth: FirebaseAuth,
        @AuthTwitter oAuthProvider: OAuthProvider
    ): OAuthRemoteUser {
        return OAuthFirebase(firebaseAuth, oAuthProvider)
    }

    @ViewModelScoped
    @Provides
    fun providesOAuthRepositoryImpl(
        oAuth: OAuthRemoteUser
    ): OAuthRepository {
        return OAuthRepositoryImpl(oAuth)
    }

    @ViewModelScoped
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
