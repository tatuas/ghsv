package com.tatuas.ghsv.ui.detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateUtils
import android.text.format.DateUtils.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tatuas.ghsv.R
import com.tatuas.ghsv.data.api.GitHubDetailUser
import com.tatuas.ghsv.data.glide.GlideApp
import com.tatuas.ghsv.databinding.FragmentDetailBinding
import com.tatuas.ghsv.ext.*
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = buildDataBinding(R.layout.fragment_detail, inflater, container)
        viewModel = buildViewModel(DetailViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailLiveData.observeNonNull(this, {
            GlideApp.with(activity!!)
                    .loadDefault(it.avatar_url)
                    .centerCrop()
                    .into(binding.detail.icon)

            binding.detail.displayName.text = it.name ?: getString(R.string.empty)

            binding.detail.bio.text =
                    if (!it.bio.isNullOrEmpty()) getString(R.string.detail_format_bio, it.bio)
                    else null

            binding.detail.infomation.text = formatToInformationFrom(it)

            GlideApp.with(activity!!)
                    .loadGlass(it.login)
                    .fitCenter()
                    .into(binding.detail.glass)
        })

        viewModel.stateLiveData.observe(this, Observer {
            when (it) {
                DetailViewModel.State.Loading -> {
                    binding.state.setText(R.string.loading)
                    binding.detail.root.toGone()
                }
                DetailViewModel.State.Loaded -> {
                    binding.state.text = null
                    binding.detail.root.toVisible()
                }
                DetailViewModel.State.Offline -> {
                    binding.state.setText(R.string.offline)
                    binding.detail.root.toGone()
                }
                else -> {
                    binding.state.setText(R.string.error)
                    binding.detail.root.toGone()
                }
            }
        })
    }

    private fun convertToFriendlyDateTextFrom(rawDateText: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        sdf.timeZone = TimeZone.getDefault()

        return DateUtils.formatDateTime(context, sdf.parse(rawDateText).time,
                FORMAT_SHOW_YEAR or FORMAT_SHOW_DATE or FORMAT_SHOW_WEEKDAY or FORMAT_ABBREV_ALL)
    }

    private fun formatToInformationFrom(user: GitHubDetailUser): String {
        val empty = getString(R.string.empty)

        val information = mutableListOf<String>()

        information.add(getString(R.string.detail_location, user.location ?: empty))
        information.add(getString(R.string.detail_company, user.company ?: empty))
        information.add(getString(R.string.detail_email, user.email ?: empty))

        information.add(getString(R.string.detail_public_repos, user.public_repos))
        information.add(getString(R.string.detail_public_gists, user.public_gists))
        information.add(getString(R.string.detail_following, user.following))
        information.add(getString(R.string.detail_followers, user.followers))

        information.add(getString(R.string.detail_created_at, convertToFriendlyDateTextFrom(user.created_at)))
        information.add(getString(R.string.detail_updated_at, convertToFriendlyDateTextFrom(user.updated_at)))

        return information.joinToString(separator = "\n")
    }
}
