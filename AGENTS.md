# AnimalIdentification

Spring Boot 2.7.6 / Java 8 Maven app. Expert-system animal ID via forward/backward chaining. No DB, no frontend, no Maven wrapper, no CI.

## Commands

```
mvn test
mvn spring-boot:run
```

Stay on Java 8 (`pom.xml` source/target `1.8`). Default profile is `dev`; HTTP port is **9000**, not 8080. This machine may not have Maven on PATH.

## Layout

- Entry: `com.jinan.animalidentification.AnimalIdentificationApplication`
- HTTP: `controller/InferenceEngineController` → `/api/inference/**` (`@CrossOrigin("*")`)
- Logic: `engine/InferenceEngine` (`@Component`). Service validates and delegates.
- DTOs: `entity/Animal`, `entity/Rule`, `entity/InferenceResponse`, `dto/RuleUpdateRequest`. Not Spring beans.
- Rules live in memory on that singleton (`CopyOnWriteArrayList`). Seeded once in the engine constructor; `addInitialRules()` is a no-op if already populated. Attribute/conclusion strings are **Chinese** (`有毛发`, `哺乳动物`, `豹`, …); do not translate.

## API

| Method | Path | Notes |
|--------|------|-------|
| POST | `/api/inference/forward` | body `Animal` `{ "attributes": { "有毛发": true, ... } }` |
| POST | `/api/inference/backward` | body `["豹"]`; conclusions are **derived conditions only**, not the goal |
| GET | `/api/inference/rules` | alias: `/rules/all` |
| POST | `/api/inference/rules` | alias: `/rules/add`; missing conditions/conclusion → 400 |
| DELETE | `/api/inference/rules` | alias: `POST /rules/delete`; body `Rule`; missing → 404 |
| PUT | `/api/inference/rules` | alias: `POST /rules/update`; body `{ "oldRule", "newRule" }`; missing old → 404 |
| POST | `/api/inference/clear` | no-op leftover; inference path is per-request |

`equals`/`delete` match `conditions` + `conclusion` only. Seed `priority`: 1 入门 / 2 中间类 / 3 物种; list is sorted on add/update, not each inference tick. `confidence` was removed.

## Tests

- Engine unit tests (no Spring): `src/test/java/com/jinan/animalidentification/engine/InferenceEngineTest.java`
- Context smoke: `AnimalIdentificationApplicationTests.contextLoads`
