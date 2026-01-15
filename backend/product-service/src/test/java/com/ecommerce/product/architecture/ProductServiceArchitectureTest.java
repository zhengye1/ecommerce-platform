package com.ecommerce.product.architecture;

import com.ecommerce.common.test.architecture.CleanArchitectureRules;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;

@AnalyzeClasses(packages = "com.ecommerce.product", importOptions = ImportOption.DoNotIncludeTests.class)
public class ProductServiceArchitectureTest extends CleanArchitectureRules {
    // Inherits all architecture rules from CleanArchitectureRules
}
