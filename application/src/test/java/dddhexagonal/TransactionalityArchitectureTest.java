package dddhexagonal;

import org.springframework.transaction.event.TransactionalEventListener;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

@AnalyzeClasses(packages = "dddhexagonal.modules", importOptions = {ImportOption.DoNotIncludeTests.class})
public class TransactionalityArchitectureTest {

  @ArchTest
  static final ArchRule general_transactionality_is_used = ArchRuleDefinition.noClasses()
      .that().resideInAPackage("dddhexagonal.modules..")
      .should().beAnnotatedWith(org.springframework.transaction.annotation.Transactional.class)
      .orShould().beAnnotatedWith(jakarta.transaction.Transactional.class);

  @ArchTest
  static final ArchRule transactional_event_listeners_used_only_in_domain_service_or_integration = methods()
      .that().areAnnotatedWith(TransactionalEventListener.class)
      .should().beDeclaredInClassesThat().resideInAnyPackage(
          "dddhexagonal.modules.*.domain.*",
          "dddhexagonal.modules.*.domain.*.ports",
          "dddhexagonal.modules.*.application.integration");

}
