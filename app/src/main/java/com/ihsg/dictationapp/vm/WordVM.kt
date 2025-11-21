package com.ihsg.dictationapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.db.entity.GradeEntity
import com.ihsg.dictationapp.model.db.entity.LessonEntity
import com.ihsg.dictationapp.model.db.entity.WordEntity
import com.ihsg.dictationapp.model.repository.BookRepository
import com.ihsg.dictationapp.model.repository.GradeRepository
import com.ihsg.dictationapp.model.repository.LessonRepository
import com.ihsg.dictationapp.model.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordVM @Inject constructor(
    private val bookRepository: BookRepository,
    private val gradeRepository: GradeRepository,
    private val lessonRepository: LessonRepository,
    private val wordRepository: WordRepository,
) : ViewModel() {

    private val _bookStateFlow = MutableStateFlow<BookEntity?>(null)
    val bookStateFlow = _bookStateFlow.asStateFlow()

    private val _gradeStateFlow = MutableStateFlow<GradeEntity?>(null)
    val gradeStateFlow = _gradeStateFlow.asStateFlow()

    private val _lessonStateFlow = MutableStateFlow<LessonEntity?>(null)
    val lessonStateFlow = _lessonStateFlow.asStateFlow()

    private val _wordsStateFlow = MutableStateFlow<List<WordEntity>>(emptyList())
    val wordsStateFlow = _wordsStateFlow.asStateFlow()

    fun load(bookId: Long, gradeId: Long, lessonId: Long) {
        viewModelScope.launch {
            val bookDeferred = async { bookRepository.loadById(bookId) }
            val gradeDeferred = async { gradeRepository.loadById(bookId, gradeId) }
            val lessonDeferred = async { lessonRepository.loadById(bookId, gradeId, lessonId) }
            val wordsDeferred = async { wordRepository.loadAll(bookId, gradeId, lessonId) }

            _bookStateFlow.value = bookDeferred.await()
            _gradeStateFlow.value = gradeDeferred.await()
            _lessonStateFlow.value = lessonDeferred.await()
            _wordsStateFlow.value = wordsDeferred.await().orEmpty()
        }
    }
}