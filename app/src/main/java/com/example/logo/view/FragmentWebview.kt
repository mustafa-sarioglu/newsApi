package com.example.logo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.logo.R
import com.example.logo.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_webview.*

class FragmentWebview : Fragment() {

    lateinit var viewModel: NewsViewModel
    val args: FragmentWebviewArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        val article = args.article
        webViewArticleFragment.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
    }

}