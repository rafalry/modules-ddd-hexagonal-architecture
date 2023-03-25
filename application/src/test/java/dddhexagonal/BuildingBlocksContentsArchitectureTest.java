package dddhexagonal;

import jakarta.persistence.Entity;

import java.util.List;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

@AnalyzeClasses(packages = "dddhexagonal.modules", importOptions = ImportOption.DoNotIncludeTests.class)
public class BuildingBlocksContentsArchitectureTest {

  @ArchTest
  static final ArchRule ports_can_only_be_interfaces_or_pojos = classes()
      .that().resideInAPackage("dddhexagonal.modules.*.domain..ports..")
      .should().beInterfaces()
      .orShould().haveNameMatching(".*Dto$");

  @ArchTest
  static final ArchRule ports_cannot_depend_on_entities = noMethods()
      .that().areDeclaredInClassesThat().resideInAPackage("dddhexagonal.modules.*.domain..ports..")
      .should().haveRawParameterTypes(new DescribedPredicate<>("is a ORM entity") {
        @Override
        public boolean test(List<JavaClass> javaClasses) {
          return javaClasses.stream().anyMatch(paramClass -> paramClass.isAnnotatedWith(Entity.class));
        }
      });

  @ArchTest
  static final ArchRule adapters_isolation_is_respected = SlicesRuleDefinition.slices()
      .matching("dddhexagonal.modules.*.adapter.(*)..")
      .should()
      .notDependOnEachOther();

  @ArchTest
  static final ArchRule adapters_can_call_only_usecases = noClasses()
      .that().resideInAPackage("dddhexagonal.modules.*.adapter..")
      .should().accessClassesThat()
        .resideInAnyPackage("dddhexagonal.modules.*.application.repository..",
            "dddhexagonal.modules.*.application.integration..");
}
