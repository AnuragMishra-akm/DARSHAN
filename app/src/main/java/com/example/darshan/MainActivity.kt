package com.example.darshan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.darshan.connect.ConnectScreen
import com.example.darshan.connect.ConnectViewModel
import com.example.darshan.ui.theme.DarshanTheme
import com.example.darshan.video.CallState
import com.example.darshan.video.VideoCallScreen
import com.example.darshan.video.VideoCallViewModel
import io.getstream.video.android.compose.theme.VideoTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DarshanTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ConnectRoute,
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable<ConnectRoute> {
                            val connectViewModel = koinViewModel<ConnectViewModel>()
                            val state = connectViewModel.state

                            LaunchedEffect(key1 = state.isConnected) {
                                if(state.isConnected){
                                    navController.navigate(VideoCallRoute(roomID = state.roomID ?: "main-room")){
                                    popUpTo(ConnectRoute) {
                                        inclusive = true
                                       }
                                    }
                                }
                            }
                            ConnectScreen(
                                state = state,
                                onAction = connectViewModel::onAction
                            )
                        }
                        composable<VideoCallRoute> { backStackEntry ->
                            val route = backStackEntry.toRoute<VideoCallRoute>()
                            val videoCallViewModel = koinViewModel<VideoCallViewModel>(
                                parameters = { parametersOf(route.roomID) }
                            )
                            val state = videoCallViewModel.state
                            LaunchedEffect(key1 = state.callState){
                                if(state.callState == CallState.ENDED){
                                    navController.navigate(ConnectRoute){
                                        popUpTo(VideoCallRoute(route.roomID)){
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                            VideoTheme {
                                VideoCallScreen(
                                    state = state,
                                    onAction = videoCallViewModel::onAction
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}
@Serializable
data object ConnectRoute
@Serializable
data class VideoCallRoute(val roomID: String)