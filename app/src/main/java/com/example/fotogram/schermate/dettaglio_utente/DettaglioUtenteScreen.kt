package com.example.fotogram.schermate.dettaglio_utente

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.fotogram.model.Post
import com.example.fotogram.model.Utente
import com.example.fotogram.schermate.bacheca.SchedaPost

@Composable
fun DettaglioUtenteScreen(
    nomeUtente: String,
    onApriImmaginePost: (String) -> Unit,
    onApriMappaPost: (String) -> Unit
) {
    var seguito by remember {
        mutableStateOf(false)
    }

    val utenteDemo = Utente(
        nomeUtente = nomeUtente,
        bio = "Bio di $nomeUtente",
        dataNascita = "20/05/2001",
        numeroFollower = 42,
        numeroFollowing = 18,
        numeroPost = 2
    )

    val postUtente = listOf(
        Post(
            idPost = "post_${nomeUtente}_1",
            nomeAutore = nomeUtente,
            testo = "Post pubblicato da $nomeUtente",
            seguito = seguito,
            haPosizione = true,
            dataCreazione = "2026-06-07"
        ),
        Post(
            idPost = "post_${nomeUtente}_2",
            nomeAutore = nomeUtente,
            testo = "Secondo post di esempio.",
            seguito = seguito,
            haPosizione = false,
            dataCreazione = "2026-06-06"
        )
    )

    LazyColumn(
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            IntestazioneDettaglioUtente(
                utente = utenteDemo,
                seguito = seguito,
                onCambiaFollow = {
                    seguito = !seguito
                }
            )
        }

        item {
            Text(
                text = "Post di $nomeUtente",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        items(postUtente) { post ->
            SchedaPost(
                post = post,
                onApriDettaglioUtente = {
                    // Siamo già nel dettaglio di questo utente.
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
fun IntestazioneDettaglioUtente(
    utente: Utente,
    seguito: Boolean,
    onCambiaFollow: () -> Unit
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
                    StatisticaDettaglioUtente(
                        valore = utente.numeroPost,
                        etichetta = "Post"
                    )

                    StatisticaDettaglioUtente(
                        valore = utente.numeroFollower,
                        etichetta = "Follower"
                    )

                    StatisticaDettaglioUtente(
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

        if (seguito) {
            OutlinedButton(
                onClick = onCambiaFollow,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Smetti di seguire")
            }
        } else {
            Button(
                onClick = onCambiaFollow,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Segui")
            }
        }
    }
}

@Composable
fun StatisticaDettaglioUtente(
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