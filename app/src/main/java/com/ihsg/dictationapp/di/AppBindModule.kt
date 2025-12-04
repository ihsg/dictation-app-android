package com.ihsg.dictationapp.di

import com.ihsg.dictationapp.model.repository.BookRepository
import com.ihsg.dictationapp.model.repository.BookRepositoryImpl
import com.ihsg.dictationapp.model.repository.GradeRepository
import com.ihsg.dictationapp.model.repository.GradeRepositoryImpl
import com.ihsg.dictationapp.model.repository.LessonRepository
import com.ihsg.dictationapp.model.repository.LessonRepositoryImpl
import com.ihsg.dictationapp.model.repository.RecordRepository
import com.ihsg.dictationapp.model.repository.RecordRepositoryImpl
import com.ihsg.dictationapp.model.repository.WordRepository
import com.ihsg.dictationapp.model.repository.WordRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindModule {

    @Singleton
    @Binds
    abstract fun bindBookRepository(
        bookRepositoryImpl: BookRepositoryImpl
    ): BookRepository


    @Singleton
    @Binds
    abstract fun bindGradeRepository(
        gradeRepositoryImpl: GradeRepositoryImpl
    ): GradeRepository


    @Singleton
    @Binds
    abstract fun bindLessonRepository(
        lessonRepositoryImpl: LessonRepositoryImpl
    ): LessonRepository

    @Singleton
    @Binds
    abstract fun bindWordRepository(
        wordRepositoryImpl: WordRepositoryImpl
    ): WordRepository

    @Singleton
    @Binds
    abstract fun bindRecordRepository(
        recordRepositoryImpl: RecordRepositoryImpl
    ): RecordRepository
}