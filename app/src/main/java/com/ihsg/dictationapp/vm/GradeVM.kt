package com.ihsg.dictationapp.vm

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.db.entity.GradeEntity
import com.ihsg.dictationapp.model.repository.BookRepository
import com.ihsg.dictationapp.model.repository.GradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeVM @Inject constructor(
    private val bookRepository: BookRepository,
    private val gradeRepository: GradeRepository,
) : ViewModel() {

    private val _bookStateFlow = MutableStateFlow<BookEntity?>(null)
    val bookStateFlow = _bookStateFlow.asStateFlow()

    private val _gradesStateFlow = MutableStateFlow<List<GradeEntity>>(emptyList())
    val gradesStateFlow = _gradesStateFlow.asStateFlow()

    fun load(bookId: Long) {
        viewModelScope.launch {
            val bookDeferred = async { bookRepository.loadById(bookId) }
            val gradesDeferred = async { gradeRepository.loadAll(bookId) }
            _bookStateFlow.value = bookDeferred.await()
            _gradesStateFlow.value = gradesDeferred.await().orEmpty()
        }
    }
}