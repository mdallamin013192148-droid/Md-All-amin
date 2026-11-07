package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val balance: Double = 120.0,
    val userName: String = "Alamin Hossain",
    val userEmail: String = "alamin.worker@globalearn.net",
    val userPhone: String = "01712345678",
    val country: String = "Bangladesh",
    val referralCode: String = "ALAMIN-EARN",
    val referredCount: Int = 5,
    val referralEarnings: Double = 50.0,
    val dailyStreak: Int = 3,
    val lastCheckInDate: String = "",
    val twoFactorEnabled: Boolean = true,
    val twoFactorPin: String = "123456",
    val biometricLockEnabled: Boolean = false,
    val language: String = "bn", // "bn" or "en"
    val darkModeOption: String = "system", // "light", "dark", "system"
    val accentColorIndex: Int = 0, // 0: Deep Navy, 1: Emerald Green, 2: Royal Blue, 3: Sunset Crimson
    val isOfflineModeSimulated: Boolean = false,
    val lastBackupTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val titleBn: String,
    val titleEn: String,
    val descBn: String,
    val descEn: String,
    val category: String, // "micro_task", "ad_reward", "survey", "global_gig"
    val reward: Double,
    val country: String,
    val isCompleted: Boolean = false,
    val durationSeconds: Int = 0,
    val clientName: String = "Global Client"
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val type: String, // "WITHDRAWAL", "EARNING"
    val method: String, // "bKash", "Nagad", "Rocket", "Task Bonus", "Referral", "Daily Check-in"
    val accountNumber: String,
    val amount: Double,
    val fee: Double = 0.0,
    val status: String, // "COMPLETED", "PROCESSING", "PENDING"
    val timestamp: Long = System.currentTimeMillis(),
    val trxId: String
)

@Entity(tableName = "client_contacts")
data class ClientContactEntity(
    @PrimaryKey val id: String,
    val clientName: String,
    val company: String,
    val country: String,
    val flag: String,
    val role: String,
    val offerTitle: String,
    val rateUsd: Double,
    val rateBdt: Double,
    val requirements: String,
    val contactEmail: String,
    val status: String = "AVAILABLE" // "AVAILABLE", "ACCEPTED", "COMPLETED"
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "USER" or "SUPPORT"
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isQuickReply: Boolean = false
)
