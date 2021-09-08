package com.example.logo.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.logo.R
import com.example.logo.util.Resource
import com.example.logo.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_search_in.*

class FragmentSearchIn : Fragment() {

    lateinit var viewModel: NewsViewModel
    val TAG = "SearchInNewsFragment"
    var titles: Boolean = false
    var contents: Boolean = false
    var descriptions: Boolean = false
    var list: ArrayList<String> = arrayListOf()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = requireActivity().getSharedPreferences("searchIn", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        imageButtonFragmentSearchInClear.setOnClickListener {
            switchTitle.isChecked = false
            switchDescription.isChecked = false
            switchContent.isChecked = false
            editor.clear().commit()
        }

        switchTitle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                titles = true
                list.add("title")
            }else{
                titles = false
                list.remove("title")
            }
        }

        switchDescription.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                descriptions = true
                list.add("description")
            }else{
                descriptions = false
                list.remove("description")
            }
        }

        switchContent.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                contents = true
                list.add("content")
            }else{
                contents = false
                list.remove("content")
            }
        }

        buttonApplyFilter.setOnClickListener {
            val output:String = convertString(list)
            Log.e("a",""+output)
            /*val bundle = Bundle().apply {
                putSerializable("output",output)
            }
            Log.e("bundle",""+bundle)*/
            editor.putString("searchIn",output)
            editor.commit()
            findNavController().navigate(R.id.action_fragmentSearchIn_to_fragmentFilter)
        }

        viewModel.searchNewsIn.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { newsResponse ->
                    }
                }
                is Resource.Error -> {
                    response.message?.let {message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                }
            }
        })
    }

    fun convertString(list: ArrayList<String>): String{
        val separator = ","
        val output = list.joinToString(separator)
        Log.e("output",""+output)
        return output
    }
}