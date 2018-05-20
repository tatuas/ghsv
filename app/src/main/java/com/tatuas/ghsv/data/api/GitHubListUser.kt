package com.tatuas.ghsv.data.api

data class GitHubListUser(val login: String,
                          val id: Long,
                          val avatar_url: String,
                          val html_url: String,
                          val type: String)
