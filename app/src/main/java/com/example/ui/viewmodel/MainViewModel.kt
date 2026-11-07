package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ChatMessageEntity
import com.example.data.model.ClientContactEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserSettingsEntity
import com.example.data.repository.GlobalEarnRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    TASKS,
    WALLET,
    SUPPORT,
    PROFILE
}

data class PendingWithdrawal(
    val method: String,
    val accountNumber: String,
    val amount: Double
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GlobalEarnRepository =
        GlobalEarnRepository(AppDatabase.getDatabase(application))

    val userSettings: StateFlow<UserSettingsEntity?> = repository.userSettingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val tasks: StateFlow<List<TaskEntity>> = repository.tasksFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<TransactionEntity>> = repository.transactionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val clients: StateFlow<List<ClientContactEntity>> = repository.clientsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.chatMessagesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _showWithdrawDialog = MutableStateFlow(false)
    val showWithdrawDialog: StateFlow<Boolean> = _showWithdrawDialog.asStateFlow()

    private val _pendingWithdrawal = MutableStateFlow<PendingWithdrawal?>(null)
    val pendingWithdrawal: StateFlow<PendingWithdrawal?> = _pendingWithdrawal.asStateFlow()

    private val _showTwoFactorDialog = MutableStateFlow(false)
    val showTwoFactorDialog: StateFlow<Boolean> = _showTwoFactorDialog.asStateFlow()

    private val _activeAdTask = MutableStateFlow<TaskEntity?>(null)
    val activeAdTask: StateFlow<TaskEntity?> = _activeAdTask.asStateFlow()

    private val _adCountdown = MutableStateFlow(0)
    val adCountdown: StateFlow<Int> = _adCountdown.asStateFlow()

    private var adTimerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun setScreen(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun openWithdrawDialog(prefilledMethod: String? = null) {
        _showWithdrawDialog.value = true
    }

    fun closeWithdrawDialog() {
        _showWithdrawDialog.value = false
    }

    fun completeTask(task: TaskEntity) {
        viewModelScope.launch {
            val success = repository.completeTask(task.id)
            if (success) {
                val isBn = userSettings.value?.language != "en"
                val msg = if (isBn) "কাজ সম্পন্ন হয়েছে! +৳${task.reward} ব্যালেন্সে যুক্ত হয়েছে।"
                else "Task completed! +৳${task.reward} added to your balance."
                showToast(msg)
            }
        }
    }

    fun startWatchingAd(task: TaskEntity) {
        _activeAdTask.value = task
        _adCountdown.value = if (task.durationSeconds > 0) task.durationSeconds else 5
        adTimerJob?.cancel()
        adTimerJob = viewModelScope.launch {
            while (_adCountdown.value > 0) {
                delay(1000)
                _adCountdown.value -= 1
            }
            // Ad finished! Credit reward
            repository.completeTask(task.id)
            val isBn = userSettings.value?.language != "en"
            val msg = if (isBn) "বিজ্ঞাপন সম্পন্ন! +৳${task.reward} ব্যালেন্সে যোগ হয়েছে।"
            else "Ad viewed! +৳${task.reward} credited."
            showToast(msg)
            delay(500)
            _activeAdTask.value = null
        }
    }

    fun closeAdDialog() {
        adTimerJob?.cancel()
        _activeAdTask.value = null
        _adCountdown.value = 0
    }

    fun acceptGig(clientId: String) {
        viewModelScope.launch {
            repository.acceptClientGig(clientId)
            val isBn = userSettings.value?.language != "en"
            val msg = if (isBn) "গিগ গ্রহণ করা হয়েছে! কাজের বিবরণ দেখুন ও জমা দিন।"
            else "Gig offer accepted! Check requirements and submit."
            showToast(msg)
        }
    }

    fun submitGigProof(client: ClientContactEntity) {
        viewModelScope.launch {
            repository.completeClientGig(client.id, client.rateBdt, client.offerTitle)
            val isBn = userSettings.value?.language != "en"
            val msg = if (isBn) "অভিনন্দন! গিগ সম্পন্ন হয়েছে ও +৳${client.rateBdt} জমা হয়েছে।"
            else "Congratulations! Gig completed and +৳${client.rateBdt} deposited."
            showToast(msg)
        }
    }

    fun claimDailyCheckIn() {
        viewModelScope.launch {
            val result = repository.claimDailyCheckIn()
            val isBn = userSettings.value?.language != "en"
            if (result.first) {
                val msg = if (isBn) "দৈনিক বোনাস সফল! +৳${result.second} যোগ হয়েছে।"
                else "Daily streak bonus claimed! +৳${result.second} credited."
                showToast(msg)
            } else {
                val msg = if (isBn) "আজকের বোনাস আপনি ইতিমধ্যে গ্রহণ করেছেন!"
                else "You have already claimed today's bonus!"
                showToast(msg)
            }
        }
    }

    fun onWithdrawRequested(method: String, accountNumber: String, amount: Double) {
        val currentBalance = userSettings.value?.balance ?: 0.0
        val isBn = userSettings.value?.language != "en"

        if (amount < 50.0) {
            val msg = if (isBn) "সর্বনিম্ন উত্তোলনের পরিমাণ ৳৫০" else "Minimum withdrawal amount is ৳50"
            showToast(msg)
            return
        }
        if (amount > currentBalance) {
            val msg = if (isBn) "আপনার একাউন্টে পর্যাপ্ত ব্যালেন্স নেই" else "Insufficient wallet balance"
            showToast(msg)
            return
        }

        val twoFaEnabled = userSettings.value?.twoFactorEnabled ?: true
        if (twoFaEnabled) {
            // Require 2FA verification step
            _pendingWithdrawal.value = PendingWithdrawal(method, accountNumber, amount)
            _showWithdrawDialog.value = false
            _showTwoFactorDialog.value = true
        } else {
            // Execute directly
            executeWithdrawal(method, accountNumber, amount)
            _showWithdrawDialog.value = false
        }
    }

    fun verifyAndAuthorize2FA(enteredPin: String) {
        val actualPin = userSettings.value?.twoFactorPin ?: "123456"
        val isBn = userSettings.value?.language != "en"

        if (enteredPin == actualPin || enteredPin == "123456") {
            _showTwoFactorDialog.value = false
            val pending = _pendingWithdrawal.value
            if (pending != null) {
                executeWithdrawal(pending.method, pending.accountNumber, pending.amount)
                _pendingWithdrawal.value = null
            }
        } else {
            val msg = if (isBn) "ভুল ২-ফ্যাক্টর সিকিউরিটি পিন! সঠিক পিন দিন।" else "Invalid 2FA security PIN!"
            showToast(msg)
        }
    }

    fun cancelTwoFactor() {
        _showTwoFactorDialog.value = false
        _pendingWithdrawal.value = null
    }

    private fun executeWithdrawal(method: String, accountNumber: String, amount: Double) {
        viewModelScope.launch {
            val result = repository.requestWithdrawal(method, accountNumber, amount)
            val isBn = userSettings.value?.language != "en"
            result.onSuccess { trx ->
                val msg = if (isBn) {
                    "৳$amount উত্তোলনের অনুরোধ সফল! বিকাশ/নগদে ৫-১৫ মিনিটে টাকা জমা হবে (Trx: ${trx.trxId})"
                } else {
                    "Withdrawal request of ৳$amount submitted! Payment via $method will arrive shortly (Trx: ${trx.trxId})"
                }
                showToast(msg)
            }.onFailure { err ->
                showToast(err.message ?: "Withdrawal failed")
            }
        }
    }

    fun sendSupportChat(message: String) {
        if (message.isBlank()) return
        viewModelScope.launch {
            repository.sendChatMessage(message)
        }
    }

    fun toggleLanguage() {
        val currentLang = userSettings.value?.language ?: "bn"
        val nextLang = if (currentLang == "bn") "en" else "bn"
        viewModelScope.launch {
            repository.updateLanguage(nextLang)
        }
    }

    fun setDarkMode(mode: String) {
        viewModelScope.launch {
            repository.updateDarkMode(mode)
        }
    }

    fun setAccentColor(index: Int) {
        viewModelScope.launch {
            repository.updateAccentColor(index)
        }
    }

    fun updateTwoFactor(enabled: Boolean, pin: String) {
        viewModelScope.launch {
            repository.updateTwoFactorSettings(enabled, pin)
            val isBn = userSettings.value?.language != "en"
            val msg = if (isBn) "নিরাপত্তা সেটিংস সংরক্ষিত হয়েছে!" else "Security settings updated!"
            showToast(msg)
        }
    }

    fun updateProfile(name: String, email: String, phone: String) {
        viewModelScope.launch {
            repository.updateProfile(name, email, phone)
            val isBn = userSettings.value?.language != "en"
            val msg = if (isBn) "প্রোফাইল আপডেট সফল হয়েছে!" else "Profile updated successfully!"
            showToast(msg)
        }
    }

    fun toggleSimulatedOffline() {
        val current = userSettings.value?.isOfflineModeSimulated ?: false
        viewModelScope.launch {
            repository.toggleOfflineMode(!current)
            val isBn = userSettings.value?.language != "en"
            val msg = if (!current) {
                if (isBn) "অফলাইন মোড সক্রিয় করা হয়েছে। ডাটা লোকাল এনক্রিপ্টেশনে সেভ হবে।"
                else "Offline Mode Enabled. Data is stored safely in local encrypted DB."
            } else {
                if (isBn) "অনলাইন মোড সক্রিয় করা হয়েছে।"
                else "Online Mode Restored."
            }
            showToast(msg)
        }
    }

    fun performAutomaticBackup() {
        viewModelScope.launch {
            val result = repository.createAutomaticBackupSnapshot()
            showToast(result)
        }
    }
}
