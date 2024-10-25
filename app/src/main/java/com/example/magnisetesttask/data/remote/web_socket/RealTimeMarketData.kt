package com.example.magnisetesttask.data.remote.web_socket

import com.example.magnisetesttask.core.util.Constants
import com.example.magnisetesttask.data.remote.web_socket.dto.RealTimeData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class RealTimeMarketData @Inject constructor(
    private val client: OkHttpClient
) {
    suspend fun getRealTimeMarketData(instrumentId: String): Flow<RealTimeData> = callbackFlow {
        val request = Request.Builder()
            .url(Constants.BASE_WEB_SOCKET_URL)
            .build()

        val webSocketListener = object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                val subscribeMessage = JSONObject().apply {
                    put("type", "l1-subscription")
                    put("id", "1")
                    put("instrumentId", instrumentId)
                    put("provider", "simulation")
                    put("subscribe", true)
                    put("kinds", JSONArray().put("last"))
                }.toString()

                webSocket.send(subscribeMessage)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val jsonResponse = JSONObject(text)

                jsonResponse.optJSONObject("last")?.let {
                    val realTimeData = RealTimeData(
                        instrumentId = jsonResponse.getString("instrumentId"),
                        timestamp = it.getString("timestamp"),
                        price = it.getDouble("price"),
                    )

                    trySend(realTimeData).isSuccess
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                close(t)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                close()
            }
        }

        val webSocket = client.newWebSocket(request, webSocketListener)

        awaitClose { webSocket.close(1000, "Flow closed") }
    }
}