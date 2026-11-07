package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.TaskEntity
import com.example.ui.components.AdWatchingDialog
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopAppHeader
import com.example.ui.components.TwoFactorDialog
import com.example.ui.components.WithdrawDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.SupportChatScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.AlaminGlobalEarnTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val userSettings by viewModel.userSettings.collectAsStateWithLifecycle()
            val darkModeOption = userSettings?.darkModeOption ?: "system"
            val accentIndex = userSettings?.accentColorIndex ?: 0

            AlaminGlobalEarnTheme(
                darkModeOption = darkModeOption,
                accentIndex = accentIndex
            ) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val context = LocalContext.current
    val userSettings by viewModel.userSettings.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val clients by viewModel.clients.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    val showWithdrawDialog by viewModel.showWithdrawDialog.collectAsStateWithLifecycle()
    val showTwoFactorDialog by viewModel.showTwoFactorDialog.collectAsStateWithLifecycle()
    val pendingWithdrawal by viewModel.pendingWithdrawal.collectAsStateWithLifecycle()
    val activeAdTask by viewModel.activeAdTask.collectAsStateWithLifecycle()
    val adCountdown by viewModel.adCountdown.collectAsStateWithLifecycle()

    val isBn = userSettings?.language != "en"
    val balance = userSettings?.balance ?: 0.0
    val isOffline = userSettings?.isOfflineModeSimulated ?: false
    val defaultPin = userSettings?.twoFactorPin ?: "123456"

    val snackbarHostState = remember { SnackbarHostState() }

    // Toast notification observer
    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Handle back button for sub-screens
    if (currentScreen != AppScreen.HOME) {
        BackHandler {
            viewModel.setScreen(AppScreen.HOME)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppHeader(
                balance = balance,
                isOffline = isOffline,
                isBn = isBn,
                onLanguageToggle = { viewModel.toggleLanguage() },
                onWithdrawClick = { viewModel.openWithdrawDialog() },
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            BottomNavBar(
                currentScreen = currentScreen,
                onScreenSelect = { viewModel.setScreen(it) },
                isBn = isBn
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(
                    settings = userSettings,
                    clients = clients,
                    isBn = isBn,
                    onNavigate = { viewModel.setScreen(it) },
                    onWithdrawClick = { viewModel.openWithdrawDialog() },
                    onClaimStreak = { viewModel.claimDailyCheckIn() },
                    onAcceptGig = { viewModel.acceptGig(it) },
                    onSubmitGig = { viewModel.submitGigProof(it) },
                    onToggleOffline = { viewModel.toggleSimulatedOffline() },
                    onQuickWatchAd = {
                        val adTask = tasks.firstOrNull { it.category == "ad_reward" }
                            ?: tasks.firstOrNull()
                        if (adTask != null) {
                            viewModel.startWatchingAd(adTask)
                        } else {
                            viewModel.setScreen(AppScreen.TASKS)
                        }
                    }
                )

                AppScreen.TASKS -> TasksScreen(
                    tasks = tasks,
                    isBn = isBn,
                    onCompleteTask = { viewModel.completeTask(it) },
                    onWatchAd = { viewModel.startWatchingAd(it) }
                )

                AppScreen.WALLET -> WalletScreen(
                    settings = userSettings,
                    transactions = transactions,
                    isBn = isBn,
                    onOpenWithdraw = { viewModel.openWithdrawDialog(it) },
                    onToast = { viewModel.showToast(it) }
                )

                AppScreen.SUPPORT -> SupportChatScreen(
                    messages = chatMessages,
                    isBn = isBn,
                    onSendMessage = { viewModel.sendSupportChat(it) }
                )

                AppScreen.PROFILE -> ProfileSettingsScreen(
                    settings = userSettings,
                    isBn = isBn,
                    onLanguageToggle = { viewModel.toggleLanguage() },
                    onDarkModeChange = { viewModel.setDarkMode(it) },
                    onAccentChange = { viewModel.setAccentColor(it) },
                    onTwoFactorChange = { enabled, pin -> viewModel.updateTwoFactor(enabled, pin) },
                    onProfileSave = { n, e, p -> viewModel.updateProfile(n, e, p) },
                    onToggleOffline = { viewModel.toggleSimulatedOffline() },
                    onPerformBackup = { viewModel.performAutomaticBackup() },
                    onToast = { viewModel.showToast(it) }
                )
            }
        }
    }

    // Active Dialogs
    if (showWithdrawDialog) {
        WithdrawDialog(
            currentBalance = balance,
            isBn = isBn,
            onDismiss = { viewModel.closeWithdrawDialog() },
            onProceed = { method, account, amount ->
                viewModel.onWithdrawRequested(method, account, amount)
            }
        )
    }

    if (showTwoFactorDialog) {
        TwoFactorDialog(
            pendingWithdrawal = pendingWithdrawal,
            isBn = isBn,
            defaultPin = defaultPin,
            onConfirm = { pin -> viewModel.verifyAndAuthorize2FA(pin) },
            onCancel = { viewModel.cancelTwoFactor() }
        )
    }

    if (activeAdTask != null) {
        AdWatchingDialog(
            task = activeAdTask!!,
            countdown = adCountdown,
            isBn = isBn,
            onClose = { viewModel.closeAdDialog() }
        )
    }
}
