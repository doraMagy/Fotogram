package com.example.fotogram.schermate.mappa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@Composable
fun SelezionaPosizioneScreen(
    onConfermaPosizione: (Double, Double) -> Unit
) {
    val posizioneIniziale = remember {
        Point.fromLngLat(9.1900, 45.4642) // Milano
    }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(posizioneIniziale)
            zoom(13.0)
            pitch(0.0)
            bearing(0.0)
        }
    }

    val context = LocalContext.current

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    fun centraSuPosizioneUtente() {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location ->
            if (location != null) {
                val puntoUtente = Point.fromLngLat(
                    location.longitude,
                    location.latitude
                )

                mapViewportState.setCameraOptions {
                    center(puntoUtente)
                    zoom(15.0)
                }

                Log.d(
                    "SelezionaPosizione",
                    "Mappa centrata su utente: ${location.latitude}, ${location.longitude}"
                )
            } else {
                Log.d("SelezionaPosizione", "Posizione utente non disponibile")
            }
        }.addOnFailureListener { errore ->
            Log.d("SelezionaPosizione", "Errore posizione: ${errore.message}")
        }
    }

    val launcherPermessoPosizione = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { permessoConcesso ->
        if (permessoConcesso) {
            centraSuPosizioneUtente()
        } else {
            Log.d("SelezionaPosizione", "Permesso posizione negato")
        }
    }

    LaunchedEffect(Unit) {
        val permesso = Manifest.permission.ACCESS_FINE_LOCATION

        val permessoGiaConcesso = ContextCompat.checkSelfPermission(
            context,
            permesso
        ) == PackageManager.PERMISSION_GRANTED

        if (permessoGiaConcesso) {
            centraSuPosizioneUtente()
        } else {
            launcherPermessoPosizione.launch(permesso)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState
        )

        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Marker centrale",
            modifier = Modifier.align(Alignment.Center),
            tint = Color.Red
        )

        Text(
            text = "Sposta la mappa e conferma la posizione",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        FloatingActionButton(
            onClick = {
                val centro = mapViewportState.cameraState?.center

                if (centro != null) {
                    onConfermaPosizione(
                        centro.latitude(),
                        centro.longitude()
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Conferma posizione"
            )
        }
    }
}