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
import androidx.compose.ui.unit.dp
import com.example.fotogram.schermate.bacheca.SchedaPost
import com.example.fotogram.model.Utente
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fotogram.sessione.SessioneManager

@Composable
fun ProfiloScreen(
    onModificaProfilo: () -> Unit,
    onApriImmaginePost: (String) -> Unit,
    onApriMappaPost: (String) -> Unit,
    onLogout: () -> Unit //da togliere alla fine
) {
    val context = LocalContext.current

    val viewModel: ProfiloViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                ProfiloViewModel(
                    sessioneManager = SessioneManager(context)
                )
            }
        }
    )

    val utente = viewModel.utente
    val postPersonali = viewModel.postPersonali
    val logoutCompletato = viewModel.logoutCompletato //da togliere alla fine

    LaunchedEffect(logoutCompletato) {
        if (logoutCompletato) {
            onLogout()
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            IntestazioneProfilo(
                utente = utente,
                onModificaProfilo = onModificaProfilo,
                onLogout = viewModel::logout
            )
        }

        item {
            Text(
                text = "I tuoi post",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
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
    }
}

@Composable
fun IntestazioneProfilo(
    utente: Utente,
    onModificaProfilo: () -> Unit,
    onLogout: () -> Unit //da togliere alla fine
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

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        //da togliere alla fine
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
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