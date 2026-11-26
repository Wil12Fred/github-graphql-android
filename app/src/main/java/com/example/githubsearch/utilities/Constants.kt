package com.example.githubsearch.utilities

import com.example.githubsearch.BuildConfig

object Constants {

    const val ASSETS_PATH = "file:///android_asset/"
    const val HERO_ASSETS_PATH = ASSETS_PATH + "hero/"
    const val SWAPI_BASE_URL = "https://swapi.co/api/"
    // GitHub token, injected at build time (see README / .env.example). Never commit it.
    val AUTH_APOLLO: String = BuildConfig.GITHUB_TOKEN
    const val BASE_URL = "https://api.github.com/graphql"
}
