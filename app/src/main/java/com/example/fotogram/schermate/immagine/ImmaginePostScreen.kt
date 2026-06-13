package com.example.fotogram.schermate.immagine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fotogram.database.AppDatabase
import com.example.fotogram.repository.PostRepository
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager
import com.example.fotogram.util.base64ToImageBitmap

@Composable
fun ImmaginePostScreen(
    idPost: String
) {
    val context = LocalContext.current

    val viewModel: ImmaginePostViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                ImmaginePostViewModel(
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

    val immaginePost = remember(post?.immagineBase64) {
        base64ToImageBitmap(post?.immagineBase64)
    }

    LaunchedEffect(idPost) {
        viewModel.caricaPost(idPost)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            caricamento -> {
                Text("Caricamento immagine...")
            }

            messaggioErrore != null -> {
                Text(
                    text = messaggioErrore,
                    color = MaterialTheme.colorScheme.error
                )
            }

            immaginePost != null -> {
                Image(
                    bitmap = immaginePost,
                    contentDescription = "Immagine del post",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            else -> {
                Text("Immagine non disponibile")
            }
        }
    }
}