package dddhexagonal;

import org.springframework.web.bind.annotation.RestController;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import dddhexagonal.foundations.application.usecases.NonTransactionalUseCase;
import dddhexagonal.foundations.application.usecases.TransactionalUseCase;
import dddhexagonal.foundations.application.usecases.UseCase;
import dddhexagonal.foundations.domain.entity.BaseAggregateRoot;
import dddhexagonal.foundations.domain.events.BaseDomainEvent;
import dddhexagonal.foundations.domain.rule.BusinessRule;
import dddhexagonal.foundations.domain.service.BaseDomainService;
import dddhexagonal.foundations.integration.commands.IntegrationCommand;
import dddhexagonal.foundations.integration.commands.IntegrationCommandHandler;
import dddhexagonal.foundations.integration.events.IntegrationEvent;
import dddhexagonal.foundations.integration.events.IntegrationEventHandler;
import dddhexagonal.foundations.integration.query.IntegrationQuery;
import dddhexagonal.foundations.integration.query.IntegrationQueryHandler;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "dddhexagonal.modules")
public class NamingConventionsArchitectureTest {

  @ArchTest
  public static final ArchRule domain_services_naming = classes()
      .that().areAssignableTo(BaseDomainService.class)
      .or().haveNameMatching(".*(Service|DomainEventHandlers?)$")
      .should().haveNameMatching(".*(Service|DomainEventHandlers?)$")
      .andShould().resideInAPackage("dddhexagonal.modules.*.domain..")
      .andShould().resideOutsideOfPackages("dddhexagonal.modules.*.domain.*.events",
          "dddhexagonal.modules.*.domain.*.ports",
          "dddhexagonal.modules.*.domain.*.rules",
          "dddhexagonal.modules.*.domain.*..service",
          "dddhexagonal.modules.*.domain.*..services",
          "dddhexagonal.modules.*.domain.*..entity",
          "dddhexagonal.modules.*.domain.*..entities");

  @ArchTest
  public static final ArchRule rules_naming = classes()
      .that().areAssignableTo(BusinessRule.class)
      .should().haveNameMatching(".*Rule$")
      .andShould().resideInAPackage("dddhexagonal.modules.*.domain..rules..");

  @ArchTest
  public static final ArchRule aggregates_naming = classes()
      .that().areAssignableTo(BaseAggregateRoot.class)
      .should().haveNameMatching(".*AggregateRoot$")
      .andShould().resideInAPackage("dddhexagonal.modules.*.domain..")
      .andShould().resideOutsideOfPackages("dddhexagonal.modules.*.domain.*.events",
          "dddhexagonal.modules.*.domain.*.ports",
          "dddhexagonal.modules.*.domain.*.rules",
          "dddhexagonal.modules.*.domain.*..entity",
          "dddhexagonal.modules.*.domain.*..entities");

  @ArchTest
  public static final ArchRule use_cases_naming = classes()
      .that().areAnnotatedWith(TransactionalUseCase.class)
      .or().areAnnotatedWith(NonTransactionalUseCase.class)
      .or().haveNameMatching(".*UseCases?$")
      .should().haveNameMatching(".*UseCases?$")
      .andShould().resideInAPackage("dddhexagonal.modules.*.application..usecases..")
      .andShould().beMetaAnnotatedWith(UseCase.class);

  @ArchTest
  public static final ArchRule ports_naming = classes()
      .that().resideInAPackage("dddhexagonal.modules.*.domain..ports..")
      .should().haveNameMatching(".*Port$")
      .orShould().haveNameMatching(".*Dto$");

  @ArchTest
  public static final ArchRule domain_events_naming = classes()
      .that().resideInAPackage("dddhexagonal.modules.*.domain..events..")
      .should().haveNameMatching(".*DomainEvent")
      .andShould().beAssignableTo(BaseDomainEvent.class);

  @ArchTest
  public static final ArchRule commands_naming = classes()
      .that().areAssignableTo(IntegrationCommand.class)
      .should().haveNameMatching(".*IntegrationCommand?$")
      .andShould().resideInAPackage("dddhexagonal.modules.*.integration.commands..");

  @ArchTest
  public static final ArchRule command_handlers_naming = classes()
      .that().containAnyMethodsThat(new DescribedPredicate<>("command handlers") {
        @Override
        public boolean test(JavaMethod javaMethod) {
          return javaMethod.isAnnotatedWith(IntegrationCommandHandler.class);
        }
      })
      .should().haveNameMatching(".*UseCases?$")
      .andShould().resideInAnyPackage("dddhexagonal.modules.*.application.usecases..");

  @ArchTest
  public static final ArchRule integration_events_naming = classes()
      .that().areAssignableTo(IntegrationEvent.class)
      .should().haveNameMatching(".*IntegrationEvent?$")
      .andShould().resideInAPackage("dddhexagonal.modules.*.integration.events..");

  @ArchTest
  public static final ArchRule integration_event_handlers_naming = classes()
      .that().containAnyMethodsThat(new DescribedPredicate<>("event handlers") {
        @Override
        public boolean test(JavaMethod javaMethod) {
          return javaMethod.isAnnotatedWith(IntegrationEventHandler.class);
        }
      })
      .should().haveNameMatching(".*(UseCases?|Projector)$")
      .andShould().resideInAnyPackage("dddhexagonal.modules.*.application.projections..",
          "dddhexagonal.modules.*.application.usecases..");

  @ArchTest
  public static final ArchRule integration_query_naming = classes()
      .that().areAssignableTo(IntegrationQuery.class)
      .should().haveNameMatching(".*IntegrationQuery?$")
      .andShould().resideInAPackage("dddhexagonal.modules.*.integration.query..");

  @ArchTest
  public static final ArchRule integration_query_handlers_naming = classes()
      .that().containAnyMethodsThat(new DescribedPredicate<>("query handlers") {
        @Override
        public boolean test(JavaMethod javaMethod) {
          return javaMethod.isAnnotatedWith(IntegrationQueryHandler.class);
        }
      })
      .should().haveNameMatching(".*(IntegrationQueryHandlers?|UseCases?)$")
      .andShould().resideInAPackage("dddhexagonal.modules.*.application.usecases..");

  @ArchTest
  public static final ArchRule allow_only_plural_package_name_for_events = classes()
      .that().resideInAPackage("dddhexagonal..event..")
      .should().notBeTopLevelClasses()
      .allowEmptyShould(true);

  @ArchTest
  public static final ArchRule allow_only_plural_package_name_for_commands = classes()
      .that().resideInAPackage("dddhexagonal..command..")
      .should().notBeTopLevelClasses()
      .allowEmptyShould(true);

  @ArchTest
  public static final ArchRule allow_only_singular_package_name_for_queries = classes()
      .that().resideInAPackage("dddhexagonal..queries..")
      .should().notBeTopLevelClasses()
      .allowEmptyShould(true);

  @ArchTest
  public static final ArchRule api_controllers_naming = classes()
      .that().areAnnotatedWith(RestController.class)
      .should().haveNameMatching(".*ApiController$");
}
