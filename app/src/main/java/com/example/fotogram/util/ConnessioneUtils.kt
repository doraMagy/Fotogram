package com.example.fotogram.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

fun controllaConnessione(context: Context): Boolean {
    val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    val reteAttiva = connectivityManager.activeNetwork ?: return false

    val capacitaRete = connectivityManager.getNetworkCapabilities(
        reteAttiva
    ) ?: return false

    return capacitaRete.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capacitaRete.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

@Composable
fun rememberStatoConnessione(): Boolean {
    val context = LocalContext.current

    var connesso by remember {
        mutableStateOf(controllaConnessione(context))
    }

    DisposableEffect(context) {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val mainHandler = Handler(Looper.getMainLooper())

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                mainHandler.post {
                    connesso = controllaConnessione(context)
                }
            }

            override fun onLost(network: Network) {
                mainHandler.post {
                    connesso = controllaConnessione(context)
                }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                mainHandler.post {
                    connesso = controllaConnessione(context)
                }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        onDispose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    return connesso
}