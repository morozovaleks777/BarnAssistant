package com.example.barnassistant.di

import android.content.Context
import androidx.room.Room
import com.example.barnassistant.data.room.AppRoomDatabase
import com.example.barnassistant.data.room.RoomDao
import com.example.barnassistant.data.room.RoomRepositoryImpl
import com.example.barnassistant.domain.repository.FireRepository
import com.example.barnassistant.domain.repository.RoomRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideFireBookRepository()
            = FireRepository(queryBook = FirebaseFirestore.getInstance()
        .collection("books"))



        @Singleton
        @Provides
        fun provideRoomDao(appRoomDatabase: AppRoomDatabase): RoomDao
                = appRoomDatabase.barnListDao()

        @Singleton
        @Provides
       fun provideRoomRepository(roomRepositoryImpl: RoomRepositoryImpl): RoomRepository {
           return roomRepositoryImpl
       }



    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext
                           context: Context
    ):AppRoomDatabase =
        Room.databaseBuilder(
            context,AppRoomDatabase::class.java,
            "barn_database"
        ).fallbackToDestructiveMigration()
            .build()

}