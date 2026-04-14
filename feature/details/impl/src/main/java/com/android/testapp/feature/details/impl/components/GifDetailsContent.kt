package com.android.testapp.feature.details.impl.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.android.testapp.core.model.Gif
import com.android.testapp.feature.details.impl.selectImageUrl
import com.android.testapp.feature.details.api.R as detailR

@Composable
internal fun GifDetailsContent(
    gif: Gif,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        val imageUrl = selectImageUrl(gif)

        imageUrl?.let { url ->
            AsyncImage(
                model = url,

                contentDescription = gif.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = gif.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        DetailRow(
            label = stringResource(detailR.string.feature_details_api_by),
            value = gif.username
        )

        gif.importDatetime?.let { date ->
            DetailRow(
                label = stringResource(detailR.string.feature_details_api_imported),
                value = date
            )
        }

        gif.sourcePostUrl?.let { url ->
            DetailRow(
                label = stringResource(detailR.string.feature_details_api_source),
                value = url
            )
        }
    }
}