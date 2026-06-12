package com.example.fotogram.schermate.profilo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fotogram.schermate.bacheca.SchedaPost
import com.example.fotogram.model.Utente
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fotogram.database.AppDatabase
import com.example.fotogram.repository.PostRepository
import com.example.fotogram.repository.UtenteRepository
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

@Composable
fun ProfiloScreen(
    onModificaProfilo: () -> Unit,
    onApriImmaginePost: (String) -> Unit,
    onApriMappaPost: (String) -> Unit,
) {
    val context = LocalContext.current

    val viewModel: ProfiloViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                ProfiloViewModel(
                    utenteRepository = UtenteRepository(
                        remoteDataSource = RemoteDataSource(),
                        sessioneManager = SessioneManager(context)
                    ),
                    postRepository = PostRepository(
                        remoteDataSource = RemoteDataSource(),
                        sessioneManager = SessioneManager(context),
                        postDao = AppDatabase.getDatabase(context).postDao()
                    )
                )
            }
        }
    )

    val utente = viewModel.utente
    val postPersonali = viewModel.postPersonali
    val caricamento = viewModel.caricamento
    val messaggioErrore = viewModel.messaggioErrore

    val caricamentoAltriPost = viewModel.caricamentoAltriPost
    val finePost = viewModel.finePost

    val listState = rememberLazyListState()

    val vicinoAlFondo = remember {
        derivedStateOf {
            val ultimoVisibile = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totaleElementi = listState.layoutInfo.totalItemsCount

            totaleElementi > 0 && ultimoVisibile >= totaleElementi - 3
        }
    }

    LaunchedEffect(Unit) {
        viewModel.caricaProfilo()
    }

    LaunchedEffect(vicinoAlFondo.value) {
        if (vicinoAlFondo.value) {
            viewModel.caricaAltriPost()
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (caricamento) {
            item {
                Text(
                    text = "Caricamento profilo...",
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

        item {
            IntestazioneProfilo(
                utente = utente,
                onModificaProfilo = onModificaProfilo
            )
        }

        item {
            Text(
                text = "I tuoi post",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (!caricamento && postPersonali.isEmpty()) {
            item {
                Text(
                    text = "Non hai ancora pubblicato post.",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        items(postPersonali) { post ->
            SchedaPost(
                post = post,
                onApriDettaglioUtente = {
                    // Nel profilo personale non serve aprire il dettaglio utente.
                },
                onApriImmaginePost = onApriImmaginePost,
                onApriMappaPost = onApriMappaPost,
                mostraAutoreCliccabile = false,
                mostraBadge = false
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
    }
}

@Composable
fun IntestazioneProfilo(
    utente: Utente,
    onModificaProfilo: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("IMG")
            }

            Spacer(
                modifier = Modifier.width(20.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = utente.nomeUtente,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatisticaProfilo(
                        valore = utente.numeroPost,
                        etichetta = "Post"
                    )

                    StatisticaProfilo(
                        valore = utente.numeroFollower,
                        etichetta = "Follower"
                    )

                    StatisticaProfilo(
                        valore = utente.numeroFollowing,
                        etichetta = "Following"
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = utente.bio,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Data di nascita: ${utente.dataNascita}",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = onModificaProfilo,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Modifica profilo")
        }
    }
}

@Composable
fun StatisticaProfilo(
    valore: Int,
    etichetta: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = valore.toString(),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = etichetta,
            style = MaterialTheme.typography.bodySmall
        )
    }
}