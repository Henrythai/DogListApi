package com.example.jetpackapp.ui.list


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackapp.R
import com.example.jetpackapp.databinding.ItemDogBinding
import com.example.jetpackapp.data.network.model.DogBreed
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(val DogList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(),
    DogClickListener {


    fun updateDogList(newDogList: List<DogBreed>) {
        DogList.clear()
        DogList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v: ItemDogBinding =
            DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)
        return DogViewHolder(
            v
        )
    }

    override fun getItemCount(): Int = DogList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val item = DogList[position]
        holder.binding.dog = item
        holder.binding.listenter = this
    }

    override fun onDogClicked(v: View) {
        val uuid = v.dogId.text.toString().toInt()
        Navigation.findNavController(v)
            .navigate(
                ListFragmentDirections.actionDetailFragment(
                    uuid
                )
            )
    }

    class DogViewHolder(val binding: ItemDogBinding) : RecyclerView.ViewHolder(binding.root)


}