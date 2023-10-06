/*
 * Copyright (C) 2022 Vaticle
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.vaticle.typedb.client.tool.doc.python

import com.vaticle.typedb.client.tool.doc.common.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    val inputDirectoryName = args[0]
    val outputDirectoryName = args[1]

    val docsDir = System.getenv("BUILD_WORKSPACE_DIRECTORY")?.let { Paths.get(it).resolve(outputDirectoryName) }
        ?: Paths.get(outputDirectoryName)
    if (!docsDir.toFile().exists()) {
        Files.createDirectory(docsDir)
    }

    File(inputDirectoryName).walkTopDown().filter {
        it.toString().endsWith(".html") &&
                (it.toString().contains(".api.") || it.toString().contains(".common."))
    }.forEach {
        val html = it.readText(Charsets.UTF_8)
        val parsed = Jsoup.parse(html)

        parsed.select("dl.class").forEach {
            val parsedClass = if (it.selectFirst("dt.sig-object + dd > p")!!.text().contains("Enum")) {
                parseEnum(it)
            } else {
                parseClass(it)
            }
            if (parsedClass.isNotEmpty()) {
                val parsedClassAsciiDoc = parsedClass.toAsciiDoc("python")
                val outputFile = docsDir.resolve("${parsedClass.name}.adoc").toFile()
                outputFile.createNewFile()
                outputFile.writeText(parsedClassAsciiDoc)
            } else {
                println("Empty class: ${parsedClass.name}")
            }
        }
    }
}

fun parseClass(element: Element): Class {
    val classSignElement = element.selectFirst("dt.sig-object")
    val className = classSignElement!!.selectFirst("dt.sig-object span.sig-name")!!.text()

    val classDetails = classSignElement.nextElementSibling()
    val classDetailsParagraphs = classDetails!!.children().map { it }.filter { it.tagName() == "p" }
    val (descr, bases) = classDetailsParagraphs.partition { it.select("code.py-class").isNullOrEmpty() }
    val superClasses = bases[0]!!.text().substringAfter("Bases: ").split(", ")
        .filter { it != "ABC" && it != "Enum" && it != "object" && it != "Generic[T]" && !it.startsWith("NativeWrapper") }
    val classDescr = descr.map { reformatTextWithCode(it.html()) }

    val classExamples = element.select("dl.class > dt.sig-object + dd > section:contains(Example) > div > .highlight")
        .map { it.text() }

    val methods = classDetails.select("dl.method")
        .map { parseMethod(it) }

    val properties = classDetails.select("dl.property")
        .map { parseProperty(it) }.filter { it.name != "native_object" }

    return Class(
        name = className,
        description = classDescr,
        examples = classExamples,
        fields = properties,
        methods = methods,
        superClasses = superClasses,
    )
}

fun parseEnum(element: Element): Class {
    val classSignElement = element.selectFirst("dt.sig-object")
    val className = classSignElement!!.selectFirst("dt.sig-object span.sig-name")!!.text()

    val classDetails = classSignElement.nextElementSibling()
    val classDetailsParagraphs = classDetails!!.children().map { it }.filter { it.tagName() == "p" }
    val (descr, bases) = classDetailsParagraphs.partition { it.select("code.py-class").isNullOrEmpty() }
    val classBases = bases[0]!!.select("span").map { it.html() }
    val classDescr = descr.map { reformatTextWithCode(it.html()) }

    val classExamples = element.select("section:contains(Examples) .highlight").map { it.text() }

    val methods = classDetails.select("dl.method").map { parseMethod(it) }
    val members = classDetails.select("dl.attribute").map { parseEnumConstant(it) }

    return Class(
        name = className,
        description = classDescr,
        enumConstants = members,
        examples = classExamples,
        methods = methods,
        superClasses = classBases,
    )
}

fun parseMethod(element: Element): Method {
    val methodSignature = enhanceSignature(element.selectFirst("dt.sig-object")!!.text())
    val methodName = element.selectFirst("dt.sig-object span.sig-name")!!.text()
    val allArgs = getArgsFromSignature(element.selectFirst("dt.sig-object")!!)
    val methodReturnType = element.select(".sig-return-typehint").text()
    val methodDescr = element.select("dl.method > dd > p").map { reformatTextWithCode(it.html()) }
    val methodArgs = element.select(".field-list > dt:contains(Parameters) + dd p").map {
        val argName = it.selectFirst("strong")!!.text()
        assert(allArgs.contains(argName))
        val argDescr = reformatTextWithCode(removeArgName(it.html())).removePrefix(" – ")
        Variable(
            name = argName,
            defaultValue = allArgs[argName]?.second,
            description = argDescr,
            type = allArgs[argName]?.first,
        )
    }
    val methodReturnDescr = element.select(".field-list > dt:contains(Returns) + dd p").text()
    val methodExamples = element.select("section:contains(Examples) .highlight").map { it.text() }

    return Method(
        name = methodName,
        signature = methodSignature,
        args = methodArgs,
        description = methodDescr,
        examples = methodExamples,
        returnDescription = methodReturnDescr,
        returnType = methodReturnType,
    )

}

fun parseProperty(element: Element): Variable {
    val name = element.selectFirst("dt.sig-object span.sig-name")!!.text()
    val type = element.selectFirst("dt.sig-object span.sig-name + .property ")?.text()?.dropWhile { !it.isLetter() }
    val descr = element.select("dd > p").map { reformatTextWithCode(it.html()) }.joinToString("\n\n")
    return Variable(
        name = name,
        description = descr,
        type = type,
    )
}

fun parseEnumConstant(element: Element): EnumConstant {
    val name = element.selectFirst("dt.sig-object span.sig-name")!!.text()
    val value = element.selectFirst("dt.sig-object span.sig-name + .property")!!.text().removePrefix("= ")
    return EnumConstant(
        name = name,
        value = value,
    )
}

fun getArgsFromSignature(methodSignature: Element): Map<String, Pair<String?, String?>> {
    return methodSignature.select(".sig-param").map {
        it.selectFirst(".n")!!.text() to
                Pair(it.select(".p + .w + .n").text(), it.selectFirst("span.default_value")?.text())
    }.toMap()
}

fun reformatTextWithCode(html: String): String {
    return removeAllTags(replaceEmTags(replaceCodeTags(html)))
}

fun removeArgName(html: String): String {
    return Regex("<strong>[^<]*</strong>").replace(html, "")
}

fun enhanceSignature(signature: String): String {
    return signature.replace("→", "->").replace("¶", "")
}
