package com.yangdai.opennote

import android.app.Application
import androidx.core.content.pm.ShortcutManagerCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Remove the legacy dynamic "Compose" shortcut. The supported shortcuts
        // are declared in res/xml/shortcuts.xml so launchers can refresh them reliably.
        ShortcutManagerCompat.removeAllDynamicShortcuts(applicationContext)
    }
}
