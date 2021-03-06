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

import javax.servlet.http.HttpServletResponse

class ResponseEntity<T>(val statusCode: Int, val body: T)

fun badRequest(body: com.cognifide.apm.core.endpoints.utils.ErrorBody.() -> Unit): ResponseEntity<Any> {
    return error(HttpServletResponse.SC_BAD_REQUEST, body)
}

fun internalServerError(body: com.cognifide.apm.core.endpoints.utils.ErrorBody.() -> Unit): ResponseEntity<Any> {
    return error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, body)
}

fun ok(body: com.cognifide.apm.core.endpoints.utils.SuccessBody.() -> Unit): ResponseEntity<Any> {
    return success(200, body)
}

fun error(statusCode: Int, body: com.cognifide.apm.core.endpoints.utils.ErrorBody.() -> Unit): ResponseEntity<Any> {
    return ResponseEntity(statusCode, com.cognifide.apm.core.endpoints.utils.ErrorBody().apply(body))
}

fun success(statusCode: Int, body: com.cognifide.apm.core.endpoints.utils.SuccessBody.() -> Unit): ResponseEntity<Any> {
    return ResponseEntity(statusCode, com.cognifide.apm.core.endpoints.utils.SuccessBody().apply(body))
}