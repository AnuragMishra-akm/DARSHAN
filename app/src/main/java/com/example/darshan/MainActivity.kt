package com.example.darshan

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.darshan.connect.ConnectAction
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
    private var isPipMode by mutableStateOf(false)
    private var canEnterPip by mutableStateOf(false)

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
                        modifier = if (isPipMode) Modifier else Modifier.padding(innerPadding)
                    ){
                        composable<ConnectRoute> {
                            val connectViewModel = koinViewModel<ConnectViewModel>()
                            val state = connectViewModel.state

                            LaunchedEffect(Unit) {
                                intent?.data?.getQueryParameter("roomID")?.let { id ->
                                    connectViewModel.onAction(ConnectAction.OnRoomIDChange(id))
                                    connectViewModel.onAction(ConnectAction.OnModeChange(false))
                                }
                            }

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

                            DisposableEffect(Unit) {
                                canEnterPip = true
                                onDispose { canEnterPip = false }
                            }

                            LaunchedEffect(key1 = state.callState){
                                if(state.callState == CallState.ENDED){
                                    if (isPipMode) {
                                        val intent = Intent(this@MainActivity, MainActivity::class.java).apply {
                                            flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                        }
                                        startActivity(intent)
                                    }
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
                                    onAction = videoCallViewModel::onAction,
                                    onBackPressed = {
                                        enterPip()
                                    }
                                )
                            }
                        }

                    }
                }
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (canEnterPip) {
            enterPip()
        }
    }

//    @Suppress("DEPRECATION")
//    override fun onBackPressed() {
//        if (!isPipMode) {
//             enterPip()
//        } else {
//            super.onBackPressed()
//        }
//    }

    private fun enterPip() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(Rational(9, 16))
                .build()
            enterPictureInPictureMode(params)
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isPipMode = isInPictureInPictureMode
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
@Serializable
data object ConnectRoute
@Serializable
data class VideoCallRoute(val roomID: String)
