package com.akexorcist.ruammij.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akexorcist.ruammij.R
import com.akexorcist.ruammij.common.Installer
import com.akexorcist.ruammij.common.InstallerVerificationStatus
import com.akexorcist.ruammij.data.InstalledApp
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import com.akexorcist.ruammij.utility.DarkLightPreviews
import com.akexorcist.ruammij.utility.toReadableDatetime
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppInfoContent(
    app: InstalledApp,
    onAppInfoClick: () -> Unit,
) {
    SectionCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAppInfoClick() },
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.padding(top = 4.dp)) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = rememberDrawablePainter(drawable = app.icon),
                    contentDescription = stringResource(R.string.description_app_icon, app.name),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        BoldBodyText(
                            text = app.name,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        BodyText(text = app.packageName, color = MaterialTheme.colorScheme.onBackground)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
                if (app.systemApp) {
                    Spacer(modifier = Modifier.height(6.dp))
                    SystemAppBadge()
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                AdditionalAppInfo(
                    label = stringResource(R.string.app_info_version),
                    value = app.appVersion,
                )
                Spacer(modifier = Modifier.height(4.dp))
                AdditionalAppInfo(
                    label = stringResource(R.string.app_info_installed_at),
                    value = app.installedAt.toReadableDatetime(),
                )
                Spacer(modifier = Modifier.height(4.dp))
                AdditionalAppInfo(
                    label = stringResource(R.string.app_info_installed_by),
                    value = app.installer.packageName ?: "",
                    verticalAlignment = Alignment.CenterVertically,
                    valueContent = {
                        AppInstaller(
                            name = app.installer.name,
                            packageName = app.installer.packageName,
                            verificationStatus = app.installer.verificationStatus,
                        )
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Preview
@Composable
private fun AppInfoContentPreview() {
    RuamMijTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            AppInfoContent(
                app = InstalledApp(
                    name = "Privacy Checker",
                    packageName = "com.akexorcist.ruammij",
                    appVersion = "1.0.0",
                    installedAt = System.currentTimeMillis(),
                    installer = Installer(
                        name = "Google Play",
                        packageName = "com.android.vending",
                        verificationStatus = InstallerVerificationStatus.VERIFIED,
                        sha256 = "12:34:56:78:90",
                    ),
                    icon = null,
                    systemApp = false,
                    sha256 = "12:34:56:78:90",
                ),
                onAppInfoClick = {},
            )
        }
    }
}

@DarkLightPreviews
@Composable
private fun SystemAppInfoContentPreview() {
    RuamMijTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            AppInfoContent(
                app = InstalledApp(
                    name = "Privacy Checker",
                    packageName = "com.akexorcist.ruammij",
                    appVersion = "1.0.0",
                    installedAt = System.currentTimeMillis(),
                    installer = Installer(
                        name = "Google Play",
                        packageName = "com.android.vending",
                        verificationStatus = InstallerVerificationStatus.VERIFIED,
                        sha256 = "12:34:56:78:90",
                    ),
                    icon = null,
                    systemApp = true,
                    sha256 = "12:34:56:78:90",
                ),
                onAppInfoClick = {},
            )
        }
    }
}

@DarkLightPreviews
@Composable
private fun AppInfoContentUnVerifiedPreview() {
    RuamMijTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            AppInfoContent(
                app = InstalledApp(
                    name = "Privacy Checker",
                    packageName = "com.akexorcist.ruammij",
                    appVersion = "1.0.0",
                    installedAt = System.currentTimeMillis(),
                    installer = Installer(
                        name = "Google Play",
                        packageName = "com.android.vending",
                        verificationStatus = InstallerVerificationStatus.UNVERIFIED,
                        sha256 = "12:34:56:78:90",
                    ),
                    icon = null,
                    systemApp = true,
                    sha256 = "12:34:56:78:90",
                ),
                onAppInfoClick = {},
            )
        }
    }
}
