package com.ecommerce.common.test.architecture;

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

    // Layer name constants
    private static final String LAYER_API = "API";
    private static final String LAYER_APPLICATION = "Application";
    private static final String LAYER_DOMAIN = "Domain";
    private static final String LAYER_INFRASTRUCTURE = "Infrastructure";
    private static final String LAYER_CONFIG = "Config";

    // Package pattern constants
    private static final String PKG_DOMAIN = "..domain..";
    private static final String PKG_INFRASTRUCTURE = "..infrastructure..";
    private static final String PKG_APPLICATION = "..application..";

    /**
     * Protected constructor for abstract class.
     */
    protected CleanArchitectureRules() {
        // Protected constructor to prevent direct instantiation
    }

    @ArchTest
    public static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .consideringAllDependencies()
            .layer(LAYER_API).definedBy("..api..")
            .layer(LAYER_APPLICATION).definedBy(PKG_APPLICATION)
            .layer(LAYER_DOMAIN).definedBy(PKG_DOMAIN)
            .layer(LAYER_INFRASTRUCTURE).definedBy(PKG_INFRASTRUCTURE)
            .layer(LAYER_CONFIG).definedBy("..config..")
            .whereLayer(LAYER_API).mayOnlyBeAccessedByLayers(LAYER_CONFIG)
            .whereLayer(LAYER_APPLICATION).mayOnlyBeAccessedByLayers(LAYER_API, LAYER_CONFIG, LAYER_INFRASTRUCTURE)
            .whereLayer(LAYER_DOMAIN).mayOnlyBeAccessedByLayers(LAYER_APPLICATION, LAYER_INFRASTRUCTURE, LAYER_API, LAYER_CONFIG)
            .whereLayer(LAYER_INFRASTRUCTURE).mayOnlyBeAccessedByLayers(LAYER_CONFIG);

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_infrastructure = noClasses()
            .that().resideInAPackage(PKG_DOMAIN)
            .should().dependOnClassesThat().resideInAPackage(PKG_INFRASTRUCTURE);

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_application = noClasses()
            .that().resideInAPackage(PKG_DOMAIN)
            .should().dependOnClassesThat().resideInAPackage(PKG_APPLICATION);

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
