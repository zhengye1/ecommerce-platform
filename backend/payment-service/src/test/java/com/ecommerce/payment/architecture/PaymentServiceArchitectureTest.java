package com.ecommerce.payment.architecture;

import com.ecommerce.common.test.architecture.CleanArchitectureRules;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;

@AnalyzeClasses(packages = "com.ecommerce.payment", importOptions = ImportOption.DoNotIncludeTests.class)
public class PaymentServiceArchitectureTest extends CleanArchitectureRules {
    // Inherits all architecture rules from CleanArchitectureRules
}
