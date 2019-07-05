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

package com.cognifide.apm.antlr.parsedscript

import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.IntStream
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.Token

object InvalidSyntaxMessageFactory {

    fun generalSyntaxError(e: InvalidSyntaxException): String {
        return String.format("Error at %d:%d", e.line, e.charPositionInLine)
    }

    fun detailedSyntaxError(e: InvalidSyntaxException): String {
        return underlineError(e.recognizer, e.offendingToken, e.line, e.charPositionInLine)
    }

    private fun underlineError(recognizer: Recognizer<*, *>, offendingToken: Token?, line: Int, charPositionInLine: Int): String {
        val errorLine = getErrorLine(recognizer, line)
        val builder = StringBuilder()
        builder.append("Invalid line: ")
        builder.append(errorLine)
        builder.append("\n")
        val length = invalidSequenceLength(offendingToken)
        builder.append("Invalid sequence: ")
        builder.append(errorLine, charPositionInLine, charPositionInLine + length)
        return builder.toString()
    }

    private fun getErrorLine(recognizer: Recognizer<*, *>, line: Int): String {
        val input = toString(recognizer.inputStream)
        val lines = input.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return lines[line - 1]
    }

    private fun toString(inputStream: IntStream): String {
        return (inputStream as? CommonTokenStream)?.tokenSource?.inputStream?.toString() ?: inputStream.toString()
    }

    private fun invalidSequenceLength(offendingToken: Token?): Int {
        if (offendingToken != null) {
            val start = offendingToken.startIndex
            val stop = offendingToken.stopIndex
            if (start in 0..stop) {
                return Math.max(stop - start, 1)
            }
        }
        return 1
    }
}
