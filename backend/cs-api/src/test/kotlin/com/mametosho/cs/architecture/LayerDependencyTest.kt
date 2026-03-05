package com.mametosho.cs.architecture

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import kotlin.test.Test

class LayerDependencyTest {

    private val classes = ClassFileImporter()
        .withImportOption(ImportOption.DoNotIncludeTests())
        .importPackages("com.mametosho.cs")

    @Test
    fun `infrastructure層はpresentation層に依存しない`() {
        noClasses()
            .that().resideInAPackage("..infrastructure..")
            .should().dependOnClassesThat().resideInAPackage("..presentation..")
            .because("infrastructure層はpresentation層に依存しない")
            .check(classes)
    }

    @Test
    fun `application層はinfrastructure層に依存しない`() {
        noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
            .because("application層はinfrastructure層に依存しない")
            .check(classes)
    }

    @Test
    fun `application層はpresentation層に依存しない`() {
        noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..presentation..")
            .because("application層はpresentation層に依存しない")
            .check(classes)
    }
}
