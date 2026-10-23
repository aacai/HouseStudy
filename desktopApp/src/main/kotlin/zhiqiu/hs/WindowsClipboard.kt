package zhiqiu.hs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.NativeClipboard
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard as AwtClipboard
import kotlinx.coroutines.delay

private val isWindows: Boolean =
    System.getProperty("os.name")?.startsWith("Windows", ignoreCase = true) == true

/**
 * Windows 上 AWT 会把本进程上次 [java.awt.datatransfer.Clipboard.setContents] 的内容
 * 留在 `contents` 里。别的程序复制新内容后，清掉这份缓存的事件是异步投到 AWT 队列的，
 * 粘贴如果赶在它前面，读到的就是上一次的文本。
 */
@Composable
fun ProvideWindowsClipboard(content: @Composable () -> Unit) {
    if (!isWindows) {
        content()
        return
    }
    val current = LocalClipboard.current
    val fresh = remember(current) { WindowsFreshClipboard(current) }
    CompositionLocalProvider(LocalClipboard provides fresh, content = content)
}

private class WindowsFreshClipboard(
    private val delegate: Clipboard,
) : Clipboard {
    override suspend fun getClipEntry(): ClipEntry? {
        WindowsClipboardCache.drop()
        var attempt = 0
        while (true) {
            try {
                return delegate.getClipEntry()
            } catch (_: IllegalStateException) {
                // 别的进程正占着剪贴板时 OpenClipboard 会失败，稍等再读
                if (attempt >= 4) return null
                attempt++
                delay(20L * attempt)
                WindowsClipboardCache.drop()
            }
        }
    }

    override suspend fun setClipEntry(clipEntry: ClipEntry?) {
        delegate.setClipEntry(clipEntry)
    }

    @Suppress("DEPRECATION")
    override val nativeClipboard: NativeClipboard
        get() = delegate.nativeClipboard
}

private object WindowsClipboardCache {
    private val contentsField = runCatching {
        AwtClipboard::class.java.getDeclaredField("contents").takeIf { it.trySetAccessible() }
    }.getOrNull()

    private val ownerField = runCatching {
        AwtClipboard::class.java.getDeclaredField("owner").takeIf { it.trySetAccessible() }
    }.getOrNull()

    fun drop() {
        val contents = contentsField ?: return
        val owner = ownerField ?: return
        val clipboard = runCatching { Toolkit.getDefaultToolkit().systemClipboard }.getOrNull() ?: return
        runCatching {
            synchronized(clipboard) {
                contents.set(clipboard, null)
                owner.set(clipboard, null)
            }
        }
    }
}
