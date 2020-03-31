package com.example.jetpackapp.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.icu.text.CaseMap
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.example.jetpackapp.R
import com.example.jetpackapp.databinding.FragmentDetailBinding
import com.example.jetpackapp.model.DogBreed
import com.example.jetpackapp.model.DogPalette
import com.example.jetpackapp.util.getProgressDrawable
import com.example.jetpackapp.util.loadImage
import com.example.jetpackapp.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {

    private lateinit var viewmodel: DetailViewModel
    private lateinit var binding: FragmentDetailBinding
    private var dogUuid: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentDetailBinding>(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        )

        return binding.root
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
                binding.dog = it
                setUpBackgroundColor(it.imageUrl)
//                dogDetailsImage.loadImage(it.imageUrl)
////                dogDetailsName.text = it.dogBreed
////                dogDetailLifeSpan.text = it.lifeSpan
////                dogDetailsPurporse.text = it.bredFor
////                dogDetailsTemperament.text = it.temperament


//                context?.let { context ->
//                    dogDetailsImage.loadImage(it.imageUrl, getProgressDrawable(context))
//                }
            }
        })
    }

    private fun setUpBackgroundColor(uri: String?) {
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColor = palette?.vibrantSwatch?.rgb ?: 0
                            val myPallte = DogPalette(intColor)
                            binding.palette = myPallte

                        }
                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_sms -> {

            }
            R.id.action_share -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
