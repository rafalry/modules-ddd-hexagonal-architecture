/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "dddhexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
public class DddWithHexagonalArchitectureTest {

  @ArchTest
  static final ArchRule ddd_and_hexagonal_architecture_is_respected = layeredArchitecture()
      .consideringOnlyDependenciesInLayers()
      .layer("domain").definedBy("dddhexagonal.modules.*.domain..")
      .layer("integration").definedBy("dddhexagonal.modules.*.integration..")
      .layer("application").definedBy("dddhexagonal.modules.*.application..")
      .layer("adapters").definedBy("dddhexagonal.modules.*.adapter..")
      .layer("infrastructure").definedBy("dddhexagonal.modules.*.infrastructure..", "dddhexagonal.infrastructure..")
      .whereLayer("domain").mayNotAccessAnyLayer()
      .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapters")
      .whereLayer("application").mayOnlyBeAccessedByLayers("adapters")
      .whereLayer("integration").mayOnlyBeAccessedByLayers("application")
      .whereLayer("adapters").mayNotBeAccessedByAnyLayer()
      .whereLayer("infrastructure").mayOnlyBeAccessedByLayers("adapters");

  @ArchTest
  static final ArchRule domain_layer_building_blocks_relationships_are_respected = layeredArchitecture()
      .consideringOnlyDependenciesInLayers()
      .layer("domain-logic").definedBy("dddhexagonal.modules.*.domain.*")
      .layer("domain-rules").definedBy("dddhexagonal.modules.*.domain.*.rules..")
      .layer("domain-ports").definedBy("dddhexagonal.modules.*.domain.*.ports..")
      .layer("domain-events").definedBy("dddhexagonal.modules.*.domain.*.events..")
      .whereLayer("domain-rules").mayOnlyAccessLayers("domain-logic", "domain-ports")
      .whereLayer("domain-rules").mayOnlyBeAccessedByLayers("domain-logic")
      .whereLayer("domain-ports").mayOnlyAccessLayers("domain-logic", "domain-events")
      .whereLayer("domain-ports").mayOnlyBeAccessedByLayers("domain-logic", "domain-rules")
      .whereLayer("domain-events").mayNotAccessAnyLayer()
      .whereLayer("domain-events").mayOnlyBeAccessedByLayers( "domain-logic", "domain-ports");

  @ArchTest
  static final ArchRule application_layer_building_blocks_relationships_are_respected = layeredArchitecture()
      .consideringOnlyDependenciesInLayers()
      .layer("application-usecases").definedBy("dddhexagonal.modules.*.application.usecases..")
      .layer("application-integration").definedBy("dddhexagonal.modules.*.application.integration..")
      .layer("application-repository").definedBy("dddhexagonal.modules.*.application.repository..")
      .whereLayer("application-repository").mayNotAccessAnyLayer()
      .whereLayer("application-integration").mayNotBeAccessedByAnyLayer();

  @ArchTest
  static final ArchRule module_building_blocks_relationships_are_respected = layeredArchitecture()
      .consideringOnlyDependenciesInLayers()
      .layer("adapters").definedBy("dddhexagonal.modules.*.adapter..")

      .layer("domain-logic").definedBy("dddhexagonal.modules.*.domain.*")
      .layer("domain-rules").definedBy("dddhexagonal.modules.*.domain.*.rules..")
      .layer("domain-ports").definedBy("dddhexagonal.modules.*.domain.*.ports..")
      .layer("domain-events").definedBy("dddhexagonal.modules.*.domain.*.events..")

      .layer("application-usecases").definedBy("dddhexagonal.modules.*.application.usecases..")
      .layer("application-integration").definedBy("dddhexagonal.modules.*.application.integration..")
      .layer("application-repository").definedBy("dddhexagonal.modules.*.application.repository..")
      .layer("application-projections").definedBy("dddhexagonal.modules.*.application.projections..")

      .whereLayer("adapters").mayNotBeAccessedByAnyLayer()
      .whereLayer("application-usecases").mayOnlyAccessLayers("application-repository", "application-projections", "domain-logic")
      .whereLayer("application-repository").mayOnlyAccessLayers("domain-ports", "domain-logic")
      .whereLayer("application-integration").mayOnlyAccessLayers("domain-ports", "domain-events", "domain-logic");


  @ArchTest
  static final ArchRule no_cyclical_dependencies_between_packages = SlicesRuleDefinition.slices()
      .matching("dddhexagonal.modules.*.domain.(*)..")
      .should()
      .beFreeOfCycles();

}
