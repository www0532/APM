/*
 * ========================LICENSE_START=================================
 * AEM Permission Management
 * %%
 * Copyright (C) 2013 Cognifide Limited
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

package com.cognifide.apm.core.endpoints.utils

import kotlin.reflect.KProperty

open class JsonObject {

    protected val properties: MutableMap<String, Any> = mutableMapOf()
    private val children: MutableMap<String, com.cognifide.apm.core.endpoints.utils.JsonObject> = mutableMapOf()

    fun properties(vararg pairs: Pair<String, String>) {
        properties.putAll(pairs)
    }

    infix fun String.set(value: Any) {
        properties[this] = value
    }

    fun addChild(name: String, child: com.cognifide.apm.core.endpoints.utils.JsonObject) {
        children[name] = child
    }

    operator fun String.invoke(child: com.cognifide.apm.core.endpoints.utils.JsonObject.() -> Unit) {
        children[this] = com.cognifide.apm.core.endpoints.utils.JsonObject().apply(child)
    }

    fun toMap(): Map<String, Any> {
        val map = properties.toMutableMap()
        children.forEach { (name, child) ->
            map[name] = child.toMap()
        }
        return map.toMap()
    }
}

class SuccessBody(initialMessage: String = "") : com.cognifide.apm.core.endpoints.utils.JsonObject() {
    var message: Any? by com.cognifide.apm.core.endpoints.utils.PropertyDelegate(properties, "message", initialMessage)

    init {
        message = initialMessage
    }
}

class ErrorBody(initMessage: String = "", initErrors: List<String> = listOf()) : com.cognifide.apm.core.endpoints.utils.JsonObject() {
    var message: String? by com.cognifide.apm.core.endpoints.utils.PropertyDelegate(properties, "message", initMessage)
    var errors: List<String>? by com.cognifide.apm.core.endpoints.utils.PropertyDelegate(properties, "errors", initErrors)

    init {
        message = initMessage
        errors = initErrors
    }
}

private class PropertyDelegate<T>(
        private val properties: MutableMap<String, Any>,
        private val name: String,
        private val default: T
) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return properties[name] as T ?: default
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        properties[name] = (value ?: default) as Any
    }
}