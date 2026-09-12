package com.arbani.alquranpro.ui.screens

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbani.alquranpro.ui.components.IOSCard
import com.arbani.alquranpro.ui.theme.EmeraldPrimary
import com.arbani.alquranpro.ui.theme.IOSGradients

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(onBack: () -> Unit) {
    var count by remember { mutableIntStateOf(0) }
    var selectedTarget by remember { mutableIntStateOf(33) }
    val zikirOptions = listOf("Subhanallah", "Alhamdulillah", "Allahu Akbar", "Astaghfirullah", "La ilaha illallah")
    var selectedZikirIndex by remember { mutableIntStateOf(0) }

    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "tasbih_scale"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasbih Digital", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("‹", fontSize = 32.sp, fontWeight = FontWeight.Light)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Zikir Selector Card
            IOSCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Pilih Lafadz Zikir",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = zikirOptions[selectedZikirIndex],
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        zikirOptions.forEachIndexed { index, zikir ->
                            if (index < 3) {
                                FilterChip(
                                    selected = selectedZikirIndex == index,
                                    onClick = { selectedZikirIndex = index; count = 0 },
                                    label = { Text(zikir.take(7) + "...", fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            }

            // Big Tap Circle with Haptics
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(230.dp)
                        .scale(scale)
                        .shadow(16.dp, CircleShape, ambientColor = EmeraldPrimary.copy(alpha = 0.25f), spotColor = EmeraldPrimary)
                        .clip(CircleShape)
                        .background(IOSGradients.HeroEmerald)
                        .border(4.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            count++
                            if (selectedTarget > 0 && count % selectedTarget == 0) {
                                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = count.toString(),
                            fontSize = 60.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = if (selectedTarget > 0) "Target: $selectedTarget" else "Tanpa Batas",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "Ketuk Di Sini",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Target selector & Reset
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(33, 99, 0).forEach { target ->
                        FilledTonalButton(
                            onClick = { selectedTarget = target },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (selectedTarget == target) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (selectedTarget == target) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text(if (target == 0) "∞" else target.toString(), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                OutlinedButton(
                    onClick = {
                        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                        count = 0
                    },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Reset")
                }
            }
        }
    }
}
