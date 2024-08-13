@file:OptIn(ExperimentalMaterial3Api::class)

package com.akexorcist.ruammij.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue.PartiallyExpanded
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.akexorcist.ruammij.R
import com.akexorcist.ruammij.common.Installer
import com.akexorcist.ruammij.common.InstallerVerificationStatus
import com.akexorcist.ruammij.data.InstalledApp
import com.akexorcist.ruammij.data.PermissionInfo
import com.akexorcist.ruammij.ui.theme.Buttons
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import com.akexorcist.ruammij.utility.DarkLightPreviews
import com.akexorcist.ruammij.utility.toReadableDatetime
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppInfoBottomSheet(
    app: InstalledApp,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onOpenInSettingClick: () -> Unit,
    onMarkAsSafeClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        onDismissRequest = onDismissRequest,
    ) {
        DisplayAppInfoContent(
            app = app,
            onOpenInSettingClick = onOpenInSettingClick,
            onMarkAsSafeClick = onMarkAsSafeClick,
            onCloseButtonClick = onDismissRequest
        )
    }
}

@Composable
private fun DisplayAppInfoContent(
    app: InstalledApp,
    onOpenInSettingClick: () -> Unit,
    onMarkAsSafeClick: () -> Unit,
    onCloseButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier.navigationBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 64.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            Column {
                HeaderSection(app = app)

                Spacer(modifier = Modifier.height(8.dp))

                GeneralInformationSection(app = app)

                Spacer(modifier = Modifier.height(16.dp))

                PermissionSection(
                    title = stringResource(R.string.app_info_permissions_section_title),
                    permissions = app.permissions
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        BottomSection(
            modifier = Modifier.align(Alignment.BottomCenter),
            app = app,
            onOpenInSettingClick = onOpenInSettingClick,
            onMarkAsSafeClick = onMarkAsSafeClick,
            onCloseButtonClick = onCloseButtonClick
        )
    }
}

@Composable
private fun HeaderSection(
    app: InstalledApp,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(64.dp),
            painter = rememberDrawablePainter(drawable = app.icon),
            contentDescription = stringResource(
                R.string.description_app_icon,
                app.name
            ),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            HeadlineText(text = app.name)

            BodyText(
                text = app.packageName,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (app.systemApp) {
                Spacer(modifier = Modifier.height(4.dp))
                SystemAppBadge()
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun GeneralInformationSection(
    app: InstalledApp,
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            BoldBodyText(
                text = stringResource(R.string.app_info_general_section_title),
                color = MaterialTheme.colorScheme.primary
            )

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
        }
    }
}

@Composable
private fun PermissionSection(
    title: String,
    permissions: List<PermissionInfo>
) {
    if (permissions.isNotEmpty()) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(6.dp),
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                BoldBodyText(text = title, color = MaterialTheme.colorScheme.primary)

                Spacer(modifier = Modifier.height(4.dp))

                permissions.forEachIndexed { index, permission ->
                    Spacer(modifier = Modifier.height(8.dp))
                    PermissionItem(
                        info = permission
                    )

                    if (index != permissions.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionItem(
    info: PermissionInfo,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                BoldBodyText(
                    text = info.label,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.width(16.dp))

                val icon =
                    if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = rememberVectorPainter(image = icon),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    contentDescription = ""
                )
            }

            AnimatedVisibility(visible = isExpanded && info.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))

                BodyText(
                    text = info.description,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun BottomSection(
    modifier: Modifier = Modifier,
    app: InstalledApp,
    onOpenInSettingClick: () -> Unit,
    onMarkAsSafeClick: () -> Unit,
    onCloseButtonClick: () -> Unit,
) {
    Box(modifier = modifier) {
        Column {
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                FilledTonalButton(
                    modifier = Modifier.height(32.dp),
                    contentPadding = Buttons.ContentPadding,
                    onClick = onOpenInSettingClick,
                ) {
                    Text(text = stringResource(R.string.app_info_button_app_info))
                }

                if (app.installer.verificationStatus != InstallerVerificationStatus.VERIFIED) {
                    Spacer(modifier = Modifier.width(8.dp))
                    FilledTonalButton(
                        modifier = Modifier.height(32.dp),
                        contentPadding = Buttons.ContentPadding,
                        onClick = onMarkAsSafeClick,
                    ) {
                        Text(text = stringResource(R.string.app_info_button_mark_as_safe))
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    modifier = Modifier.height(32.dp),
                    contentPadding = Buttons.ContentPadding,
                    onClick = onCloseButtonClick,
                ) {
                    BoldBodyText(text = stringResource(R.string.installed_app_display_option_close))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@DarkLightPreviews
@Composable
private fun DisplayAppInfoBottomSheetPreview() {
    RuamMijTheme {
        AppInfoBottomSheet(
            app = InstalledApp(
                name = "App Name",
                packageName = "com.akexorcist.ruammij",
                appVersion = "1.0.0",
                icon = ResourcesCompat.getDrawable(
                    LocalContext.current.resources,
                    R.mipmap.ic_launcher,
                    LocalContext.current.theme
                ),
                systemApp = true,
                installedAt = System.currentTimeMillis(),
                installer = Installer(
                    name = "Installer Name",
                    packageName = "com.akexorcist.installer",
                    verificationStatus = InstallerVerificationStatus.UNVERIFIED,
                    sha256 = "12:34:56:78:90",
                ),
                sha256 = "12:34:56:78:90",
                permissions = listOf(
                    PermissionInfo(
                        name = "",
                        label = "Permission 1",
                        description = "Description 1"
                    ),
                    PermissionInfo(
                        name = "",
                        label = "Permission 2",
                        description = "Description 2"
                    ),
                ),
            ),
            sheetState = SheetState(
                skipPartiallyExpanded = false,
                density = LocalDensity.current,
                initialValue = PartiallyExpanded,
                skipHiddenState = true,
            ),
            onOpenInSettingClick = {},
            onMarkAsSafeClick = {},
            onDismissRequest = {}
        )
    }
}