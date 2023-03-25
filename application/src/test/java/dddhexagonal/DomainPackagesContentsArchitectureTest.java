package dddhexagonal;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import dddhexagonal.foundations.domain.entity.BaseDomainEntity;
import dddhexagonal.foundations.domain.service.BaseDomainService;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "dddhexagonal.modules", importOptions = ImportOption.DoNotIncludeTests.class)
public class DomainPackagesContentsArchitectureTest {

  @ArchTest
  static final ArchRule domain_layer_should_be_divided_into_subpackage_per_aggregate = noClasses()
      .that().resideInAPackage("dddhexagonal.modules.*.domain")
      .should().beTopLevelClasses()
      .allowEmptyShould(true);

  @ArchTest
  static final ArchRule domain_business_logic_should_reside_in_the_main_package = classes()
      .that().areAssignableTo(BaseDomainEntity.class)
      .or().areAssignableTo(BaseDomainService.class)
      .should().resideInAPackage("dddhexagonal.modules.*.domain.*")
      .andShould().resideOutsideOfPackage("dddhexagonal.modules.*.domain.ports");

}
