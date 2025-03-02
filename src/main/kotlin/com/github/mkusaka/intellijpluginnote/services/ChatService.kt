package com.github.mkusaka.intellijpluginnote.services

import com.github.mkusaka.intellijpluginnote.models.ChatMessage
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

/**
 * チャット機能を提供するサービス
 * メッセージの保存、取得、送信などの機能を提供します
 */
@Service(Service.Level.PROJECT)
class ChatService(private val project: Project) {
    
    // メッセージの履歴
    private val messages = mutableListOf<ChatMessage>()
    
    // メッセージ更新時のリスナー
    private val listeners = mutableListOf<(List<ChatMessage>) -> Unit>()
    
    /**
     * すべてのメッセージを取得します
     */
    fun getAllMessages(): List<ChatMessage> = messages.toList()
    
    /**
     * 新しいメッセージを追加します
     */
    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyListeners()
    }
    
    /**
     * ユーザーからのメッセージを追加します
     */
    fun sendUserMessage(content: String) {
        val message = ChatMessage(
            content = content,
            sender = "user"
        )
        addMessage(message)
        
        // ここで応答を生成したり、外部APIと連携したりする処理を追加できます
        // デモ用に簡単な応答を返します
        respondToUserMessage(content)
    }
    
    /**
     * システムからの応答メッセージを追加します
     */
    private fun respondToUserMessage(userMessage: String) {
        // 簡単なエコー応答（実際の実装ではAIとの連携などを行う）
        val response = "あなたのメッセージを受け取りました: $userMessage"
        val message = ChatMessage(
            content = response,
            sender = "system"
        )
        addMessage(message)
    }
    
    /**
     * チャット履歴をクリアします
     */
    fun clearMessages() {
        messages.clear()
        notifyListeners()
    }
    
    /**
     * メッセージ更新のリスナーを追加します
     */
    fun addListener(listener: (List<ChatMessage>) -> Unit) {
        listeners.add(listener)
        listener(getAllMessages()) // 初期状態を通知
    }
    
    /**
     * メッセージ更新のリスナーを削除します
     */
    fun removeListener(listener: (List<ChatMessage>) -> Unit) {
        listeners.remove(listener)
    }
    
    /**
     * すべてのリスナーに変更を通知します
     */
    private fun notifyListeners() {
        val currentMessages = getAllMessages()
        listeners.forEach { it(currentMessages) }
    }
    
    companion object {
        /**
         * プロジェクトからChatServiceのインスタンスを取得します
         */
        fun getInstance(project: Project): ChatService = project.service()
    }
} 
