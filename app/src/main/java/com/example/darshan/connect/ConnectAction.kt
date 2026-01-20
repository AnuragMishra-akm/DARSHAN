package com.example.darshan.connect

interface ConnectAction {
    data class OnNameChange(val name: String) : ConnectAction
    data object OnConnectClick : ConnectAction
//    object OnDisconnect : ConnectAction
}