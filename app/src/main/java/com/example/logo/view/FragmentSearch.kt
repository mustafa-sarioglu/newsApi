package com.example.logo.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logo.R
import com.example.logo.adapter.NewsAdapter
import com.example.logo.util.Constants.Companion.SEARCH_NEW_TIME_DELAY
import com.example.logo.util.Resource
import com.example.logo.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.indexOf

class FragmentSearch : Fragment() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "SearchNewsFragment"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferencess: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var list = HashSet<String>()
    val liste = HashSet<String>()
    var listArray = arrayOf(String())
    var listArrayEditable = arrayOf(String())
    val args: FragmentSearchArgs by navArgs()
    var dataIn: String? = null
    var sortBy: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = requireActivity().getSharedPreferences("value",Context.MODE_PRIVATE)
        sharedPreferencess = requireActivity().getSharedPreferences("searchIn", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()
        hideProgressBar()
        hideRecyclerView()
        callPreferences()

        dataIn = sharedPreferencess.getString("searchIn","")

        arguments?.let {
            val from = args.data
            Log.e("from",""+from)
            val to = args.dataa
            Log.e("to",""+to)

            if (from != "yyyy-mm-ddT13:58:42Z" && to != "yyyy-mm-ddT13:58:42Z"){
                showRecyclerView()
            }
        }

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_fragmentSearch_to_fragmentArticle, bundle)
        }

        imageButtonFragmentSearchSort.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Sort by")

            val sortByArray = arrayOf("Upload date","Relevance")
            var checkedItem = 0
            var selected = IntArray(1)

            val firstWord = getFirstEditable()

            builder.setSingleChoiceItems(sortByArray, checkedItem) { dialog, which ->
                selected[checkedItem] = which
                Log.e("selected[checkedItem]",""+ selected[checkedItem])
            }.setPositiveButton("OK"){ dialog, which ->
                if (selected[checkedItem] == 0){
                    sortBy = "Upload date"
                    Log.e("sortby",""+ sortBy)

                    viewModel.searchNews(firstWord,dataIn.toString(),sortBy.toString())
                }
                else if (selected[checkedItem] == 1){
                    sortBy = "Relevance"
                    Log.e("sortby",""+ sortBy)
                    viewModel.searchNews(firstWord,dataIn.toString(),sortBy.toString())
                }
            }

            val dialog = builder.create()
            dialog.show()
        }

        var job: Job? = null
        textInputSearchFragment.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEW_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isEmpty()){
                        hideRecyclerView()
                        hideProgressBar()
                        callPreferences()
                    }
                    else if(editable.toString().isNotEmpty()){
                        showRecyclerView()
                        viewModel.searchNews(editable.toString(),dataIn.toString(),sortBy.toString())
                        list.add(editable.toString())
                        editor.putStringSet("list",list)
                        editor.commit()
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        viewModel.searchNewsFromTo.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        imageButtonFilter.setOnClickListener {
            val action = FragmentSearchDirections.actionFragmentSearchToFragmentFilter()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun hideProgressBar() {
        progressBarSearch.visibility = View.INVISIBLE
    }

    private fun showRecyclerView(){
        RVfragmentSearch.visibility = View.VISIBLE
        listViewFragmentSearch.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBarSearch.visibility = View.VISIBLE
    }

    private fun hideRecyclerView(){
        RVfragmentSearch.visibility = View.INVISIBLE
        listViewFragmentSearch.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        RVfragmentSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun callPreferences(){
        val valueList = sharedPreferences.getStringSet("list",liste)
        listArray = valueList!!.toTypedArray()
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,listArray)
        listViewFragmentSearch.adapter = arrayAdapter
    }

    private fun getFirstEditable(): String{
        val valueList = sharedPreferences.getStringSet("list",liste)
        listArrayEditable = valueList!!.toTypedArray()
        val firstValue = listArrayEditable[0]
        return firstValue
    }
}