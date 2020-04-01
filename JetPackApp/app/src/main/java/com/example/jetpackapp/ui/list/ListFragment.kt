package com.example.jetpackapp.ui.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.jetpackapp.R
import com.example.jetpackapp.data.network.service.DogsApiService
import com.example.jetpackapp.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : BaseFragment<ListViewModel>() {
    private val doglistadapter =
        DogsListAdapter(arrayListOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel?.refresh()

        refreshLayout.setOnRefreshListener {
            dogsList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel?.refreshBypassCache()
            refreshLayout.isRefreshing = false
        }

        dogsList?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = doglistadapter
        }

        observeViewModel()

    }

    override fun createViewModel(): ListViewModel {
        val factory = ListViewModelFactory(activity?.application!!, DogsApiService())
        return ViewModelProvider(this, factory).get(ListViewModel::class.java)
    }

    fun observeViewModel() {
        viewModel?.dogs?.observe(viewLifecycleOwner, Observer {
            it?.let {
                dogsList.visibility = View.VISIBLE
                doglistadapter.updateDogList(it)
            }
        })

        viewModel?.dogsLoadError?.observe(viewLifecycleOwner, Observer {
            it?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel?.loading?.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    dogsList.visibility = View.GONE
                    listError.visibility = View.GONE
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> view?.let {
                Navigation.findNavController(it)
                    .navigate(ListFragmentDirections.actionListFragmentToSettingsFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }


}