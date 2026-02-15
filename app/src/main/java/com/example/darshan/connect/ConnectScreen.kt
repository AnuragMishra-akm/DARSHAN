package com.example.darshan.connect

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConnectScreen(
    state: ConnectState,
    onAction: (ConnectAction) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Darshan",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onAction(ConnectAction.OnModeChange(true)) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.isCreateMode) MaterialTheme.colorScheme.primary else Color.Gray
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Create Meeting")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { onAction(ConnectAction.OnModeChange(false)) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!state.isCreateMode) MaterialTheme.colorScheme.primary else Color.Gray
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Join Meeting")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Your Name", fontSize = 18.sp)
        TextField(
            value = state.name ?: "",
            onValueChange = { onAction(ConnectAction.OnNameChange(it)) },
            placeholder = { Text(text = "Enter your name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isCreateMode) {
            Text("Room ID", fontSize = 18.sp)
            TextField(
                value = state.roomID ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(text = "Click Generate to get ID") },
                trailingIcon = {
                    if (state.roomID?.isNotBlank() == true) {
                        IconButton(onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Room ID", state.roomID)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Room ID copied", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onAction(ConnectAction.OnGenerateRoomID) }) {
                    Text("Generate ID")
                }
                
                if (state.roomID?.isNotBlank() == true) {
                    OutlinedButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Join my Darshan meeting!\nRoom ID: ${state.roomID}\nJoin here: https://darshan.app/join?roomID=${state.roomID}")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Meeting Link"))
                        }
                    ) {
                        Text("Share Link")
                    }
                }
            }
        } else {
            Text("Enter Room ID to Join", fontSize = 18.sp)
            TextField(
                value = state.roomID ?: "",
                onValueChange = { onAction(ConnectAction.OnRoomIDChange(it)) },
                placeholder = { Text(text = "Room ID") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onAction(ConnectAction.OnConnectClick) },
            enabled = state.roomID?.isNotBlank() == true,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (state.isCreateMode) "Start Meeting" else "Join Meeting")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = state.errorMessage ?: "",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.error
        )
    }
}