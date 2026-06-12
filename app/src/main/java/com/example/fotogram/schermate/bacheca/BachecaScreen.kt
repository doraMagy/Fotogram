package com.example.fotogram.schermate.bacheca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fotogram.repository.PostRepository
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import com.example.fotogram.database.AppDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BachecaScreen(
    onApriDettaglioUtente: (Int) -> Unit,
    onApriImmaginePost: (String) -> Unit,
    onApriMappaPost: (String) -> Unit,
    idAutoreFollowAggiornato: Int?,
    seguitoAggiornato: Boolean?,
    onFollowAggiornatoGestito: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: BachecaViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                BachecaViewModel(
                    postRepository = PostRepository(
                        remoteDataSource = RemoteDataSource(),
                        sessioneManager = SessioneManager(context),
                        postDao = AppDatabase.getDatabase(context).postDao()
                    )
                )
            }
        }
    )

    val postBacheca = viewModel.postBacheca
    val caricamento = viewModel.caricamento
    val aggiornamento = viewModel.aggiornamento
    val caricamentoAltriPost = viewModel.caricamentoAltriPost
    val fineFeed = viewModel.fineFeed
    val messaggioErrore = viewModel.messaggioErrore

    val listState = rememberLazyListState()

    val vicinoAlFondo = remember {
        derivedStateOf {
            val ultimoVisibile = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totaleElementi = listState.layoutInfo.totalItemsCount

            totaleElementi > 0 && ultimoVisibile >= totaleElementi - 3
        }
    }

    LaunchedEffect(Unit) {
        viewModel.caricaBacheca()
    }

    LaunchedEffect(vicinoAlFondo.value) {
        if (vicinoAlFondo.value) {
            viewModel.caricaAltriPost()
        }
    }

    LaunchedEffect(idAutoreFollowAggiornato, seguitoAggiornato) {
        if (idAutoreFollowAggiornato != null && seguitoAggiornato != null) {
            viewModel.aggiornaFollowAutore(
                idAutore = idAutoreFollowAggiornato,
                seguito = seguitoAggiornato
            )

            onFollowAggiornatoGestito()
        }
    }

    PullToRefreshBox(
        isRefreshing = aggiornamento,
        onRefresh = {
            viewModel.aggiornaBacheca()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
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

            items(
                items = postBacheca,
                key = { post -> post.idPost }
            ) { post ->
                SchedaPost(
                    post = post,
                    onApriDettaglioUtente = onApriDettaglioUtente,
                    onApriImmaginePost = onApriImmaginePost,
                    onApriMappaPost = onApriMappaPost
                )
            }

            if (caricamentoAltriPost) {
                item {
                    Text(
                        text = "Caricamento altri post...",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            if (fineFeed && postBacheca.isNotEmpty()) {
                item {
                    Text(
                        text = "Hai visto tutti i post disponibili.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}