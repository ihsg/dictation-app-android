package com.ihsg.dictationapp.model.log

import android.util.Log.DEBUG
import android.util.Log.ERROR
import android.util.Log.INFO
import android.util.Log.VERBOSE
import android.util.Log.WARN
import android.util.Log.isLoggable
import timber.log.Timber

class Logger(private val classOrTag: Any) {
    private val tag by lazy {
        "#Dictation" + if (classOrTag is String) {
            classOrTag
        } else {
            classOrTag.javaClass.simpleName
        }
    }
    private val isDebugVersion by lazy { true }
    private val isVerboseEnabled by lazy { isDebugVersion || isLoggable("", VERBOSE) }
    private val isInfoEnabled by lazy { isDebugVersion || isLoggable("", INFO) }
    private val isDebugEnabled by lazy { isDebugVersion || isLoggable("", DEBUG) }
    private val isWarnEnabled by lazy { isDebugVersion || isLoggable("", WARN) }
    private val isErrorEnabled by lazy { isDebugVersion || isLoggable("", ERROR) }


    fun v(message: () -> String) {
        if (isVerboseEnabled) {
            Timber.tag(tag).v(message.invoke())
        }
    }

    fun i(message: () -> String) {
        if (isInfoEnabled) {
            Timber.tag(tag).i(message.invoke())
        }
    }

    fun d(message: () -> String) {
        if (isDebugEnabled) {
            Timber.tag(tag).d(message.invoke())
        }
    }

    fun w(message: () -> String) {
        if (isWarnEnabled) {
            Timber.tag(tag).w(message.invoke())
        }
    }

    fun e(message: () -> String) {
        if (isErrorEnabled) {
            Timber.tag(tag).e(message.invoke())
        }
    }
}