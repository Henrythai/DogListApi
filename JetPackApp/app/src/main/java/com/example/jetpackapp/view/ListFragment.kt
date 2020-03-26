package com.example.jetpackapp.view

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.jetpackapp.R
import com.example.jetpackapp.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewmodel: ListViewModel
    private val doglistadapter = DogsListAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        btnDetail.setOnClickListener(View.OnClickListener {
//            val action = ListFragmentDirections.actionDetailFragment(5)
//            Navigation.findNavController(it).navigate(action)
//        })

        viewmodel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewmodel.refresh()

        dogsList?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = doglistadapter
        }

        observeViewModel()

    }

    fun observeViewModel() {
        viewmodel.dogs.observe(viewLifecycleOwner, Observer {
            it?.let {
                doglistadapter.updateDogList(it)
            }
        })

        viewmodel.dogsLoadError.observe(viewLifecycleOwner, Observer {
            it?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewmodel.loading.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    dogsList.visibility = View.GONE
                    listError.visibility = View.GONE
                }
            }
        })

    }
}