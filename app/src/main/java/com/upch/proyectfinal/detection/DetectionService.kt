package com.upch.proyectfinal.detection

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface DetectionService {
    @Multipart
    @POST("tools/agentic-object-detection")
    suspend fun detectFood(
        @Part image: MultipartBody.Part,
        @Part("prompts") prompts: RequestBody,
        @Part("model") model: RequestBody =
            "agentic".toRequestBody("text/plain".toMediaTypeOrNull())
    ): Response<ResponseBody>
}
