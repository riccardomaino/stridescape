package it.unito.progmob.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import it.unito.progmob.ui.theme.doubleExtraLarge
import it.unito.progmob.ui.theme.floatingActionButtonSize
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.navigationBarHeight
import it.unito.progmob.ui.theme.navigationBarShadow
import it.unito.progmob.ui.theme.small

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    floatingActionButtonIcon: @Composable() () -> Unit,
    onClickFloatingActionButton: () -> Unit,
    onClickHome: () -> Unit,
    onClickMap: () -> Unit,
    onClickHistory: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(small),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomAppBar(
            tonalElevation = 0.dp,
            contentPadding = PaddingValues(0.dp),
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .width(LocalContext.current.resources.configuration.screenWidthDp.dp*0.4f)
                .height(navigationBarHeight)
                .shadow(navigationBarShadow, shape = RoundedCornerShape(doubleExtraLarge))
                .clip(
                    shape = RoundedCornerShape(
                        doubleExtraLarge
                    )
                )

        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { onClickHome() }) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Localized description",
                            modifier = Modifier.size(large),
                        )
                    }
                }
                IconButton(onClick = { onClickMap() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ShowChart,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(large),
                    )
                }
                IconButton(onClick = { onClickHistory() }) {
                    Icon(
                        Icons.Filled.History,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(large),
                    )
                }
            }
        }
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primary,
            elevation = FloatingActionButtonDefaults.elevation(),
            onClick = { onClickFloatingActionButton() },
            shape = FloatingActionButtonDefaults.extendedFabShape,
            modifier = Modifier.padding(small).size(floatingActionButtonSize)
        ) {
            floatingActionButtonIcon()
        }
    }
}