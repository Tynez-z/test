package com.android.testapp.core.network

import com.android.testapp.core.network.service.ApiService
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.net.HttpURLConnection

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * search gifs
     */
    @Test
    fun searchGifsRequestPathAndQueryAreCorrect() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(SEARCH_RESPONSE_JSON)
        )

        apiService.searchGifs(query = "cats", limit = 10, offset = 5)

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/gifs/search", request.requestUrl?.encodedPath)
        assertEquals("cats", request.requestUrl?.queryParameter("q"))
        assertEquals("10", request.requestUrl?.queryParameter("limit"))
        assertEquals("5", request.requestUrl?.queryParameter("offset"))
    }

    @Test(expected = Exception::class)
    fun searchGifsThrow404() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND))
        apiService.searchGifs("cats")
    }

    @Test(expected = Exception::class)
    fun searchGifsThrow500() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR))
        apiService.searchGifs("cats")
    }

    /**
     * get gif details
     */

    @Test
    fun getGifByIdRequestContainsGifId() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(GIF_DETAIL_RESPONSE_JSON)
        )

        apiService.getGifById("abc123")

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/gifs/abc123", request.requestUrl?.encodedPath)
    }

    @Test
    fun getGifById200Response() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(GIF_DETAIL_RESPONSE_JSON)
        )

        val response = apiService.getGifById("abc123")
        assertNotNull(response)
         assertEquals("abc123", response.data.id)
    }

    @Test(expected = Exception::class)
    fun getGifByIdThrow401Unauthorized() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
        )
        apiService.getGifById("abc123")
    }

    @Test
    fun searchGifsIgnoresUnknownKeys() = runTest {
        val jsonWithExtraFields = """
            {
              "data": [],
              "pagination": {"total_count":0,"count":0,"offset":0},
              "meta": {"status":200,"msg":"OK","response_id":"abc"},
              "unknown_future_field": "should_be_ignored"
            }
        """.trimIndent()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonWithExtraFields)
        )

        val response = apiService.searchGifs("cats")
        assertNotNull(response)
    }

    companion object {
        private const val SEARCH_RESPONSE_JSON =
            """
                {          
 "data": [
    {
      "type": "gif",
      "id": "abc123",
      "title": "Skating Hey Girl GIF",
      "url": "https://giphy.com/gifs/swerk-d7fvcrPydrDIVDRkim",
      "images": {
        "original": {
          "url": "https://media2.giphy.com/media/d7fvcrPydrDIVDRkim/giphy.gif"
        }
      }
    }
  ],
  "pagination": {
    "total_count": 1,
    "count": 1,
    "offset": 0
  },
  "meta": {
    "status": 200,
    "msg": "OK",
    "response_id": "asdasdasd"
  }
}
"""
        private const val GIF_DETAIL_RESPONSE_JSON = """
{
  "data": {
    "type": "gif",
    "id": "abc123",
    "title": "Skating Hey Girl GIF",
    "url": "https://giphy.com/gifs/swerk-d7fvcrPydrDIVDRkim",
    "images": {
      "original": {
        "url": "https://media2.giphy.com/media/d7fvcrPydrDIVDRkim/giphy.gif"
      }
    }
  },
  "meta": {
    "status": 200,
    "msg": "OK",
    "response_id": "asdasdasd"
  }
}
"""
    }
}