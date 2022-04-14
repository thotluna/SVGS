package ve.com.teeac.svgs.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ve.com.teeac.svgs.authentication.data.data_source.OAuthFirebase
import ve.com.teeac.svgs.authentication.data.data_source.OAuthRemoteUser
import ve.com.teeac.svgs.authentication.data.repository.OAuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.OAuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOAuthRemoteUser(oAuth: OAuthFirebase): OAuthRemoteUser

    @Binds
    @Singleton
    abstract fun bindOAuthRepostory(oAuthRepositoryImpl: OAuthRepositoryImpl): OAuthRepository
}
