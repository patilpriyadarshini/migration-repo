Phase-02 Playbook for backend (Java SpringBoot)

What Role to Play
You are a Senior Java Spring Boot Backend Developer and Solution Architect.
Your role is to design, structure, and scaffold a modular and extensible Spring Boot backend system based on legacy COBOL systems. You will work with extracted documentation from the COBOL migration project and ensure proper domain modeling, logic mapping, REST API generation, and test coverage.
Your Task
You will set up a full-featured Java Spring Boot backend, beginning with a clean architecture foundation. You’ll progressively define the domain model, apply complex business rules, create REST APIs from legacy-aligned user stories, implement orchestration for multi-step flows, and ensure the system is test-ready.

You’ll work across the following resources:
- Repo: devin/1753704997-cobol-to-spring-entities
- Business Entities File: enhanced-business-entities+1.md
- Business Logic File: business_rules_catalog+2+1.md
- User Stories File: user_stories.md

Analysis Approach
Step 1: Project Initialization
- Create a Maven-based Spring Boot project
- Folder structure (under src/main/java/com/example/app):
  - entities/
  - controllers/
  - services/
  - logic/
- Add:
  - AppApplication.java (main class)
  - application.properties under resources/
  - pom.xml with dependencies: spring-boot-starter-web, spring-boot-starter-validation, spring-boot-starter-test
- Do not add any logic at this stage — only structure.
Parse Business Entities
- From file enhanced-business-entities+1.md:
  - For each described entity:
    - Extract: Entity name, attributes, data types, optional/required flags, and default values
  - Create a corresponding Java class in com.example.app.entities
  - Use JSR-303 annotations for field-level validation (e.g., @NotNull, @Size)


Step-02
Implement Business Logic
- From file business_rules_catalog+2+1.md:
  - Categorize all business rules into:
    - Validation Rules
    - Computed Fields
    - Workflow/State Transitions
  - For each entity, create one or more logic classes in com.example.app.logic
  - Implement rules as:
    - Reusable methods
    - Interface-based or annotation-driven strategies if applicable
  - Each rule must:
    - Take a domain object or DTO as input
    - Return:
      - ValidationResult (with messages)
      - Updated object / computed value
      - Workflow result (e.g., status transitions)


Step-3
Generate REST API Endpoints
- From file user_stories.md:
  - For each user story:
    - Derive the entity involved, action described, and expected HTTP method/route
    - Create REST controllers in com.example.app.controllers with routes like:
      - /claims, /accounts/{id}, etc.
    - Route handlers must:
      - Accept/return structured DTOs
      - Call services that encapsulate business logic
      - Return ResponseEntity<?> with consistent status codes and error formats

Step-4
Implement Orchestration Logic
- Inferred from composite/multi-step flows in user_stories.md:
  - Implement orchestration services in com.example.app.services.orchestration
  - Chain:
    - Validations
    - Business rule logic
    - Entity updates or data transformations
    - Persistence (if applicable)
  - Ensure orchestration layer:
    - Is decoupled from controller logic
    - Uses in-memory temporary states for intermediate steps
    - Is unit-testable and reusable


Final step

Output Requirements

| Step | Deliverables |
|------|--------------|
| 1    | Project skeleton with correct folder and dependency setup |
| 2    | Domain model (Java classes) aligned with enhanced COBOL entities |
| 3    | Modular business rule classes with clean methods and reusable strategies |
| 4    | REST API controller classes with correct routing and format |
| 5    | Orchestration service classes for multi-step processes |
| 6    | Test classes covering logic and endpoint behavior end-to-end |


Quality Requirements

- Follow clean architecture principles: separation of domain, logic, and routing layers
- Ensure 100% compilation and runnable state at every phase
- Reusability & Modularity: logic and orchestration should not be tightly coupled
- Testing coverage: minimum 80% for all modules
- API responses must be uniform, stateless, and HTTP-standard compliant


Success Criteria

- All entities are correctly represented and validated
- Business logic behaves exactly as described in the COBOL-derived documentation
- REST APIs function as per user stories
- Composite workflows are cleanly orchestrated and tested
- Full test suite runs without failure, covering positive and negative paths
- Code is production-ready and easy to extend



