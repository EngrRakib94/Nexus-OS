package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskPriority
import com.example.data.model.TaskStatus
import com.example.ui.theme.*

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberBorder,
    glowColor: Color = Color.Transparent,
    backgroundColor: Color = CyberSurface,
    cornerPadding: Dp = 14.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else Modifier

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(borderColor, borderColor.copy(alpha = 0.8f))
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .then(clickModifier)
            .padding(cornerPadding)
    ) {
        Column(content = content)
    }
}

@Composable
fun CyberBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = NeonGreen,
    backgroundColor: Color = color.copy(alpha = 0.15f)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                letterSpacing = 0.6.sp
            ),
            color = color,
            maxLines = 1
        )
    }
}

@Composable
fun StatusBadge(status: TaskStatus) {
    val (color, label) = when (status) {
        TaskStatus.TODO -> NeonCyan to "TO DO"
        TaskStatus.IN_PROGRESS -> NeonGreen to "IN PROGRESS"
        TaskStatus.PENDING -> NeonAmber to "PENDING"
        TaskStatus.COMPLETED -> Color(0xFF22C55E) to "COMPLETED"
        TaskStatus.CANCELLED -> NeonRed to "CANCELLED"
    }
    CyberBadge(text = label, color = color)
}

@Composable
fun PriorityBadge(priority: TaskPriority) {
    val (color, label) = when (priority) {
        TaskPriority.LOW -> TextSecondary to "LOW"
        TaskPriority.MEDIUM -> NeonCyan to "MED"
        TaskPriority.HIGH -> NeonAmber to "HIGH"
        TaskPriority.CRITICAL -> NeonRed to "CRIT"
    }
    CyberBadge(text = label, color = color)
}

@Composable
fun RaciRoleBadge(role: String, personName: String) {
    val roleColor = when (role.uppercase()) {
        "R" -> NeonGreen
        "A" -> NeonRed
        "C" -> NeonCyan
        "I" -> NeonPurple
        else -> TextSecondary
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(CyberSurfaceElevated)
            .border(1.dp, CyberBorder, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(roleColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = role,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = personName,
            color = TextPrimary,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CyberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isPrimary: Boolean = true,
    testTag: String = "cyber_button"
) {
    val bg = if (isPrimary) NeonGreen else CyberSurfaceElevated
    val contentColor = if (isPrimary) Color(0xFF00290E) else NeonCyan
    val border = if (isPrimary) NeonGreen else CyberBorder

    Button(
        onClick = onClick,
        modifier = modifier
            .testTag(testTag)
            .border(1.dp, border, RoundedCornerShape(6.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = bg,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
fun PulsingNodeIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(NeonGreen.copy(alpha = alpha))
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "LIVE // ASIA-EAST1",
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                letterSpacing = 0.5.sp
            ),
            color = NeonGreen
        )
    }
}

@Composable
fun CyberStatWidget(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    accentColor: Color = NeonGreen,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    CyberCard(
        modifier = modifier,
        borderColor = if (onClick != null) CyberBorder.copy(alpha = 0.9f) else CyberBorder,
        backgroundColor = CyberSurface,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.8.sp,
                        fontSize = 10.sp
                    ),
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = accentColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(accentColor.copy(alpha = 0.12f))
                        .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CyberSectionHeader(
    title: String,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(14.dp)
                        .background(NeonGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 13.sp
                    ),
                    color = TextPrimary
                )
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 9.dp)
                )
            }
        }
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    fontSize = 10.sp
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onActionClick() }
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
    }
}
