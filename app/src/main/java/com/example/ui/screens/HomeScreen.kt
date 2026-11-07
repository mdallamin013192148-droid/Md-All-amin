package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClientContactEntity
import com.example.data.model.UserSettingsEntity
import com.example.ui.theme.BKashPink
import com.example.ui.theme.BlueAccent
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.NavyDeep
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.AppScreen

@Composable
fun HomeScreen(
    settings: UserSettingsEntity?,
    clients: List<ClientContactEntity>,
    isBn: Boolean,
    onNavigate: (AppScreen) -> Unit,
    onWithdrawClick: () -> Unit,
    onClaimStreak: () -> Unit,
    onAcceptGig: (String) -> Unit,
    onSubmitGig: (ClientContactEntity) -> Unit,
    onToggleOffline: () -> Unit,
    onQuickWatchAd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val streak = settings?.dailyStreak ?: 1
    val isOffline = settings?.isOfflineModeSimulated ?: false

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Daily Streak & Check-in Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = GoldYellow.copy(alpha = 0.2f),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = NagadOrange,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = String.format(AppStrings.t("streak_day", isBn), streak),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = if (isBn) "প্রতিদিন অ্যাপে এসে ফ্রি বোনাস নিন" else "Open daily to build streak bonus",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = onClaimStreak,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldYellow),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("claim_daily_streak_button")
                    ) {
                        Text(
                            text = if (isBn) "ক্লেম করুন" else "Claim",
                            color = NavyDeep,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Quick Actions Grid (6 Tiles)
        item {
            Column {
                Text(
                    text = AppStrings.t("quick_actions", isBn),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Task Tile
                    QuickActionTile(
                        emoji = "📝",
                        title = AppStrings.t("action_tasks", isBn),
                        sub = if (isBn) "কাজ সম্পন্ন করুন" else "Easy tasks",
                        onClick = { onNavigate(AppScreen.TASKS) },
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_tasks"
                    )

                    // Video Ads Tile
                    QuickActionTile(
                        emoji = "📺",
                        title = AppStrings.t("action_ads", isBn),
                        sub = if (isBn) "রিওয়ার্ড বিজ্ঞাপন" else "Video rewards",
                        onClick = onQuickWatchAd,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_ads"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Direct Withdrawal Tile (bKash & Nagad)
                    QuickActionTile(
                        emoji = "🏧",
                        title = AppStrings.t("action_withdraw", isBn),
                        sub = if (isBn) "bKash / Nagad" else "Instant payout",
                        onClick = onWithdrawClick,
                        modifier = Modifier.weight(1f),
                        accentColor = GreenSuccess,
                        testTag = "quick_action_withdraw"
                    )

                    // 24/7 Chat Tile
                    QuickActionTile(
                        emoji = "💬",
                        title = AppStrings.t("nav_support", isBn),
                        sub = if (isBn) "লাইভ সহায়তা" else "24/7 Live chat",
                        onClick = { onNavigate(AppScreen.SUPPORT) },
                        modifier = Modifier.weight(1f),
                        accentColor = BlueAccent,
                        testTag = "quick_action_support"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Referral Tile
                    QuickActionTile(
                        emoji = "🤝",
                        title = AppStrings.t("action_refer", isBn),
                        sub = if (isBn) "কোড: ALAMIN" else "Invite friends",
                        onClick = { onNavigate(AppScreen.WALLET) },
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_refer"
                    )

                    // Offline Toggle Tile
                    QuickActionTile(
                        emoji = if (isOffline) "📴" else "📶",
                        title = if (isOffline) (if (isBn) "অফলাইন সক্রিয়" else "Offline Active")
                        else (if (isBn) "অনলাইন মোড" else "Online Mode"),
                        sub = if (isBn) "ট্যাপ করে টগল করুন" else "Tap to toggle",
                        onClick = onToggleOffline,
                        modifier = Modifier.weight(1f),
                        accentColor = if (isOffline) Color(0xFFD63031) else GreenSuccess,
                        testTag = "quick_action_offline"
                    )
                }
            }
        }

        // Global Client Contacts & Gigs
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = AppStrings.t("global_clients_title", isBn),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = AppStrings.t("global_clients_sub", isBn),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        items(clients) { client ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = client.flag, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = client.clientName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${client.company} • ${client.country}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = GreenSuccess.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "৳ ${String.format("%.0f", client.rateBdt)}",
                                color = GreenSuccess,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = client.offerTitle,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = client.requirements,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rate: $${client.rateUsd} USD (৳${String.format("%.0f", client.rateBdt)})",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )

                        when (client.status) {
                            "AVAILABLE" -> {
                                Button(
                                    onClick = { onAcceptGig(client.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = AppStrings.t("accept_gig", isBn),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            "ACCEPTED" -> {
                                Button(
                                    onClick = { onSubmitGig(client) },
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (isBn) "কাজের প্রমাণ জমা দিন" else "Submit Work Proof",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            else -> {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = GreenSuccess.copy(alpha = 0.15f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = GreenSuccess,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = AppStrings.t("completed", isBn),
                                            fontSize = 11.sp,
                                            color = GreenSuccess,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Transparency Note Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = GoldYellow.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isBn) "স্বচ্ছতার নিশ্চয়তা ও নিরাপত্তা গ্যারান্টি:"
                        else "Transparency & Security Guarantee:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = NavyDeep
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isBn)
                            "Alamin Global Earn-এ সকল কাজ, বিজ্ঞাপন দেখা ও রেফারেল বোনাস সরাসরি আপনার ওয়ালেটে জমা হয়। প্রতিটি বিকাশ ও নগদ উত্তোলন ২-ফ্যাক্টর অথেনটিকেশন (2FA) এবং AES-256 এনক্রিপশনের মাধ্যমে সম্পূর্ণ সুরক্ষিত।"
                        else
                            "In Alamin Global Earn, all tasks, video ads, and referral rewards credit directly to your wallet. Every bKash & Nagad cash out is authenticated via 2FA and AES-256 encrypted storage.",
                        fontSize = 11.sp,
                        color = NavyDeep.copy(alpha = 0.85f),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionTile(
    emoji: String,
    title: String,
    sub: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = NavyDeep,
    testTag: String = ""
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .testTag(testTag)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(emoji, fontSize = 26.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = sub,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
