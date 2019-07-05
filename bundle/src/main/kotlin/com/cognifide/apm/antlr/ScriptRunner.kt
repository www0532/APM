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

package com.cognifide.apm.antlr

import com.cognifide.apm.antlr.ApmLangParser.DefineVariableContext
import com.cognifide.apm.antlr.ApmLangParser.ForEachContext
import com.cognifide.apm.antlr.executioncontext.ExecutionContext
import com.cognifide.cq.cqsm.api.logger.Message
import com.cognifide.cq.cqsm.api.logger.Progress
import com.cognifide.cq.cqsm.api.logger.Status

class ScriptRunner(private val actionInvoker: ActionInvoker) {

    fun execute(executionContext: ExecutionContext): Progress {
        val executor = Executor(executionContext)
        executor.visit(executionContext.root.apm)
        return executionContext.progress
    }

    private inner class Executor(private val executionContext: ExecutionContext) : ApmLangBaseVisitor<Unit>() {

        override fun visitDefineVariable(ctx: DefineVariableContext) {
            val variableName = ctx.IDENTIFIER().toString()
            val variableValue = executionContext.resolveArgument(ctx.argument())
            executionContext.setVariable(variableName, variableValue)
            info("define", "Defined variable: $variableName= $variableValue")
        }

        override fun visitForEach(ctx: ForEachContext) {
            val index = ctx.IDENTIFIER().toString()
            val values: List<ApmValue> = readValues(ctx)
            var i = 1
            info("foreach", "Begin")
            for (value in values) {
                try {
                    executionContext.createLocalContext()
                    info("foreach", "Iteration: $i")
                    executionContext.setVariable(index, value)
                    visit(ctx.body())
                    i++
                } finally {
                    executionContext.removeLocalContext()
                }
            }
            info("foreach", "End")
        }

        private fun readValues(ctx: ForEachContext): List<ApmValue> {
            val variableValue = executionContext.resolveArgument(ctx.argument())
            return when (variableValue) {
                is ApmList -> variableValue.list.map { ApmString(it) }
                is ApmEmpty -> listOf()
                else -> listOf(variableValue as ApmValue)
            }
        }

        override fun visitGenericCommand(ctx: ApmLangParser.GenericCommandContext) {
            val commandName = ctx.IDENTIFIER().toString().toUpperCase()
            val arguments = executionContext.resolveArguments(ctx.arguments())
            actionInvoker.runAction(executionContext.progress, commandName, arguments)
        }

        private fun info(shortInfo: String, details: String = "") {
            executionContext.progress.addEntry(shortInfo, Message.getInfoMessage(details), Status.SUCCESS)
        }
    }
}