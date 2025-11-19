package com.ihsg.dictationapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsg.dictationapp.data.BookDataFactory
import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookVM @Inject constructor(
    private val bookRepository: BookRepository,
    private val bookDataFactory: BookDataFactory
) : ViewModel() {

    private val _booksStateFlow = MutableStateFlow<List<BookEntity>>(emptyList())
    val booksStateFlow = _booksStateFlow.asStateFlow()

    fun load() {
        viewModelScope.launch {
            var books = bookRepository.loadAll().orEmpty()
            if (books.isEmpty()) {
                bookDataFactory.buildData()
            }
            books = bookRepository.loadAll().orEmpty()
            _booksStateFlow.value = books
        }
    }
}