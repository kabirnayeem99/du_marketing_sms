package io.github.kabirnayeem99.dumarketingstudent.presentation.fragments

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.common.util.Constants.DU_WEBSITE_URL
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentWebViewBinding
import io.github.kabirnayeem99.dumarketingstudent.presentation.MainActivity
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


/*
Refer to: Enhance Android WebView Performance using Glide by Mudit Sen
https://proandroiddev.com/enhance-android-webview-performance-using-glide-aba4bbc41bc7
 */
@AndroidEntryPoint
class WebViewFragment : BaseFragment<FragmentWebViewBinding>() {


    override val layoutRes: Int
        get() = R.layout.fragment_web_view

    lateinit var webView: WebView


    override fun onCreated(savedInstanceState: Bundle?) {
        hideBottomNavBar()
        showWebsite()
    }

    private fun showWebsite() {
        webView = WebView(requireContext())
        webView.settings.apply {
            javaScriptEnabled = true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webView.apply {
                webChromeClient = getChromeClient()
            }
        }

        webView.apply {
            webViewClient = getClient()
        }

        binding.wvWebsite.apply {
            loadUrl(DU_WEBSITE_URL)
            webViewClient = WebViewClient()
        }
    }

    private fun getChromeClient(): WebChromeClient {
        return object : WebChromeClient() {

        }
    }

    private fun getBitmapInputStream(
        bitmap: Bitmap,
        compressFormat: Bitmap.CompressFormat
    ): InputStream {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(compressFormat, 80, byteArrayOutputStream)
        val bitmapData: ByteArray = byteArrayOutputStream.toByteArray()
        return ByteArrayInputStream(bitmapData)
    }

    private fun hideBottomNavBar() {
        (activity as MainActivity).bottomNavBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).bottomNavBar.visibility = View.VISIBLE
    }

    private fun getClient(): WebViewClient {
        return object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                url: String?
            ): WebResourceResponse? {
                if (url == null) {
                    return super.shouldInterceptRequest(view, url as String)
                }
                return if (url.toLowerCase(Locale.ROOT)
                        .contains(".jpg") || url.toLowerCase(Locale.ROOT).contains(".jpeg")
                ) {
                    val bitmap =
                        Glide.with(webView).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                            .load(url).submit().get()
                    WebResourceResponse(
                        "image/jpg", "UTF-8", getBitmapInputStream(
                            bitmap,
                            Bitmap.CompressFormat.JPEG
                        )
                    )
                } else if (url.toLowerCase(Locale.ROOT).contains(".png")) {
                    val bitmap =
                        Glide.with(webView).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                            .load(url).submit().get()
                    WebResourceResponse(
                        "image/png", "UTF-8", getBitmapInputStream(
                            bitmap,
                            Bitmap.CompressFormat.PNG
                        )
                    )
                } else if (url.toLowerCase(Locale.ROOT)
                        .contains(".webp") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
                ) {
                    val bitmap =
                        Glide.with(webView).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                            .load(url).submit().get()
                    WebResourceResponse(
                        "image/webp", "UTF-8", getBitmapInputStream(
                            bitmap,
                            Bitmap.CompressFormat.WEBP_LOSSY
                        )
                    )
                } else {
                    super.shouldInterceptRequest(view, url)
                }

            }
        }
    }
}