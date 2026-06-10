package com.example.fotogram.schermate.bacheca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fotogram.repository.PostRepository
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager
import androidx.compose.foundation.lazy.rememberLazyListState

@Composable
fun BachecaScreen(
    onApriDettaglioUtente: (Int) -> Unit,
    onApriImmaginePost: (String) -> Unit,
    onApriMappaPost: (String) -> Unit
) {
    val context = LocalContext.current

    val viewModel: BachecaViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                BachecaViewModel(
                    postRepository = PostRepository(
                        remoteDataSource = RemoteDataSource(),
                        sessioneManager = SessioneManager(context)
                    )
                )
            }
        }
    )

    val postBacheca = viewModel.postBacheca
    val caricamento = viewModel.caricamento
    val messaggioErrore = viewModel.messaggioErrore

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.caricaBacheca()
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (caricamento) {
            item {
                Text(
                    text = "Caricamento bacheca...",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        if (messaggioErrore != null) {
            item {
                Text(
                    text = messaggioErrore,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        if (!caricamento && messaggioErrore == null && postBacheca.isEmpty()) {
            item {
                Text(
                    text = "Nessun post disponibile.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        items(postBacheca) { post ->
            SchedaPost(
                post = post,
                onApriDettaglioUtente = onApriDettaglioUtente,
                onApriImmaginePost = onApriImmaginePost,
                onApriMappaPost = onApriMappaPost
            )
        }
    }
}