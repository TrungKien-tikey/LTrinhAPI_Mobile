package com.example.mobile_api.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson

class BooleanTitleAdapter {

    @FromJson
    @HandleBooleanAsTitle // Phải khớp với annotation đã tạo
    fun fromJson(reader: JsonReader): String? {
        return when (reader.peek()) {
            JsonReader.Token.STRING -> reader.nextString()
            JsonReader.Token.BOOLEAN -> reader.nextBoolean().toString() // Chuyển false -> "false"
            JsonReader.Token.NULL -> reader.nextNull()
            else -> {
                reader.skipValue() // Bỏ qua giá trị không mong muốn
                null
            }
        }
    }

    // Phần ToJson không quan trọng vì bạn chỉ đọc dữ liệu
    @ToJson
    fun toJson(@HandleBooleanAsTitle value: String?): String? {
        return value
    }
}