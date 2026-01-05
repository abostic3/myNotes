package com.consumer.notesapp

import android.app.Application
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

class CrashHandler : Application() {
    override fun onCreate() {
        super.onCreate()
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val ts = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US).format(Date())
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                throwable.printStackTrace(pw)
                val text = "Timestamp: $ts\nThread: ${thread.name}\nException:\n${sw.toString()}"

                val f = File(filesDir, "crash_logs.txt")
                f.appendText(text + "\n\n---\n\n")
            } catch (t: Throwable) {
                // ignore write failures
            }
            // pass to default handler to let system handle kill/report
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
