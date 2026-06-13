package com.example.fotogram.schermate.bacheca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.fotogram.model.Post
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.Image
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import com.example.fotogram.util.base64ToImageBitmap

@Composable
fun SchedaPost(
    post: Post,
    onApriDettaglioUtente: (Int) -> Unit,
    onApriImmaginePost: (String) -> Unit,
    onApriMappaPost: (String) -> Unit,
    mostraAutoreCliccabile: Boolean = true,
    mostraBadge: Boolean = true
) {
    val immaginePost = remember(post.immagineBase64) {
        base64ToImageBitmap(post.immagineBase64)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = if (mostraAutoreCliccabile) {
                        Modifier.clickable {
                            onApriDettaglioUtente(post.idAutore)
                        }
                    } else {
                        Modifier
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "IMG",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = post.nomeAutore,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                if (mostraBadge) {
                    AssistChip(
                        onClick = {},
                        label = {
                            if (post.seguito) {
                                Text("Seguito")
                            } else {
                                Text("Scoperta")
                            }
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = post.testo,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable {
                        onApriImmaginePost(post.idPost)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (immaginePost != null) {
                    Image(
                        bitmap = immaginePost,
                        contentDescription = "Immagine del post",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Immagine non disponibile")
                }
            }

            if (post.haPosizione) {
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    onClick = {
                        onApriMappaPost(post.idPost)
                    }
                ) {
                    Text("Apri mappa")
                }
            }
        }
    }
}