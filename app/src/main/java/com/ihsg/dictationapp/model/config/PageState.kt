package com.ihsg.dictationapp.model.config


sealed class PageState {

    /**
     * the initialization state for the page.
     */
    data object Initial : PageState()

    /**
     * the loading state is started for the page, a loading will be shown.
     */
    data object LoadingStart : PageState()

    /**
     * the loading state is ended for the page, a loading will be hided.
     */
    data object LoadingEnd : PageState()

    /**
     * the success state for the page, a successful message will be shown.
     */
    data class Success(val message: String) : PageState()

    /**
     * the error state for the page, a error message will be shown.
     */
    data class Error(val throwable: Throwable) : PageState()

    /**
     * the finish state for the page which will be finished immediately.
     */
    data object Finish : PageState()
}
