/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal;

import java.util.regex.Pattern;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import static java.util.regex.Pattern.compile;

public class PackageNamesInModulesArchitectureTest {

  @AnalyzeClasses(packages = "",
      importOptions = ModulesPackageNamingArchitectureTest.IncludeAdapters.class)
  public static class ModulesPackageNamingArchitectureTest {
    @ArchTest
    public static final ArchRule modules_package_naming = getPackageNamingRule("module", "modules");

    public static class IncludeAdapters extends IncludeModuleLayer {

      @Override
      public Pattern getUriPattern() {
        return compile(".*modules-ddd-hexagonal-architecture/module-[a-zA-Z0-9-_]+/.*");
      }
    }
  }

  @AnalyzeClasses(packages = "dddhexagonal.modules",
      importOptions = AdaptersLayerPackageNamingArchitectureTest.IncludeAdapters.class)
  public static class AdaptersLayerPackageNamingArchitectureTest {
    @ArchTest
    public static final ArchRule adapters_package_naming = getModulePackageNamingRule("adapter");

    public static class IncludeAdapters extends IncludeModuleLayer {

      @Override
      public Pattern getUriPattern() {
        return compile(".*eis-services/module-[a-zA-Z0-9-_]+/([a-zA-Z0-9-_]+/)?[a-zA-Z0-9-_]+-adapter-[a-zA-Z0-9-_]+.*");
      }
    }
  }

  @AnalyzeClasses(packages = "dddhexagonal.modules",
      importOptions = DomainLayerPackageNamingArchitectureTest.IncludeDomainLayer.class)
  public static class DomainLayerPackageNamingArchitectureTest {
    @ArchTest
    public static final ArchRule domain_package_naming = getModulePackageNamingRule("domain");

    public static class IncludeDomainLayer extends IncludeModuleLayer {

      @Override
      public Pattern getUriPattern() {
        return compile(".*eis-services/(module-[a-zA-Z0-9-_]+/)?module-[a-zA-Z0-9-_]+/[a-zA-Z0-9-_]+-domain/.*");
      }
    }
  }

  @AnalyzeClasses(packages = "dddhexagonal.modules",
      importOptions = ApplicationLayerPackageNamingArchitectureTest.IncludeApplicationLayer.class)
  public static class ApplicationLayerPackageNamingArchitectureTest {
    @ArchTest
    public static final ArchRule application_package_naming = getModulePackageNamingRule("application");

    public static class IncludeApplicationLayer extends IncludeModuleLayer {

      @Override
      public Pattern getUriPattern() {
        return compile(".*eis-services/(module-[a-zA-Z0-9-_]+/)?module-[a-zA-Z0-9-_]+/[a-zA-Z0-9-_]+-application/.*");
      }
    }
  }

  @AnalyzeClasses(packages = "dddhexagonal.modules",
      importOptions = InfrastructureLayerPackageNamingArchitectureTest.IncludeInfrastructureLayer.class)
  public static class InfrastructureLayerPackageNamingArchitectureTest {
    @ArchTest
    public static final ArchRule infrastructure_package_naming = getModulePackageNamingRule("infrastructure");

    public static class IncludeInfrastructureLayer extends IncludeModuleLayer {

      @Override
      public Pattern getUriPattern() {
        return compile(".*eis-services/(module-[a-zA-Z0-9-_]+/)?module-[a-zA-Z0-9-_]+/[a-zA-Z0-9-_]+-infrastructure[a-zA-Z0-9-_]+/.*");
      }
    }
  }

  @AnalyzeClasses(packages = "dddhexagonal.modules",
      importOptions = IntegrationLayerPackageNamingArchitectureTest.IncludeIntegrationLayer.class)
  public static class IntegrationLayerPackageNamingArchitectureTest {
    @ArchTest
    public static final ArchRule integration_package_naming = getModulePackageNamingRule("integration");

    public static class IncludeIntegrationLayer extends IncludeModuleLayer {

      @Override
      public Pattern getUriPattern() {
        return compile(".*eis-services/(module-[a-zA-Z0-9-_]+/)?module-[a-zA-Z0-9-_]+/[a-zA-Z0-9-_]+-integration/.*");
      }
    }
  }

  @AnalyzeClasses(packages = "dddhexagonal.modules",
      importOptions = FoundationsPackageNamingArchitectureTest.IncludeFoundations.class)
  public static class FoundationsPackageNamingArchitectureTest {
    @ArchTest
    public static final ArchRule foundations_package_naming = getPackageNamingRule("", "foundations");

    public static class IncludeFoundations extends IncludeModuleLayer {

      @Override
      public Pattern getUriPattern() {
        return compile(".*eis-services/(module-[a-zA-Z0-9-_]+/)?foundations/foundations-[a-zA-Z0-9-_]+/.*");
      }
    }
  }


  public static ArchRule getModulePackageNamingRule(String layer) {
    return getPackageNamingRule(layer, "modules.(*)." + layer + ".(*)");
  }
  
  public static ArchRule getPackageNamingRule(String packageLabel, String packagePattern) {
    return ArchRuleDefinition
        .noClasses().that().resideOutsideOfPackage("dddhexagonal." + packagePattern + "..")
        .should().beTopLevelClasses()
        .allowEmptyShould(true)
        .because(
            "all " + packageLabel + " classes should be placed in package and subpackages of dddhexagonal." + packagePattern);
  }


  public static abstract class IncludeModuleLayer implements ImportOption {

    public abstract Pattern getUriPattern();


    @Override
    public boolean includes(Location location) {
      boolean include = location.matches(getUriPattern());
      if (include) {
        System.out.println(getClass().getSimpleName() + " location includes URL " + location.asURI());
      }
      return include;
    }
  }
}
