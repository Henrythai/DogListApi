package com.example.jetpackapp.ui.detail

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.example.jetpackapp.R
import com.example.jetpackapp.databinding.FragmentDetailBinding
import com.example.jetpackapp.databinding.SendSmsDialogBinding
import com.example.jetpackapp.data.network.model.DogBreed
import com.example.jetpackapp.data.network.model.DogPalette
import com.example.jetpackapp.data.network.model.SmsInfo
import com.example.jetpackapp.ui.base.BaseFragment
import com.example.jetpackapp.ui.Activities.MainActivity

class DetailFragment : BaseFragment<DetailViewModel>() {

    private lateinit var binding: FragmentDetailBinding
    private var dogUuid: Int = 0
    private var sendSMSstarted = false
    private lateinit var currentDog: DogBreed

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        viewModel?.fetch(dogUuid)
        observeViewModel()
    }

    override fun createViewModel(): DetailViewModel {
        val factory = DetailModelFactory(activity?.application!!)
        return ViewModelProvider(this, factory).get(DetailViewModel::class.java)
    }

    fun observeViewModel() {
        viewModel?.dogBreed?.observe(viewLifecycleOwner, Observer {
            it?.let {
                currentDog = it
                binding.dog = it
                setUpBackgroundColor(it.imageUrl)
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
                sendSMSstarted = true
                (activity as MainActivity).checkSmsPermission()
            }
            R.id.action_share -> {
                shareWithOtherApp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun shareWithOtherApp() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog breed")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "${currentDog.dogBreed} bred for ${currentDog.bredFor}"
        )
        intent.putExtra(Intent.EXTRA_STREAM, currentDog.imageUrl)
        startActivity(Intent.createChooser(intent, "Share it"))
    }

    fun onPermissionResult(isPermissionGranted: Boolean) {
        if (sendSMSstarted && isPermissionGranted) {
            context?.let {
                val smsInfo = SmsInfo("", "${currentDog.dogBreed} bred for ${currentDog.bredFor}", currentDog.imageUrl)

                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                    LayoutInflater.from(it),
                    R.layout.send_sms_dialog,
                    null,
                    false
                )

                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") { dialog, wich ->
                        if (!dialogBinding.txtDestination.text.isNullOrEmpty()) {
                            smsInfo.to = dialogBinding.txtDestination.text.toString()
                            sendSMS(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, which -> }
                    .show()

                dialogBinding.smsInfo = smsInfo
            }
        }

    }

    fun sendSMS(smsInfo: SmsInfo) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to, null, smsInfo.text, pendingIntent, null)
    }


}
