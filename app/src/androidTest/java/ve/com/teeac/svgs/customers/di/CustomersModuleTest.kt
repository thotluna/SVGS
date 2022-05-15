package ve.com.teeac.svgs.customers.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ve.com.teeac.svgs.customers.data.data_source.CustumerFirestore
import ve.com.teeac.svgs.customers.data.data_source.SystemFirestore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CustomersModuleTest {

    @Singleton
    @Provides
    fun providerFirestore(): FirebaseFirestore {
        val db = FirebaseFirestore.getInstance()

        db.useEmulator("10.0.2.2", 8080)

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        db.firestoreSettings = settings
        return db
    }

    @Singleton
    @Provides
    fun providerCustomerRemoteDataSource(firestore: FirebaseFirestore): CustumerFirestore {
        return CustumerFirestore(firestore)
    }

    @Singleton
    @Provides
    fun providerSystemRemoteDataSource(firestore: FirebaseFirestore): SystemFirestore {
        return SystemFirestore(firestore)
    }
}
