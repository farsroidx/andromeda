@file:Suppress("unused")

package ir.farsroidx.andromeda.ui.ktx

import android.webkit.WebView

/**
 * Loads a local HTML file from the assets folder into a WebView.
 *
 * @param filePath The path of the HTML file within the 'assets' folder.
 *                 It should be a relative path starting from the 'assets' folder.
 */
fun WebView.loadHtmlFromAssets(filePath: String) = this.loadUrl("file:///android_asset/$filePath")

fun WebView.loadHtmlString(html: String) = this.loadData(html, "text/html; charset=utf-8", "UTF-8")
