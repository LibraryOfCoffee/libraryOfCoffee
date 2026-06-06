package com.mametosho.admin.architecture

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import java.io.File
import kotlin.test.Test

/**
 * com.mametosho.admin パッケージ内で FQCN（完全修飾クラス名）を使わないことを保証するテスト。
 *
 * 対象: コードボディ内の FQCN 参照。
 * 除外: package 宣言、import 文、コメント行（//、/＊、＊）。
 */
class FqcnUsageTest {

    private val importedClasses = ClassFileImporter()
        .withImportOption(ImportOption.DoNotIncludeTests())
        .importPackages("com.mametosho.admin")

    @Test
    fun `コードボディにFQCNを使用しない`() {
        val fqcnPattern = Regex("""(?<![.\w])com\.mametosho(?:\.[a-z][a-zA-Z0-9_]*)+\.[A-Z]\w*""")

        val noFqcnCondition = object : ArchCondition<JavaClass>("コードボディにFQCNを使用しない") {
            override fun check(item: JavaClass, events: ConditionEvents) {
                val sourceFile = resolveSourceFile(item) ?: return

                sourceFile.readLines().forEachIndexed { lineIndex, line ->
                    val trimmed = line.trim()
                    val isSkippable = listOf("package ", "import ", "//", "/*", "*")
                        .any { trimmed.startsWith(it) }
                    if (isSkippable) return@forEachIndexed

                    fqcnPattern.findAll(trimmed).forEach { match ->
                        events.add(
                            SimpleConditionEvent.violated(
                                item,
                                "${sourceFile.name}:${lineIndex + 1}: FQCNが使用されています → '${match.value}'" +
                                    "（import文と短縮名を使用すること）",
                            ),
                        )
                    }
                }
            }

            private fun resolveSourceFile(item: JavaClass): File? {
                val simpleName = item.simpleName.removeSuffix("Kt")
                val packagePath = item.packageName.replace('.', File.separatorChar)
                return File("src/main/kotlin/$packagePath/$simpleName.kt").takeIf { it.exists() }
            }
        }

        classes()
            .that().resideInAPackage("com.mametosho.admin..")
            .should(noFqcnCondition)
            .because("コードボディでは import 文と短縮名を使用し、FQCNは使用しないこと")
            .check(importedClasses)
    }
}
