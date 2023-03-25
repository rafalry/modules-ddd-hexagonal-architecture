* https://dev.to/peholmst/building-aggregates-with-spring-data-2iig
* https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/#application-core-organisation
* https://www.baeldung.com/spring-data-ddd

# DDD + hexagonal references & examples
* https://github.com/citerus/dddsample-core
* https://github.com/ddd-by-examples/library
* https://github.com/kgrzybek/modular-monolith-with-ddd
  - comparison (relaxed same-module side effects transactionality, simplified integration messaging, relaxed integration command usage, addition of integration queries, simplified domain to integration events propagation (no notifications) )

DDD vs JPA mismatch:
* https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.entity-persistence.saving-entites.strategies
* https://www.youtube.com/watch?v=GOSW911Ox6s
* https://www.youtube.com/watch?v=98avpvXcO5w

DDD how to implement aggregates:
- https://enterprisecraftsmanship.com/posts/domain-model-purity-completeness/
  - prefer domain purity over completeness but make an informed decision
- https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/domain-events-design-implementation
  - domain & integration events usage
- https://udidahan.com/2009/09/01/dont-delete-just-dont/
  - model more specific use case than delete (also https://stackoverflow.com/questions/8578943/how-is-a-delete-of-an-aggregate-root-handled-in-ddd)
- https://github.com/dotnet/docs/blob/main/docs/architecture/microservices/microservice-ddd-cqrs-patterns/domain-events-design-implementation.md
- https://www.youtube.com/watch?v=f6G46rqkePc&list=PLzYkqgWkHPKDpXETRRsFv2F9ht6XdAF3v&index=5
  - modelling domain into aggregates
  - 
## Testing
https://rieckpil.de/reuse-containers-with-testcontainers-for-fast-integration-tests/

## Questions
- are domain events domain interface or domain details?
- how to unit test domain side effects triggered by domain events? - check NotificationHandlers
- services project vs CQRS, Event Sourcing, command handling internally, read model

### Layers
- Domain: domain logic, aggregates, domain services, business rules, domain events
- Application: use case handling, persistence awareness, integration commands/events/queries handling

### Domain completeness vs purity

#### Completeness 
Domain interface consists of: aggregate, domain service & ports.
Pros:
 - fast unit test of the complete domain logic 
Cons:
 - port mocking is necessary in unit tests
 - 
#### Purity
Domain interface consists of: aggregate, domain service & domain events. 
Pros:
 - minimised mocking in tests on domain & application layer
Cons:
 - domain logic side effects are tested on application layer using slower integration tests with persistence awareness
 - transaction boundary spills over from domain to application layer

## Priorities

- domain logic completely testable using fast unit tests (without persistence)
- minimised mocking in tests

## TODO:
* foundations
  * aggregates
  * persistence
  * event handling
  * command handling
  * query handling
  * arch unit
  * testability
  * audit
  * validation on all levels: use cases, queries, events, commands, web 
  * integration outbox/inbox
  * read model
* unit test for concurrent aggregate modification
* frontend
  * e2e test
  * sample react frontend
* event-driven architecture
  * internal data projection
  * external event communication
* db migrations per schema
* security
  * authentcation
  * authorization
  * permissions
  

| Feature                                   | Event | Command | Query |
|-------------------------------------------|-------|---------|-------|
| Side effects                              | Yes   | Yes     | No    |
| Returns result                            | No    | Yes     | Yes   |
| Communication uses source domain language | Yes   | No      | No    |
| Communication uses target domain language | No    | Yes     | Yes   |


## Design criteria

1. Module isolation - as if they were microservices (async communication, potential module extraction to separate microservice)
2. Testability - fast, contract-based unit tests that verify complete logic of a unit


# Naming consistency
- package and class names should be standardised across all modules
- class names should be preferably unique across the whole project
- package should be preferably functional names and isolate functional units (as opposed to technical package names isolating technical aspects)
  - ex. prefer `user.management.services` than `user.services.management`

## Building blocks

### Domain

Domain layer building blocks:
- entities
- services - domain logic that involves reading multiple aggregates (multiple instances of a type or multiple instances of multiple types of aggregates) and modifying instances of one aggregate type

In the domain completeness variant the domain layer is the only place where the business logic resides.

### Application
  
Application layer building blocks:
- use cases - isolated unit of business logic, typical responsibilities:
  - fetch required aggregates from repositories
  - tell aggregates or domain services to execute business logic
  - persist aggregates
  - may create or delete aggregates in case it does not involve business logic and it has no side effects (otherwise creation/deletion should be responsibility of the domain layer)
- persistence - framework specific storage and queries
- integration - handle integration level communication:
  - inbound to the module via integration command handlers that trigger use cases
  - outbound from the module via adapters for domain event handlers that trigger integration commands, queries and events

In the domain completeness variant the application layer does not contain any business logic.
In the integration tests completeness variant responsibilities of the building blocks of the domain and application layers are preserved, however both layers are not separated to independent Maven modules.

#### Testability

Application layer should be testable with JUnit and spring context. Application layer tests should be limited and focus 
on testing whether persistence and integration functions work well for all the use cases. 

### Adapters 

Contain implementation for external dependencies that cannot be easily instantiated in the integration tests.

#### Testability

Adapters code alone is typically difficult to completely test, however parts that contain logic should be unit tested.

### Infrastructure

Contains low-level wiring of the whole application.

#### Testability

Infrastructure code alone is typically difficult to test in isolation, although some units related to ex. authentication can be testable.

Whether infrastructure code performs well should be verified in e2e tests. 

## Communication patterns

1. Same domain
    * use case modifying an aggregate
    * use case modifying an aggregate with side effect to another aggregate
2. Cross-domain
    * command/query/event
3. External system
    * outgoing command/query/event
    * incoming command/query/event

## Architecture Decision Record

### Application and domain layers are knowledgeable of persistence but do not contain persistence implementation

Aggregates and entities are hibernate entities because it solves the problem of optimistic locking and enables working with domain events.

SQL migrations and repositories interfaces are in the application layer:
- it does not result in application layer being tied to specific persistence implementation
- it enables integration tests with mocked or embedded persistence
- persistence is not an adapter because it does not contain specific persistence implementation

### Failed input validation against the invariants in the domain layer should result in throwing `BusinessRuleException`

Such exception naturally maps into 4xx API HTTP response. 

http://www.kamilgrzybek.com/design/domain-model-validation/
https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/domain-model-layer-validations

### Work with integration events, commands and queries in application layer

### Integration commands return results

In puristic scenario integration commands do not return results. However, this can be impractical (i.e. over-engineering overhead) in case the logic depends on success failure of the command or an ID of the entity created by the command.

### Dead-letter-queue instead of outbox and inbox for integration messages 

### Topics
- atomic vs eventual invariants
- transactionality

### Puting upfront queries for aggregates to repositories in the `UseCase` before the control is passed to the domain layer

The main goal is full testability of the domain logic. It can be a good move in case extra upfront queries to repositories do not add any domain logic to application layer. Basically, such setup improves both domain completeness and purity. 

### Explicit save when working on repositories

Explicit save is required in order for Spring to pick up domain events and trigger `@TransactionalEventListener`s.

### DDD + hexagonal architecture - domain boundary variants
- complete integration tests preference
- domain completeness preference
- domain purity preference
