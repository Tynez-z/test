package com.android.testapp.core.data

import com.android.testapp.core.data.mapper.toDomain
import com.android.testapp.core.network.model.searchGif.GifDto
import com.android.testapp.core.network.model.searchGif.GifImagesDto
import com.android.testapp.core.network.model.searchGif.ImageDataDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class GifMapper {
    @Test
    fun gifDtoToDomain() {
        val gifDto = GifDto(
            id = "123ewdsa",
            title = "Cat",
            username = "userCat",
            importDatetime = "2026-13-01 12:00:00",
            sourcePostUrl = "https://example.com",
            images = GifImagesDto(
                original = ImageDataDto(
                    url = "https://example.com/cat.gif",
                    width = "300",
                    height = "200",
                    size = "1222222",
                    webp = "https://example.com/cat.webp",
                    webpSize = "512000"
                ),
                fixedWidth = ImageDataDto(
                    url = "https://example.com/cat-small.gif",
                    width = "150",
                    height = "100",
                    size = "256000",
                    webp = null,
                    webpSize = null
                ),
                fixedHeight = ImageDataDto(
                    url = "https://example.com/cat-small.gif",
                    width = "150",
                    height = "100",
                    size = "256000",
                    webp = null,
                    webpSize = "412000"
                )

            )
        )

        val domainGif = gifDto.toDomain()

        assertNotNull(domainGif)
        assertEquals("123ewdsa", domainGif.id)
        assertEquals("Cat", domainGif.title)
        assertEquals("userCat", domainGif.username)
        assertEquals("2026-13-01 12:00:00", domainGif.importDatetime)
        assertEquals("https://example.com", domainGif.sourcePostUrl)
        assertEquals("https://example.com/cat.gif", domainGif.images.original?.url)
        assertEquals("300", domainGif.images.original?.width)
        assertEquals("200", domainGif.images.original?.height)
        assertEquals("1222222", domainGif.images.original?.size)
    }
}