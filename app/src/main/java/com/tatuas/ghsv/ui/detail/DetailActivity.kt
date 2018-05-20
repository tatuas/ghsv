package com.tatuas.ghsv.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.tatuas.ghsv.R
import com.tatuas.ghsv.ext.buildViewModel
import com.tatuas.ghsv.ext.observeNonNull

class DetailActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_NICKNAME = "nickname"

        fun newIntent(context: Context, nickname: String) = Intent(context, DetailActivity::class.java)
                .apply { putExtra(EXTRA_NICKNAME, nickname) }
    }

    private val viewModel by buildViewModel(DetailViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.titleLiveData.observeNonNull(this, { supportActionBar?.title = it })

        viewModel.initialize(intent.getStringExtra(EXTRA_NICKNAME))
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> true.also { finish() }
        else -> super.onOptionsItemSelected(item)
    }
}
