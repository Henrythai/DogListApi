package com.example.jetpackapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation

import com.example.jetpackapp.R
import com.example.jetpackapp.model.DogBreed
import com.example.jetpackapp.util.getProgressDrawable
import com.example.jetpackapp.util.loadImage
import com.example.jetpackapp.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {

    private lateinit var viewmodel: DetailViewModel
    private var dogUuid: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }
        viewmodel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewmodel.fetch(dogUuid)
        observeViewModel()
    }

    fun observeViewModel() {
        viewmodel.dogBreed.observe(viewLifecycleOwner, Observer {
            it?.let {
                dogDetailsImage.loadImage(it.imageUrl)
                dogDetailsName.text = it.dogBreed
                dogDetailLifeSpan.text = it.lifeSpan
                dogDetailsPurporse.text = it.bredFor
                dogDetailsTemperament.text = it.temperament

//                context?.let { context ->
//                    dogDetailsImage.loadImage(it.imageUrl, getProgressDrawable(context))
//                }
            }
        })
    }

}
