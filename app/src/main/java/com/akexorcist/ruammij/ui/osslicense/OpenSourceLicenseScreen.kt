package com.akexorcist.ruammij.ui.osslicense

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.akexorcist.ruammij.R
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.util.withJson

@Composable
fun OpenSourceLicenseRoute(
    navController: NavController,
) {
    OpenSourceLicenseScreen(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OpenSourceLicenseScreen(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.about_app_menu_open_source_licenses),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        LibrariesContainer(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
            librariesBlock = { ctx ->
                Libs.Builder()
                    .withJson(ctx, R.raw.aboutlibraries)
                    .build()
            },
        )
    }
}