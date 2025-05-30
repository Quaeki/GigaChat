package com.kaiser.gigachat.presentation.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data model for chat history
data class ChatHistoryItem(val id: Int, val title: String, val isDate: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val state = viewModel.state.value
    val listState = rememberLazyListState()
    val chatHistory = remember {
        mutableStateOf(emptyList<ChatHistoryItem>()) // История чата очищена
    }
    val selectedChat = remember { mutableStateOf<ChatHistoryItem?>(null) }
    val isHistoryVisible = remember { mutableStateOf(false) }

    // Прокрутка к последнему сообщению при обновлении списка сообщений
    LaunchedEffect(state.messages) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Верхняя панель
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {}
                },
                navigationIcon = {
                    IconButton(onClick = { isHistoryVisible.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Действие */ }) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )

            // Список сообщений
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black),
                state = listState,
                contentPadding = PaddingValues(vertical = 8.dp),
                reverseLayout = true
            ) {
                items(state.messages) { message ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        if (message.userMessage.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .background(
                                        Color.DarkGray,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = message.userMessage,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp
                                )
                            }
                        }
                        if (message.aiResponse.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .background(
                                        Color.DarkGray,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = message.aiResponse,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp
                                )
                            }
                        }
                    }
                }
            }

            // Индикатор загрузки
            AnimatedVisibility(
                visible = state.isLoading,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp)
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Поле ввода и кнопки внизу
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Действие */ }) {
                    Icon(
                        imageVector = Icons.Rounded.Build,
                        contentDescription = "Image",
                        tint = Color.White
                    )
                }
                BasicTextField(
                    value = state.inputText,
                    onValueChange = { viewModel.onMessageInputChanged(it) },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF2A2A2A), RoundedCornerShape(24.dp))
                        .padding(12.dp),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp
                    ),
                    decorationBox = { innerTextField ->
                        if (state.inputText.isEmpty()) {
                            Text(
                                "Сообщение",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    },
                    enabled = !state.isLoading
                )
                IconButton(onClick = { viewModel.onSendMessage() }) {
                    Icon(
                        imageVector = Icons.Rounded.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }

        // Боковая панель с историей чатов
        AnimatedVisibility(
            visible = isHistoryVisible.value,
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = slideOutHorizontally(targetOffsetX = { -it }),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight()
                    .background(Color.Black)
                    .padding(16.dp)
            ) {
                // Верхняя часть с "Поиск" и иконкой закрытия
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Поиск",
                        color = Color(0xFF8E8E9E),
                        fontSize = 16.sp
                    )
                    IconButton(
                        onClick = { isHistoryVisible.value = false },
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close history",
                            tint = Color.White
                        )
                    }
                }

                // Список чатов
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(chatHistory.value) { chat ->
                        if (chat.isDate) {
                            // Временные метки (например, "Сегодня", "Вчера")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(1.dp)
                                        .background(Color(0xFF8E8E9E))
                                )
                                Text(
                                    text = chat.title,
                                    color = Color(0xFF8E8E9E),
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(1.dp)
                                        .background(Color(0xFF8E8E9E))
                                )
                            }
                        } else {
                            // Элемент чата
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (selectedChat.value?.id == chat.id) {
                                            Color(0xFF2A2A2A)
                                        } else {
                                            Color.Transparent
                                        }
                                    )
                                    .clickable { selectedChat.value = chat }
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = chat.title,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                // Кнопка для очистки истории
                Button(
                    onClick = {
                        chatHistory.value = emptyList()
                        selectedChat.value = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF4D4D),
                        contentColor = Color.White
                    )
                ) {
                    Text("Очистить историю")
                }

                // Нижняя часть с аватаром и именем
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFFF4D4D), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "A",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Anton Kuricin",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /* Действие */ }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Create",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}