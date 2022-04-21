package ve.com.teeac.svgs.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteFirebase
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.repository.AuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providerAuthFirebase(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun providesAuthRemoteFirebase(auth: FirebaseAuth): AuthRemoteFirebase {
        return AuthRemoteFirebase(auth)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(repositoryRemote: AuthRemoteUser): AuthRepository {
        return AuthRepositoryImpl(repositoryRemote)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRemoteUserModule {

    @Binds
    abstract fun bindAuthRemoteUser(authRemoteFirebase: AuthRemoteFirebase): AuthRemoteUser
}
