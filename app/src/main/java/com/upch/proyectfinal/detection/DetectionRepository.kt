package com.upch.proyectfinal.detection

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.TimeUnit

class DetectionRepository {

    suspend fun detectFoodImage(imageFile: File, onResult: (List<FoodItem>) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

                val mediaType = "image/*".toMediaType()
                val requestBody = imageFile.asRequestBody(mediaType)

                val imagePart = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    requestBody
                )

                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addPart(imagePart)
                    .addFormDataPart("prompts", "rice, fries potatoes, tomatoes, onion, meat")
                    .addFormDataPart("model", "agentic")
                    .build()

                val request = Request.Builder()
                    .url("https://api.va.landing.ai/v1/tools/agentic-object-detection")
                    .addHeader("Authorization", "Basic bmRtaGtnNzIweGY3cTVuenB0ZGxwOkxMMmszRjdUa255MFV6MDZERk0yNzNXeUxFYWNlVUZz") // ← reemplaza por tu clave real
                    .post(requestBodyBuilder)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null) {
                    Log.e("DetectionRepo", "Error: ${response.message}")
                    onResult(emptyList())
                    return@withContext
                }

                Log.d("API_RESPONSE", "🧠 JSON: $responseBody")

                val json = Json.parseToJsonElement(responseBody).jsonObject

                val detections = json["data"]?.jsonArray?.getOrNull(0)?.jsonArray

                val foodList = detections?.map { item ->
                    val label = item.jsonObject["label"]?.jsonPrimitive?.content ?: "unknown"
                    val box = item.jsonObject["bounding_box"]?.jsonArray
                    val score = item.jsonObject["score"]?.jsonPrimitive?.floatOrNull ?: 0f

                    val calories = CalorieMap.getCaloriesFor(label)

                    FoodItem(
                        name = label,
                        score = score,
                        boundingBox = box?.mapNotNull { it.jsonPrimitive.floatOrNull } ?: emptyList(),
                        calories = calories
                    )
                } ?: emptyList()

                onResult(foodList)

            } catch (e: Exception) {
                Log.e("DetectionRepo", "Error: ${e.message}", e)
                onResult(emptyList())
            }
        }
    }
}
