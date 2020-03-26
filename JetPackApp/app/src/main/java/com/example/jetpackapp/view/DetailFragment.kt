package com.example.jetpackapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation

import com.example.jetpackapp.R
import com.example.jetpackapp.model.DogBreed
import com.example.jetpackapp.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {

    private lateinit var viewmodel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel = ViewModelProvider(this).get(DetailViewModel::class.java)

        viewmodel.fetch()

        arguments?.let {
//            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
//            textView2.text = dogUuid.toString()


        }

//        btnList.setOnClickListener(View.OnClickListener {
//            val action = DetailFragmentDirections.actionListFragment()
//            Navigation.findNavController(it).navigate(action)
//        })

        observeViewModel()
    }

    fun observeViewModel() {
        viewmodel.dogBreed.observe(viewLifecycleOwner, Observer {
            it?.let {
                dogDetailsName.text = it.dogBreed
                dogDetailLifeSpan.text = it.lifeSpan
                dogDetailsPurporse.text = it.bredFor
                dogDetailsTemperament.text = it.temperament
            }
        })
    }

}
