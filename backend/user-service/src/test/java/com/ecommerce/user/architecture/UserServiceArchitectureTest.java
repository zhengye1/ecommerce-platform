package com.ecommerce.user.architecture;

import com.ecommerce.common.test.architecture.CleanArchitectureRules;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;

@AnalyzeClasses(packages = "com.ecommerce.user", importOptions = ImportOption.DoNotIncludeTests.class)
public class UserServiceArchitectureTest extends CleanArchitectureRules {
    // Inherits all architecture rules from CleanArchitectureRules
}
