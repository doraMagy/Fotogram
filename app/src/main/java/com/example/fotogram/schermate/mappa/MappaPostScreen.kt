package com.example.fotogram.schermate.mappa

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fotogram.R
import com.example.fotogram.database.AppDatabase
import com.example.fotogram.repository.PostRepository
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.mapbox.geojson.Point
import com.mapbox.maps.CoordinateBounds
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MappaPostScreen(
    idPost: String
) {
    val context = LocalContext.current

    val viewModel: MappaPostViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                MappaPostViewModel(
                    postRepository = PostRepository(
                        remoteDataSource = RemoteDataSource(),
                        sessioneManager = SessioneManager(context),
                        postDao = AppDatabase.getDatabase(context).postDao()
                    )
                )
            }
        }
    )

    val post = viewModel.post
    val caricamento = viewModel.caricamento
    val messaggioErrore = viewModel.messaggioErrore

    LaunchedEffect(idPost) {
        viewModel.caricaPost(idPost)
    }

    when {
        caricamento -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Caricamento mappa...")
            }
        }

        messaggioErrore != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = messaggioErrore,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        post != null -> {
            MappaPostContenuto(
                latitudinePost = post.latitudine,
                longitudinePost = post.longitudine
            )
        }
    }
}

@Composable
private fun MappaPostContenuto(
    latitudinePost: Double?,
    longitudinePost: Double?
) {
    if (latitudinePost == null || longitudinePost == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Posizione non disponibile")
        }
        return
    }

    val context = LocalContext.current

    val puntoPost = remember(latitudinePost, longitudinePost) {
        Point.fromLngLat(longitudinePost, latitudinePost)
    }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(puntoPost)
            zoom(14.0)
            pitch(0.0)
            bearing(0.0)
        }
    }

    var posizioneUtente by remember { mutableStateOf<Location?>(null) }
    var utenteVicino by remember { mutableStateOf(false) }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    fun aggiornaVicinanza(location: Location) {
        val risultati = FloatArray(1)

        Location.distanceBetween(
            location.latitude,
            location.longitude,
            latitudinePost,
            longitudinePost,
            risultati
        )

        val distanzaMetri = risultati[0]
        utenteVicino = distanzaMetri <= 5_000f

        Log.d(
            "MappaPostScreen",
            "Distanza dal post: $distanzaMetri metri, vicino = $utenteVicino"
        )
    }

    @SuppressLint("MissingPermission")
    fun caricaPosizioneUtente() {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location ->
            if (location != null) {
                posizioneUtente = location
                aggiornaVicinanza(location)
            }
        }.addOnFailureListener { errore ->
            Log.d("MappaPostScreen", "Errore posizione utente: ${errore.message}")
        }
    }

    val launcherPermessoPosizione = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { permessoConcesso ->
        if (permessoConcesso) {
            caricaPosizioneUtente()
        } else {
            Log.d("MappaPostScreen", "Permesso posizione negato")
        }
    }

    LaunchedEffect(Unit) {
        val permesso = Manifest.permission.ACCESS_FINE_LOCATION

        val permessoGiaConcesso = ContextCompat.checkSelfPermission(
            context,
            permesso
        ) == PackageManager.PERMISSION_GRANTED

        if (permessoGiaConcesso) {
            caricaPosizioneUtente()
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
        ) {
            val markerPost = rememberIconImage(
                key = R.drawable.ic_post_marker,
                painter = painterResource(R.drawable.ic_post_marker)
            )

            PointAnnotation(
                point = puntoPost
            ) {
                iconImage = markerPost
                iconSize = 1.2
            }

            MapEffect(utenteVicino, posizioneUtente) { mapView ->
                if (utenteVicino && posizioneUtente != null) {
                    mapView.location.updateSettings {
                        locationPuck = createDefault2DPuck(withBearing = true)
                        puckBearingEnabled = true
                        puckBearing = PuckBearing.HEADING
                        enabled = true
                    }

                    val puntoUtente = Point.fromLngLat(
                        posizioneUtente!!.longitude,
                        posizioneUtente!!.latitude
                    )

                    val bounds = CoordinateBounds(
                        Point.fromLngLat(
                            minOf(puntoPost.longitude(), puntoUtente.longitude()),
                            minOf(puntoPost.latitude(), puntoUtente.latitude())
                        ),
                        Point.fromLngLat(
                            maxOf(puntoPost.longitude(), puntoUtente.longitude()),
                            maxOf(puntoPost.latitude(), puntoUtente.latitude())
                        )
                    )

                    val cameraOptions = mapView.mapboxMap.cameraForCoordinateBounds(
                        bounds,
                        EdgeInsets(120.0, 120.0, 120.0, 120.0),
                        null,
                        null
                    )

                    mapView.mapboxMap.flyTo(cameraOptions)
                } else {
                    mapView.location.updateSettings {
                        enabled = false
                    }
                }
            }
        }
    }
}