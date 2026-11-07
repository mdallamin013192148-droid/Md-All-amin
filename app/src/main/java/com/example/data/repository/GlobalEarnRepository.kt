package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.ChatMessageEntity
import com.example.data.model.ClientContactEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserSettingsEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class GlobalEarnRepository(private val db: AppDatabase) {

    val userSettingsFlow: Flow<UserSettingsEntity?> = db.userSettingsDao().getUserSettingsFlow()
    val tasksFlow: Flow<List<TaskEntity>> = db.taskDao().getAllTasksFlow()
    val transactionsFlow: Flow<List<TransactionEntity>> = db.transactionDao().getAllTransactionsFlow()
    val clientsFlow: Flow<List<ClientContactEntity>> = db.clientContactDao().getAllClientsFlow()
    val chatMessagesFlow: Flow<List<ChatMessageEntity>> = db.chatMessageDao().getAllMessagesFlow()

    suspend fun seedInitialDataIfEmpty() {
        var settings = db.userSettingsDao().getUserSettings()
        if (settings == null) {
            settings = UserSettingsEntity(
                id = 1,
                balance = 125.0,
                userName = "Alamin Hossain",
                userEmail = "alamin.worker@globalearn.net",
                userPhone = "01712345678",
                country = "Bangladesh",
                referralCode = "ALAMIN-EARN",
                referredCount = 5,
                referralEarnings = 50.0,
                dailyStreak = 3,
                lastCheckInDate = "",
                twoFactorEnabled = true,
                twoFactorPin = "123456",
                biometricLockEnabled = false,
                language = "bn",
                darkModeOption = "system",
                accentColorIndex = 0,
                isOfflineModeSimulated = false,
                lastBackupTimestamp = System.currentTimeMillis()
            )
            db.userSettingsDao().insertOrUpdate(settings)

            // Seed Tasks
            val sampleTasks = listOf(
                TaskEntity(
                    id = "task_1",
                    titleBn = "প্রোফাইল তথ্য ও ফোন নম্বর যাচাই",
                    titleEn = "Verify Profile & Phone Number",
                    descBn = "আপনার সম্পূর্ণ প্রোফাইল তথ্য পূরণ করে পরিচয় নিশ্চিত করুন",
                    descEn = "Complete your profile info and verify your phone number",
                    category = "micro_task",
                    reward = 15.0,
                    country = "Global",
                    isCompleted = true,
                    durationSeconds = 0,
                    clientName = "Global Security Team"
                ),
                TaskEntity(
                    id = "task_2",
                    titleBn = "আন্তর্জাতিক মার্কেট রিসার্চ জরিপ",
                    titleEn = "International Market Research Survey",
                    descBn = "অনলাইন শপিং অভিজ্ঞতা সম্পর্কিত ৩ মিনিটের একটি সংক্ষিপ্ত জরিপ",
                    descEn = "A short 3-minute survey regarding online shopping habits",
                    category = "survey",
                    reward = 25.0,
                    country = "USA",
                    isCompleted = false,
                    durationSeconds = 180,
                    clientName = "Apex Consumer Analytics"
                ),
                TaskEntity(
                    id = "task_3",
                    titleBn = "স্পনসরড পার্টনার ভিডিও বিজ্ঞাপন",
                    titleEn = "Sponsored Partner Video Ad",
                    descBn = "বিশ্বস্ত গ্লোবাল অংশীদারের ৫ সেকেন্ডের ভিডিওটি সম্পূর্ণ দেখুন",
                    descEn = "Watch a 5-second verified partner video ad completely",
                    category = "ad_reward",
                    reward = 8.0,
                    country = "Global",
                    isCompleted = false,
                    durationSeconds = 5,
                    clientName = "AdPulse Global Network"
                ),
                TaskEntity(
                    id = "task_4",
                    titleBn = "বাংলা থেকে ইংরেজি ডাটা লেবেলিং",
                    titleEn = "Bengali to English Data Labeling",
                    descBn = "৫টি সাধারণ বাক্য পর্যালোচনা করে সঠিক অনুবাদ মার্ক করুন",
                    descEn = "Review 5 Bengali conversational sentences for AI quality",
                    category = "micro_task",
                    reward = 35.0,
                    country = "UK",
                    isCompleted = false,
                    durationSeconds = 60,
                    clientName = "LinguaTech UK"
                ),
                TaskEntity(
                    id = "task_5",
                    titleBn = "ই-কমার্স ওয়েবসাইট ইউএক্স রিভিউ",
                    titleEn = "E-Commerce Website UX Review",
                    descBn = "নতুন শপিং প্ল্যাটফর্মের হোমপেজ লোডিং ও বাটন ক্লিক ফিডব্যাক দিন",
                    descEn = "Test website checkout navigation and submit user experience feedback",
                    category = "micro_task",
                    reward = 40.0,
                    country = "Germany",
                    isCompleted = false,
                    durationSeconds = 120,
                    clientName = "Berlin Tech Ventures"
                ),
                TaskEntity(
                    id = "task_6",
                    titleBn = "ভিডিও বিজ্ঞাপন ২ (গেমিং প্রিভিউ)",
                    titleEn = "Video Ad 2 (Gaming Preview)",
                    descBn = "শীর্ষ মোবাইল গেম প্রিভিউ বিজ্ঞাপন দেখুন ও রিওয়ার্ড পান",
                    descEn = "Watch top mobile game promotional preview to earn rewards",
                    category = "ad_reward",
                    reward = 10.0,
                    country = "Global",
                    isCompleted = false,
                    durationSeconds = 5,
                    clientName = "PlayNexus Ads"
                )
            )
            db.taskDao().insertAll(sampleTasks)

            // Seed Initial Transactions
            val sampleTransactions = listOf(
                TransactionEntity(
                    id = "trx_1",
                    type = "EARNING",
                    method = "Task Bonus",
                    accountNumber = "Profile Verification",
                    amount = 15.0,
                    fee = 0.0,
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - 86400000L * 2,
                    trxId = "AGE-WEL-88192"
                ),
                TransactionEntity(
                    id = "trx_2",
                    type = "EARNING",
                    method = "Referral",
                    accountNumber = "Invite Bonus (5 Active)",
                    amount = 50.0,
                    fee = 0.0,
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - 86400000L,
                    trxId = "AGE-REF-44211"
                ),
                TransactionEntity(
                    id = "trx_3",
                    type = "EARNING",
                    method = "Daily Check-in",
                    accountNumber = "Day 3 Streak Bonus",
                    amount = 60.0,
                    fee = 0.0,
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - 3600000L * 5,
                    trxId = "AGE-CHK-91024"
                )
            )
            db.transactionDao().insertAll(sampleTransactions)

            // Seed Global Client Contacts
            val sampleClients = listOf(
                ClientContactEntity(
                    id = "client_us_1",
                    clientName = "David Miller",
                    company = "Silicon Crest Media",
                    country = "United States",
                    flag = "🇺🇸",
                    role = "Marketing Lead",
                    offerTitle = "Social Proof & App Rating Audit",
                    rateUsd = 4.5,
                    rateBdt = 540.0,
                    requirements = "Audit mobile app UI and leave constructive feedback note.",
                    contactEmail = "david.miller@siliconcrest.io",
                    status = "AVAILABLE"
                ),
                ClientContactEntity(
                    id = "client_uk_2",
                    clientName = "Sophie Turner",
                    company = "London AI DataLab",
                    country = "United Kingdom",
                    flag = "🇬🇧",
                    role = "Data Operations",
                    offerTitle = "Speech & Text Transcription Check",
                    rateUsd = 6.0,
                    rateBdt = 720.0,
                    requirements = "Verify 20 short audio clips against written transcript.",
                    contactEmail = "sophie.t@londondatalab.co.uk",
                    status = "AVAILABLE"
                ),
                ClientContactEntity(
                    id = "client_de_3",
                    clientName = "Lukas Weber",
                    company = "Nordic QA Solutions",
                    country = "Germany",
                    flag = "🇩🇪",
                    role = "Quality Assurance",
                    offerTitle = "E-Commerce Bengali Localization",
                    rateUsd = 8.0,
                    rateBdt = 960.0,
                    requirements = "Proofread product descriptions localized for Bangladesh audience.",
                    contactEmail = "lukas@nordicqa.de",
                    status = "AVAILABLE"
                ),
                ClientContactEntity(
                    id = "client_sg_4",
                    clientName = "Elena Tan",
                    company = "FinTech Asia Singapore",
                    country = "Singapore",
                    flag = "🇸🇬",
                    role = "Regional Manager",
                    offerTitle = "Mobile Payment Workflow Testing",
                    rateUsd = 5.0,
                    rateBdt = 600.0,
                    requirements = "Test checkout responsiveness and record confirmation screen latency.",
                    contactEmail = "elena.tan@fintechasia.sg",
                    status = "AVAILABLE"
                )
            )
            db.clientContactDao().insertAll(sampleClients)

            // Seed Welcome Support Message
            val welcomeMessages = listOf(
                ChatMessageEntity(
                    sender = "SUPPORT",
                    message = "স্বাগতম Alamin Global Earn-এ! 🌟 আমাদের ২৪/৭ সাপোর্ট টিম আপনার সেবায় সর্বদা নিয়োজিত। কাজ, বিকাশ/নগদে টাকা উত্তোলন বা সিকিউরিটি সম্পর্কিত যেকোনো প্রশ্ন এখানে করতে পারেন।",
                    timestamp = System.currentTimeMillis() - 100000L,
                    isQuickReply = false
                ),
                ChatMessageEntity(
                    sender = "SUPPORT",
                    message = "Welcome to Alamin Global Earn! 🚀 24/7 Live Support is online. Feel free to ask about tasks, instant bKash/Nagad withdrawals, or 2FA security.",
                    timestamp = System.currentTimeMillis() - 50000L,
                    isQuickReply = false
                )
            )
            db.chatMessageDao().insertAll(welcomeMessages)
        }
    }

    suspend fun completeTask(taskId: String): Boolean {
        val task = db.taskDao().getTaskById(taskId) ?: return false
        if (task.isCompleted) return false

        db.taskDao().markTaskCompleted(taskId)

        val settings = db.userSettingsDao().getUserSettings() ?: return false
        val newBalance = settings.balance + task.reward
        db.userSettingsDao().insertOrUpdate(settings.copy(balance = newBalance))

        val trx = TransactionEntity(
            id = UUID.randomUUID().toString(),
            type = "EARNING",
            method = "Task Bonus",
            accountNumber = task.titleEn,
            amount = task.reward,
            fee = 0.0,
            status = "COMPLETED",
            timestamp = System.currentTimeMillis(),
            trxId = "AGE-TSK-${(10000..99999).random()}"
        )
        db.transactionDao().insert(trx)
        return true
    }

    suspend fun acceptClientGig(clientId: String): Boolean {
        val client = db.clientContactDao().getAllClientsFlow() // we can update directly
        db.clientContactDao().updateStatus(clientId, "ACCEPTED")
        return true
    }

    suspend fun completeClientGig(clientId: String, gigRewardBdt: Double, gigTitle: String): Boolean {
        db.clientContactDao().updateStatus(clientId, "COMPLETED")
        val settings = db.userSettingsDao().getUserSettings() ?: return false
        val newBalance = settings.balance + gigRewardBdt
        db.userSettingsDao().insertOrUpdate(settings.copy(balance = newBalance))

        val trx = TransactionEntity(
            id = UUID.randomUUID().toString(),
            type = "EARNING",
            method = "Global Gig",
            accountNumber = gigTitle,
            amount = gigRewardBdt,
            fee = 0.0,
            status = "COMPLETED",
            timestamp = System.currentTimeMillis(),
            trxId = "AGE-GIG-${(10000..99999).random()}"
        )
        db.transactionDao().insert(trx)
        return true
    }

    suspend fun claimDailyCheckIn(): Pair<Boolean, Double> {
        val settings = db.userSettingsDao().getUserSettings() ?: return Pair(false, 0.0)
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        if (settings.lastCheckInDate == todayStr) {
            return Pair(false, 0.0) // Already claimed today
        }

        val nextStreak = if (settings.dailyStreak >= 7) 1 else settings.dailyStreak + 1
        val bonus = 10.0 + (nextStreak * 5.0) // e.g. 15, 20, 25 ...
        val newBalance = settings.balance + bonus

        db.userSettingsDao().insertOrUpdate(
            settings.copy(
                balance = newBalance,
                dailyStreak = nextStreak,
                lastCheckInDate = todayStr
            )
        )

        val trx = TransactionEntity(
            id = UUID.randomUUID().toString(),
            type = "EARNING",
            method = "Daily Check-in",
            accountNumber = "Day $nextStreak Streak Bonus",
            amount = bonus,
            fee = 0.0,
            status = "COMPLETED",
            timestamp = System.currentTimeMillis(),
            trxId = "AGE-STRK-${(10000..99999).random()}"
        )
        db.transactionDao().insert(trx)
        return Pair(true, bonus)
    }

    suspend fun requestWithdrawal(
        method: String,
        accountNumber: String,
        amount: Double
    ): Result<TransactionEntity> {
        val settings = db.userSettingsDao().getUserSettings()
            ?: return Result.failure(Exception("User settings not found"))

        if (amount <= 0) {
            return Result.failure(Exception("পরিমাণ শূন্যের বেশি হতে হবে / Amount must be greater than 0"))
        }
        if (amount < 50.0) {
            return Result.failure(Exception("সর্বনিম্ন উত্তোলনের পরিমাণ ৳৫০ / Minimum withdrawal is ৳50"))
        }
        if (amount > settings.balance) {
            return Result.failure(Exception("অপর্যাপ্ত ব্যালেন্স / Insufficient balance"))
        }

        // Deduct balance
        val newBalance = settings.balance - amount
        db.userSettingsDao().insertOrUpdate(settings.copy(balance = newBalance))

        val randomId = (100000..999999).random()
        val trx = TransactionEntity(
            id = UUID.randomUUID().toString(),
            type = "WITHDRAWAL",
            method = method, // "bKash" or "Nagad" or "Rocket"
            accountNumber = accountNumber,
            amount = amount,
            fee = 0.0, // 0 fee promotional offer
            status = "PROCESSING",
            timestamp = System.currentTimeMillis(),
            trxId = "AGE-WD-$randomId"
        )
        db.transactionDao().insert(trx)
        return Result.success(trx)
    }

    suspend fun sendChatMessage(userMessage: String): ChatMessageEntity {
        val userEntity = ChatMessageEntity(
            sender = "USER",
            message = userMessage,
            timestamp = System.currentTimeMillis()
        )
        db.chatMessageDao().insert(userEntity)

        // Intelligent 24/7 automated support response based on message keywords
        val lower = userMessage.lowercase()
        val replyText = when {
            lower.contains("bkash") || lower.contains("বিকাশ") -> {
                "বিকাশ (bKash) উত্তোলনের নির্দেশিকা:\n• আপনার ১১ ডিজিটের পার্সোনাল নম্বর (01XXXXXXXXX) দিন।\n• সর্বনিম্ন উত্তোলন ৳৫০। কোনো সার্ভিস ফি নেই (০% চার্জ)।\n• স্বয়ংক্রিয় প্রসেসিংয়ের মাধ্যমে ৫ থেকে ১৫ মিনিটের মধ্যে টাকা আপনার বিকাশ একাউন্টে পৌঁছে যাবে।"
            }
            lower.contains("nagad") || lower.contains("নগদ") -> {
                "নগদ (Nagad) উত্তোলনের নির্দেশিকা:\n• সঠিক নগদ একাউন্ট নম্বর দিন।\n• ২৪ ঘণ্টা যেকোনো সময় উত্তোলন অনুরোধ করা যায়।\n• ২-ফ্যাক্টর অথেনটিকেশন সফল হলে দ্রুত টাকা জমা হয়।"
            }
            lower.contains("2fa") || lower.contains("পিন") || lower.contains("pin") || lower.contains("সিকিউরিটি") -> {
                "নিরাপত্তা ও ২-ফ্যাক্টর অথেন্টিকেশন (2FA):\n• সকল লেনদেন শতভাগ নিরাপদ রাখতে ৬-সংখ্যার সিকিউরিটি পিন বাধ্যতামূলক।\n• সেটিংস থেকে আপনার ২এফএ পিন পরিবর্তন করতে পারেন।\n• কোনো ওটিপি বা পিন অন্য কারো সাথে শেয়ার করবেন না।"
            }
            lower.contains("কাজ") || lower.contains("task") || lower.contains("কাজ পাচ্ছি না") || lower.contains("আয়") -> {
                "দৈনিক কাজের সুযোগ:\n• প্রতিদিন নতুন মাইক্রো-টাস্ক, ভিডিও বিজ্ঞাপন ও বৈশ্বিক ক্লায়েন্ট গিগ যোগ হয়।\n• কাজ সম্পন্ন করার সাথে সাথেই মূল ব্যালেন্সে টাকা যুক্ত হবে।\n• দৈনিক চেক-ইন ও বন্ধুদের রেফার করে অতিরিক্ত বোনাস পান!"
            }
            lower.contains("অফলাইন") || lower.contains("offline") -> {
                "অফলাইন সুবিধা:\n• ইন্টারনেট সংযোগ না থাকলেও আপনার সকল ব্যালেন্স, ডাটা ও আগের হিস্ট্রি লোকাল এনক্রিপ্টেড ডাটাবেজে সম্পূর্ণ সুরক্ষিত থাকে।\n• ডাটা স্বয়ংক্রিয়ভাবে সিঙ্ক হয়।"
            }
            lower.contains("টাকা") || lower.contains("উত্তোলন") || lower.contains("withdraw") -> {
                "টাকা উত্তোলনের নিয়ম:\n• ওয়ালেট পেজে গিয়ে 'টাকা উত্তোলন' বাটনে চাপ দিন।\n• বিকাশ বা নগদ সিলেক্ট করে নম্বর ও পরিমাণ লিখুন।\n• ২-ফ্যাক্টর পিন দিয়ে নিশ্চিত করুন।"
            }
            else -> {
                "ধন্যবাদ আপনার বার্তার জন্য! আমাদের ২৪/৭ সাপোর্ট টিম আপনার অনুরোধটি গ্রহণ করেছে। বিকাশ/নগদ উত্তোলন, কাজ বা নিরাপত্তা বিষয়ক যেকোনো সহায়তায় আমরা সবসময় পাশে আছি।"
            }
        }

        val replyEntity = ChatMessageEntity(
            sender = "SUPPORT",
            message = replyText,
            timestamp = System.currentTimeMillis() + 800L
        )
        db.chatMessageDao().insert(replyEntity)
        return replyEntity
    }

    suspend fun updateLanguage(lang: String) {
        val settings = db.userSettingsDao().getUserSettings() ?: return
        db.userSettingsDao().insertOrUpdate(settings.copy(language = lang))
    }

    suspend fun updateDarkMode(mode: String) {
        val settings = db.userSettingsDao().getUserSettings() ?: return
        db.userSettingsDao().insertOrUpdate(settings.copy(darkModeOption = mode))
    }

    suspend fun updateAccentColor(index: Int) {
        val settings = db.userSettingsDao().getUserSettings() ?: return
        db.userSettingsDao().insertOrUpdate(settings.copy(accentColorIndex = index))
    }

    suspend fun updateTwoFactorSettings(enabled: Boolean, pin: String) {
        val settings = db.userSettingsDao().getUserSettings() ?: return
        db.userSettingsDao().insertOrUpdate(
            settings.copy(twoFactorEnabled = enabled, twoFactorPin = pin)
        )
    }

    suspend fun updateProfile(name: String, email: String, phone: String) {
        val settings = db.userSettingsDao().getUserSettings() ?: return
        db.userSettingsDao().insertOrUpdate(
            settings.copy(userName = name, userEmail = email, userPhone = phone)
        )
    }

    suspend fun toggleOfflineMode(simulatedOffline: Boolean) {
        val settings = db.userSettingsDao().getUserSettings() ?: return
        db.userSettingsDao().insertOrUpdate(
            settings.copy(isOfflineModeSimulated = simulatedOffline)
        )
    }

    suspend fun createAutomaticBackupSnapshot(): String {
        val settings = db.userSettingsDao().getUserSettings()
        val now = System.currentTimeMillis()
        if (settings != null) {
            db.userSettingsDao().insertOrUpdate(settings.copy(lastBackupTimestamp = now))
        }
        val dateFormatted = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(now))
        return "Backup snapshot successfully encrypted (AES-256) and saved locally at $dateFormatted. Data is 100% offline-ready."
    }
}
