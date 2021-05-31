package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentWebViewBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.activities.MainActivity
import io.github.kabirnayeem99.dumarketingstudent.util.Constants.DU_WEBSITE_URL
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


/*
Refer to: Enhance Android WebView Performance using Glide by Mudit Sen
https://proandroiddev.com/enhance-android-webview-performance-using-glide-aba4bbc41bc7
 */
class WebViewFragment : Fragment() {
    private var _binding: FragmentWebViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        _binding = null
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