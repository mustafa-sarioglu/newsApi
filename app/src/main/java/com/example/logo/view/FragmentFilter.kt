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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.logo.R
import com.example.logo.util.Resource
import com.example.logo.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FragmentFilter : Fragment() {

    lateinit var viewModel: NewsViewModel
    val TAG = "FilterNewsFragment"
    val args: FragmentFilterArgs by navArgs()
    var dataIn: String? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        sharedPreferences = requireActivity().getSharedPreferences("searchIn", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        dataIn = sharedPreferences.getString("searchIn","")
        Log.e("dataInFilterFragmnet",""+dataIn)
        textViewFragmentFilter.text = dataIn
        /*arguments?.let {
            dataIn = args.dataInn
            Log.e("dataInFilterFragmnet",""+dataIn)
        }*/

        toolbarFragmentFilter.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_fragmentFilter_to_fragmentSearch)
        }

        imageButtonFragmentFilterClear.setOnClickListener {
            editTextFromFragmentFilter.text?.clear()
            editTextToFragmentFilter.text?.clear()
            textViewFragmentFilter.text = ""
            editor.clear().commit()
        }

        var job: Job? = null
        buttonApplyFilter.setOnClickListener {
            job?.cancel()
            job = MainScope().launch {

                if (editTextFromFragmentFilter.toString().isNotEmpty() && editTextToFragmentFilter.toString().isNotEmpty()){
                    val bundle = Bundle().apply {

                        val from = editTextFromFragmentFilter.text.toString()
                        val to = editTextToFragmentFilter.text.toString()
                        Log.e("FilterFrom",""+from)
                        Log.e("FilterTo",""+to)

                        putSerializable("data",from+"T13:58:42Z")
                        putSerializable("dataa",to+"T13:58:42Z")
                        putSerializable("dataIn",dataIn)

                        viewModel.searchNewsFromTo(from+"T13:58:42Z",to+"T13:58:42Z",dataIn.toString())
                    }
                    Log.e("bundle",""+bundle)
                    findNavController().navigate(R.id.action_fragmentFilter_to_fragmentSearch,bundle)
                }
            }
        }

        buttonSearchIn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentFilter_to_fragmentSearchIn)
        }
    }
}