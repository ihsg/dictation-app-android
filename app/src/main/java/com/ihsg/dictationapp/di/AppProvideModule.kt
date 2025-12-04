package com.ihsg.dictationapp.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.ihsg.dictationapp.model.db.AppDatabase
import com.ihsg.dictationapp.model.db.dao.BookDao
import com.ihsg.dictationapp.model.db.dao.GradeDao
import com.ihsg.dictationapp.model.db.dao.LessonDao
import com.ihsg.dictationapp.model.db.dao.RecordDao
import com.ihsg.dictationapp.model.db.dao.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppProvideModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = databaseBuilder(context, AppDatabase::class.java, "data.db")
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton
    fun provideBookDao(
        appDatabase: AppDatabase
    ): BookDao = appDatabase.bookDao()

    @Provides
    @Singleton
    fun provideGradeDao(
        appDatabase: AppDatabase
    ): GradeDao = appDatabase.gradeDao()


    @Provides
    @Singleton
    fun provideLessonDao(
        appDatabase: AppDatabase
    ): LessonDao = appDatabase.lessonDao()

    @Provides
    @Singleton
    fun provideWordDao(
        appDatabase: AppDatabase
    ): WordDao = appDatabase.wordDao()

    @Provides
    @Singleton
    fun provideRecordDao(
        appDatabase: AppDatabase
    ): RecordDao = appDatabase.recordDao()
}