package com.tatuas.ghsv.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tatuas.ghsv.R
import com.tatuas.ghsv.databinding.FragmentMainBinding
import com.tatuas.ghsv.ext.buildDataBinding
import com.tatuas.ghsv.ext.buildViewModel
import com.tatuas.ghsv.ext.observeNonNull
import com.tatuas.ghsv.ext.observeNullable
import com.tatuas.ghsv.ui.detail.DetailActivity

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: MainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = buildDataBinding(R.layout.fragment_main, inflater, container)
        viewModel = buildViewModel(MainViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MainAdapter(activity!!).also { adapter ->
            adapter.onItemClick = { startActivity(DetailActivity.newIntent(activity!!, it)) }
        }

        binding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(activity!!)
            it.addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
            it.setHasFixedSize(true)
        }

        binding.refresh.also {
            it.setColorSchemeResources(R.color.colorAccent)
            it.setOnRefreshListener { viewModel.refreshUserList() }
        }

        viewModel.userListLiveData.observeNullable(this) { adapter.submitList(it) }

        viewModel.stateLiveData.observeNonNull(this) {
            when (it) {
                MainViewModel.State.Loading -> {
                    binding.state.setText(R.string.loading)
                }
                MainViewModel.State.Loaded -> {
                    binding.refresh.isRefreshing = false
                    binding.state.text = null
                }
                MainViewModel.State.Offline -> {
                    binding.refresh.isRefreshing = false
                    binding.state.setText(R.string.offline)
                }
                else -> {
                    binding.refresh.isRefreshing = false
                    binding.state.setText(R.string.error)
                }
            }
        }

        viewModel.initializeUserList()
    }
}
