package com.ecommerce.inventory.architecture;

import com.ecommerce.common.test.architecture.CleanArchitectureRules;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;

@AnalyzeClasses(packages = "com.ecommerce.inventory", importOptions = ImportOption.DoNotIncludeTests.class)
public class InventoryServiceArchitectureTest extends CleanArchitectureRules {
    // Inherits all architecture rules from CleanArchitectureRules
}
