package com.example.work3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimatedScreen()
        }
    }
}

@Composable
fun AnimatedScreen() {
    var showContent by remember { mutableStateOf(false) }
    var iconRotation by remember { mutableStateOf(0f) }
    val rotationAnim by animateFloatAsState(
        targetValue = iconRotation,
        animationSpec = tween(durationMillis = 1000)
    )

    var showImage by remember { mutableStateOf(true) }
    val transition = updateTransition(targetState = showImage, label = "imageTransition")
    val scale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500) },
        label = "scaleAnimation"
    ) { if (it) 1f else 0.5f }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101820))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))
        val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

        LottieAnimation(composition, progress, modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(20.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Rotating Icon",
            modifier = Modifier
                .size(100.dp)
                .rotate(rotationAnim)
                .clickable {
                    iconRotation += 360f
                },
            tint = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        Crossfade(targetState = showImage) { state ->
            Image(
                painter = painterResource(if (state) R.drawable.image1 else R.drawable.image2),
                contentDescription = "Crossfade Image",
                modifier = Modifier
                    .size(150.dp)
                    .scale(scale)
                    .clickable { showImage = !showImage }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { showContent = !showContent }) {
            Text(text = "Toggle Content")
        }

        AnimatedVisibility(visible = showContent, enter = fadeIn() + slideInVertically(), exit = fadeOut() + slideOutVertically()) {
            Text(
                text = "Hello, Animation!",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}
