package com.theokanning.emojikeyboard

import com.theokanning.emojikeyboard.model.LabelBatchRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface VisionApi {

    @POST("v1/images:annotate")
    fun annotateImages(@Body batchRequest: LabelBatchRequest) : Call<Void>

}