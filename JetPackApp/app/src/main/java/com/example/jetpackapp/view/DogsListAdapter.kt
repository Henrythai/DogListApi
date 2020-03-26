package com.example.jetpackapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackapp.R
import com.example.jetpackapp.model.DogBreed
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(val DogList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() {

    class DogViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvId = v.dogId
        val tvDogName = v.dogName
        val tvLifeSpan = v.dogLifeSpan
        val imageDog = v.dogImage
    }

    fun updateDogList(newDogList: List<DogBreed>) {
        DogList.clear()
        DogList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(v)
    }

    override fun getItemCount(): Int = DogList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val item = DogList[position]
        holder.tvDogName.text = item.dogBreed
        holder.tvLifeSpan.text = item.lifeSpan
        holder.itemView.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it).navigate(ListFragmentDirections.actionDetailFragment())
        })
    }
}