package com.hahouari.triggered_ripple_effect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.hahouari.triggered_ripple_effect.ui.theme.TriggeredRippleEffectTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { TriggeredRippleEffectTheme { MyScreen() } }
  }
}

@Composable
fun MyScreen(modifier: Modifier = Modifier) {
  val configuration = LocalConfiguration.current
  val scope = rememberCoroutineScope()
  val interactionSource = remember { MutableInteractionSource() }
  val indication = rememberRipple(
    // if set false, the ripple effect will not be cropped to the button.
    bounded = true,
    // if you want to spread to stop right at the edges of your targeted composable,
    // take the longest side divided by two, in this case the row width is the screen width.
    radius = configuration.screenWidthDp.dp / 2,
  )
  // used later to change Dp unit to Pixels using .toPx()
  val density = LocalDensity.current

  LaunchedEffect(Unit) {
    with(density) {
      scope.launch {
        // wait a little bit for user to focus on the screen
        delay(800)
        val centerX = configuration.screenWidthDp.dp / 2
        val centerY = 64.dp / 2
        // simulate a press for the targeted setting tile
        val press = PressInteraction.Press(Offset(centerX.toPx(), centerY.toPx()))
        interactionSource.emit(press)
        // wait a little bit for the effect to animate
        delay(400)
        // release the effect
        val release = PressInteraction.Release(press)
        interactionSource.emit(release)
      }
    }
  }

  Column(modifier = modifier.systemBarsPadding()) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 32.dp, vertical = 8.dp)
    ) {
      Text(text = "Select directory")
      OutlinedButton(
        contentPadding = PaddingValues(vertical = 15.dp, horizontal = 28.dp),
        onClick = { /* Something to do.. */ }
      ) {
        Text(text = "Select")
      }
    }
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .indication(interactionSource, indication) // <--- This line is soooo important :)
        .padding(horizontal = 32.dp, vertical = 8.dp),
    ) {
      Text(text = "Clear Cache")
      OutlinedButton(
        contentPadding = PaddingValues(vertical = 15.dp, horizontal = 28.dp),
        onClick = { /* Something to do.. */ }
      ) {
        Text(text = "Clear")
      }
    }
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 32.dp, vertical = 8.dp)
    ) {
      Text(text = "Another Example")
      Text(text = "Do nothing")
    }
  }
}
