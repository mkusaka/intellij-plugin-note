package com.github.mkusaka.intellijpluginnote.toolWindow.components

import com.github.mkusaka.intellijpluginnote.MyBundle
import com.github.mkusaka.intellijpluginnote.models.ChatMessage
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Font
import java.time.format.DateTimeFormatter
import javax.swing.BorderFactory
import javax.swing.JTextArea
import javax.swing.SwingConstants

/**
 * 個々のチャットメッセージを表示するコンポーネント
 */
class ChatMessageItem(private val message: ChatMessage) : JBPanel<ChatMessageItem>(BorderLayout()) {
    
    init {
        setupUI()
    }
    
    private fun setupUI() {
        // メッセージの配置とスタイルを送信者に応じて変更
        val isUser = message.sender == "user"
        
        // パネルの設定
        border = BorderFactory.createEmptyBorder(5, 10, 5, 10)
        background = JBColor.background()
        
        // メッセージ内容
        val contentArea = JTextArea(message.content).apply {
            isEditable = false
            lineWrap = true
            wrapStyleWord = true
            background = if (isUser) JBColor(0xE3F2FD, 0x263238) else JBColor(0xF5F5F5, 0x1E1E1E)
            foreground = JBColor.foreground()
            font = font.deriveFont(Font.PLAIN, 12f)
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(JBColor(0xBDBDBD, 0x3E3E3E), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            )
            margin = JBUI.insets(5)
        }
        
        // 送信者と時刻の表示
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val timeLabel = JBLabel(message.timestamp.format(formatter), SwingConstants.RIGHT).apply {
            font = font.deriveFont(Font.ITALIC, 10f)
            foreground = JBColor.gray
            border = BorderFactory.createEmptyBorder(0, 0, 2, 0)
        }
        
        val senderText = if (isUser) MyBundle.message("chat.user") else MyBundle.message("chat.system")
        val senderLabel = JBLabel(senderText).apply {
            font = font.deriveFont(Font.BOLD, 11f)
            foreground = if (isUser) JBColor(0x1976D2, 0x64B5F6) else JBColor(0x424242, 0xBDBDBD)
            border = BorderFactory.createEmptyBorder(0, 0, 2, 5)
        }
        
        // レイアウト
        val headerPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
            background = JBColor.background()
            add(senderLabel, BorderLayout.WEST)
            add(timeLabel, BorderLayout.EAST)
        }
        
        val contentPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
            background = JBColor.background()
            add(contentArea, BorderLayout.CENTER)
        }
        
        // 全体の追加
        add(headerPanel, BorderLayout.NORTH)
        add(contentPanel, BorderLayout.CENTER)
    }
} 
