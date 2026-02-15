package com.example.darshan.connect

data class ConnectState(
    val name: String? = null,
    val roomID: String? = null,
    val isCreateMode: Boolean = true,
    val isConnected: Boolean = false,
    val errorMessage: String? = null,
    val shareUrl: String? = null
)
