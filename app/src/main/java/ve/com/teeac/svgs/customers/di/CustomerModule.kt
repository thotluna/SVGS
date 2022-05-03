package ve.com.teeac.svgs.customers.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ve.com.teeac.svgs.customers.data.data_source.CustomerRemoteDataSource

@Module
@InstallIn(ViewModelComponent::class)
object CustomerModule {

    @ViewModelScoped
    @Provides
    fun providerFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @ViewModelScoped
    @Provides
    fun providerCustomerRemoteDataSource(firestore: FirebaseFirestore): CustomerRemoteDataSource {
        return CustomerRemoteDataSource(firestore)
    }
}
