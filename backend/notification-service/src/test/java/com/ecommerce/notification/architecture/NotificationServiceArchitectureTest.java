package com.ecommerce.notification.architecture;

import com.ecommerce.common.test.architecture.CleanArchitectureRules;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;

@AnalyzeClasses(packages = "com.ecommerce.notification", importOptions = ImportOption.DoNotIncludeTests.class)
public class NotificationServiceArchitectureTest extends CleanArchitectureRules {
    // Inherits all architecture rules from CleanArchitectureRules
}
