package com.ecommerce.common.test.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * ArchUnit rules for Clean Architecture enforcement.
 * Extend this class in each service's test package.
 */
public abstract class CleanArchitectureRules {

    @ArchTest
    public static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .consideringAllDependencies()
            .layer("API").definedBy("..api..")
            .layer("Application").definedBy("..application..")
            .layer("Domain").definedBy("..domain..")
            .layer("Infrastructure").definedBy("..infrastructure..")
            .layer("Config").definedBy("..config..")
            .whereLayer("API").mayOnlyBeAccessedByLayers("Config")
            .whereLayer("Application").mayOnlyBeAccessedByLayers("API", "Config", "Infrastructure")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure", "API", "Config")
            .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Config");

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_infrastructure = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..");

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_application = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..application..");

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_spring = noClasses()
            .that().resideInAPackage("..domain.model..")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..");

    @ArchTest
    public static final ArchRule controllers_should_be_in_api_package = classes()
            .that().haveSimpleNameEndingWith("Controller")
            .should().resideInAPackage("..api.controller..");

    @ArchTest
    public static final ArchRule usecases_should_be_in_application_package = classes()
            .that().haveSimpleNameEndingWith("UseCase")
            .or().haveSimpleNameEndingWith("UseCaseImpl")
            .should().resideInAPackage("..application.usecase..")
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule repositories_should_be_interfaces_in_domain = classes()
            .that().resideInAPackage("..domain.repository..")
            .should().beInterfaces();

    @ArchTest
    public static final ArchRule repository_adapters_should_be_in_infrastructure = classes()
            .that().haveSimpleNameEndingWith("RepositoryAdapter")
            .should().resideInAPackage("..infrastructure.persistence..");
}
