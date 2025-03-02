package com.github.mkusaka.intellijpluginnote.models

import java.time.LocalDateTime

/**
 * チャットメッセージを表すデータクラス
 *
 * @property id メッセージの一意の識別子
 * @property content メッセージの内容
 * @property sender 送信者（"user" または "system"）
 * @property timestamp メッセージが送信された時刻
 */
data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val sender: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
) 
