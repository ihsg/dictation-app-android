package com.ihsg.dictationapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsg.dictationapp.model.config.PageState
import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookVM @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private val logger = Logger(this)

    private val _pageStateFlow = MutableStateFlow<PageState>(PageState.Initial)
    val pageStateFlow = _pageStateFlow.asStateFlow()

    fun add(bookName: String) {
        viewModelScope.launch {
            if (bookName.isBlank()) {
                logger.e { "the book name is null or empty" }
                return@launch
            }
            bookRepository.add(BookEntity(name = bookName))
            _pageStateFlow.value = PageState.Finish
        }
    }
}