package com.ihsg.dictationapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.db.entity.GradeEntity
import com.ihsg.dictationapp.model.db.entity.LessonEntity
import com.ihsg.dictationapp.model.repository.BookRepository
import com.ihsg.dictationapp.model.repository.GradeRepository
import com.ihsg.dictationapp.model.repository.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonVM @Inject constructor(
    private val bookRepository: BookRepository,
    private val gradeRepository: GradeRepository,
    private val lessonRepository: LessonRepository,
) : ViewModel() {

    private val _bookStateFlow = MutableStateFlow<BookEntity?>(null)
    val bookStateFlow = _bookStateFlow.asStateFlow()

    private val _gradeStateFlow = MutableStateFlow<GradeEntity?>(null)
    val gradeStateFlow = _gradeStateFlow.asStateFlow()

    private val _lessonsStateFlow = MutableStateFlow<List<LessonEntity>>(emptyList())
    val lessonsStateFlow = _lessonsStateFlow.asStateFlow()

    fun load(bookId: Long, gradeId: Long) {
        viewModelScope.launch {
            val bookDeferred = async { bookRepository.loadById(bookId) }
            val gradeDeferred = async { gradeRepository.loadById(bookId, gradeId) }
            val lessonsDeferred = async { lessonRepository.loadAll(bookId, gradeId) }
            _bookStateFlow.value = bookDeferred.await()
            _gradeStateFlow.value = gradeDeferred.await()
            _lessonsStateFlow.value = lessonsDeferred.await().orEmpty()
        }
    }
}