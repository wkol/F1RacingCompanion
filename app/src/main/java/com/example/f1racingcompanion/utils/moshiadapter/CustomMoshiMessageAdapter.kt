/*
 * © 2018 Match Group, LLC.


Copyright (c) 2018, Match Group, LLC
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of Match Group, LLC nor the names of its contributors
      may be used to endorse or promote products derived from this software
      without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL MATCH GROUP, LLC BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.example.f1racingcompanion.utils.moshiadapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.Moshi
import com.tinder.scarlet.Message
import com.tinder.scarlet.MessageAdapter
import okio.ByteString.Companion.decodeHex
import okio.ByteString.Companion.toByteString
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * A [message adapter][MessageAdapter] that uses Moshi.
 */
class CustomMoshiMessageAdapter<T> private constructor(
    private val jsonAdapter: JsonAdapter<T>,
    private val annotations: Array<Annotation>
) : MessageAdapter<T> {

    override fun fromMessage(message: Message): T {
        val stringValue = when (message) {
            is Message.Text -> message.value
            is Message.Bytes -> {
                val byteString = message.value.toByteString(0, message.value.size)
                // Moshi has no document-level API so the responsibility of BOM skipping falls to whatever is delegating
                // to it. Since it's a UTF-8-only library as well we only honor the UTF-8 BOM.
                if (byteString.startsWith(UTF8_BOM)) {
                    byteString.substring(UTF8_BOM.size).utf8()
                } else {
                    byteString.utf8()
                }
            }
        }
        val annotation = annotations.firstOrNull { it is RequireType } as RequireType?
        return if (annotation == null) {
            deserializeJson(stringValue)
        } else {
            deserializeLiveTimingWithType(stringValue, annotation.type)
        }
    }

    private fun deserializeJson(json: String): T = jsonAdapter.fromJson(json)!!

    private fun deserializeLiveTimingWithType(json: String, type: KClass<*>): T {
        val jsonObject = jsonAdapter.fromJson(json)!!
        val typeOfJson = jsonObject::class
        if (typeOfJson != type) throw JsonDataException("Expected type $type but got $typeOfJson")
        return jsonObject
    }

    override fun toMessage(data: T): Message {
        val stringValue = jsonAdapter.toJson(data)
        return Message.Text(stringValue)
    }

    class Factory constructor(
        private val moshi: Moshi = DEFAULT_MOSHI,
        private val config: Config = Config()
    ) : MessageAdapter.Factory {

        override fun create(type: Type, annotations: Array<Annotation>): MessageAdapter<*> {
            val jsonAnnotations = filterJsonAnnotations(annotations)
            var adapter = moshi.adapter<Any>(type, jsonAnnotations)
            with(config) {
                if (lenient) {
                    adapter = adapter.lenient()
                }
                if (serializeNull) {
                    adapter = adapter.serializeNulls()
                }
                if (failOnUnknown) {
                    adapter = adapter.failOnUnknown()
                }
            }

            return CustomMoshiMessageAdapter(adapter, annotations)
        }

        private fun filterJsonAnnotations(annotations: Array<Annotation>): Set<Annotation> {
            return annotations
                .filter { it.annotationClass.java.isAnnotationPresent(JsonQualifier::class.java) }
                .toSet()
        }

        /**
         * Used to configure `moshi` adapters.
         *
         * @param lenient lenient when reading and writing.
         * @param serializeNull include null values into the serialized JSON.
         * @param failOnUnknown use [JsonAdapter.failOnUnknown] adapters.
         */
        data class Config(
            val lenient: Boolean = false,
            val serializeNull: Boolean = false,
            val failOnUnknown: Boolean = false
        )
    }

    private companion object {
        private val DEFAULT_MOSHI = Moshi.Builder().build()
        private val UTF8_BOM = "EFBBBF".decodeHex()
    }
}
