package com.github.mkusaka.intellijpluginnote.toolWindow.components

import com.github.mkusaka.intellijpluginnote.MyBundle
import com.github.mkusaka.intellijpluginnote.models.ChatMessage
import com.github.mkusaka.intellijpluginnote.services.ChatService
import com.intellij.openapi.project.Project
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * チャットUI全体を表示するパネル
 */
class ChatPanel(project: Project) : JBPanel<ChatPanel>(BorderLayout()) {
    
    private val chatService = ChatService.getInstance(project)
    private val messageListPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        background = JBColor.background()
    }
    private val inputField = JTextField().apply {
        preferredSize = Dimension(Integer.MAX_VALUE, 30)
        minimumSize = Dimension(100, 30)
        toolTipText = MyBundle.message("chat.placeholder")
    }
    private val sendButton = JButton(MyBundle.message("chat.sendButton")).apply {
        preferredSize = Dimension(80, 30)
    }
    
    init {
        setupUI()
        setupListeners()
    }
    
    private fun setupUI() {
        // メッセージリストを表示するスクロールパネル
        val scrollPane = JBScrollPane(messageListPanel).apply {
            border = JBUI.Borders.empty(10)
            verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        }
        
        // 入力エリア（テキストフィールドと送信ボタン）
        val inputPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
            border = JBUI.Borders.empty(0, 10, 10, 10)
            add(inputField, BorderLayout.CENTER)
            add(sendButton, BorderLayout.EAST)
        }
        
        // 全体のレイアウト
        add(scrollPane, BorderLayout.CENTER)
        add(inputPanel, BorderLayout.SOUTH)
        
        // 初期サイズ設定
        preferredSize = Dimension(400, 500)
    }
    
    private fun setupListeners() {
        // メッセージ送信処理
        val sendAction = {
            val text = inputField.text.trim()
            if (text.isNotEmpty()) {
                chatService.sendUserMessage(text)
                inputField.text = ""
            }
        }
        
        // 送信ボタンのクリックイベント
        sendButton.addActionListener { sendAction() }
        
        // テキストフィールドのEnterキーイベント
        inputField.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_ENTER && !e.isShiftDown) {
                    e.consume()
                    sendAction()
                }
            }
        })
        
        // チャットサービスからのメッセージ更新イベント
        chatService.addListener { messages ->
            updateMessageList(messages)
        }
    }
    
    /**
     * メッセージリストを更新します
     */
    private fun updateMessageList(messages: List<ChatMessage>) {
        messageListPanel.removeAll()
        
        if (messages.isEmpty()) {
            // 空の状態を表示
            val emptyLabel = com.intellij.ui.components.JBLabel(MyBundle.message("chat.emptyState"))
            emptyLabel.horizontalAlignment = javax.swing.SwingConstants.CENTER
            messageListPanel.add(Box.createVerticalGlue())
            messageListPanel.add(emptyLabel)
            messageListPanel.add(Box.createVerticalGlue())
        } else {
            messages.forEach { message ->
                messageListPanel.add(ChatMessageItem(message))
            }
            // 最下部に余白を追加
            messageListPanel.add(Box.createVerticalStrut(10))
        }
        
        // UIを更新
        messageListPanel.revalidate()
        messageListPanel.repaint()
        
        // 最新のメッセージが見えるようにスクロール
        messageListPanel.scrollRectToVisible(messageListPanel.bounds)
    }
} 
