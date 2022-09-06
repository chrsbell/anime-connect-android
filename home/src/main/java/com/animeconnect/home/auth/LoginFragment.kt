package com.animeconnect.home.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.animeconnect.home.NetworkUtils
import com.animeconnect.home.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var preferences: PreferencesRepository
    private lateinit var networkUtils: NetworkUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        networkUtils = NetworkUtils(binding.webView)
        preferences = PreferencesRepository(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        authenticateUser()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun goToWelcome() {
        binding.webView.visibility = View.GONE
        binding.loginButton.visibility = View.GONE
        Toast.makeText(context, "Logged In!", Toast.LENGTH_SHORT).show()
    }

    private fun authenticateUser() {
        // if (!preferences.getAccessToken().isNullOrEmpty()) {
        //     goToWelcome()
        // } else {
            binding.webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest
                ): Boolean {
                    if (request.isRedirect &&
                        request.url.toString().startsWith(NetworkUtils.CALLBACK_URL)
                    ) {
                        binding.webView.destroy()
                        goToWelcome()
                    }
                    // disable non-mal links?
                    // add progress bar
                    return false
                }

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    val cookie = networkUtils.getAndDecodeCookie()
                    if (cookie != null) {
                        preferences.saveAccessToken(cookie)
                    }
                    return null
                }
            }
            binding.webView.settings.javaScriptEnabled = true
            binding.loginButton.setOnClickListener {
                binding.webView.visibility = View.VISIBLE
                binding.loginButton.visibility = View.GONE
                binding.webView.loadUrl(NetworkUtils.LOGIN_URL);
            }
        // }
    }
}