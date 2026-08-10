# DPP SDK

## Standards alignment

- **EN 18222:2026:** Selected DPP repository and registry API shapes are implemented; this is not a formal compliance implementation.
- **Lifecycle management:** The SDK supports DPP lifecycle operations, historical access, and lifecycle-event recording. EN 18222 covers APIs for DPP lifecycle management and searchability. SDK-specific lifecycle-event representations or operations that are not directly prescribed by the standard are documented as extensions.
- DPP lifecycle operations and historical lookup are aligned with the lifecycle-management scope of EN 18222.

## Known standards misalignments

- **Backup operator:** The registry request does not implement a backup-operator field or operation.
- **Compression:** The compressed representation is project-defined and is not claimed as a formally validated EN representation.
- **Merge Patch and element paths:** Partial-update support is only partially implemented. For JSON objects, the API follows RFC 7396 JSON Merge Patch behavior, but it uses `application/json` instead of `application/merge-patch+json` and rejects valid non-object patch documents. Fine-grained element paths are also partially implemented: they support only the documented singular subset of RFC 9535 JSONPath, not the complete standard.
- The SDK's lifecycle-event model and event-history representation are project-specific extensions where EN 18222 does not prescribe the same payload or operation.
- **Operations:** Authentication, backup/recovery, and other production-operational requirements are outside the project scope.

![DPP SDK architecture](docs/dpp-sdk-architecture.drawio.png)

## Overview

This Java/Maven monorepo provides immutable Digital Product Passport (DPP) models and validation, optional PostgreSQL persistence, generic repository/registry HTTP clients, and runnable mock services.

Selected API contracts were designed with reference to confidential external
technical specifications that cannot be included in this public repository.
This documentation describes only the behavior implemented by the project. No
formal compliance, certification, legal conformity, or production-readiness
claim is made.

## Prerequisites

- Java 17 with `JAVA_HOME` set and `java` on `PATH`
- The included Maven wrapper (`mvnw` or `mvnw.cmd`); no separate Maven installation is required
- Docker Engine or Docker Desktop with Docker Compose, for the quick start

Run these from the repository root to verify the prerequisites:

PowerShell:

```powershell
$env:JAVA_HOME
java -version
Get-Command java
.\mvnw.cmd --version
docker --version
docker compose version
```

Linux/macOS Bash:

```bash
printf '%s\n' "$JAVA_HOME"
java -version
command -v java
./mvnw --version
docker --version
docker compose version
```

## Quick Start With Docker

Run from the repository root. The package command creates the demo JARs. Compose then builds and starts the repository API, registry API, and their separate PostgreSQL 16 databases.

PowerShell:

```powershell
.\mvnw.cmd clean package
docker compose -f .\dpp-sdk-demo\docker-compose.yml up -d --build
docker compose -f .\dpp-sdk-demo\docker-compose.yml ps
```

Linux/macOS Bash:

```bash
./mvnw clean package
docker compose -f ./dpp-sdk-demo/docker-compose.yml up -d --build
docker compose -f ./dpp-sdk-demo/docker-compose.yml ps
```

Compose publishes repository API port `8080`, registry API port `8081`, repository PostgreSQL port `5433`, and registry PostgreSQL port `5434`. Both APIs run with PostgreSQL backends in this Docker configuration.

## Health checks and integration-demo verification

From the repository root, wait until both services report `UP`, then run the HTTP walkthrough:

PowerShell:

```powershell
Invoke-WebRequest http://localhost:8080/health | Select-Object -ExpandProperty Content
Invoke-WebRequest http://localhost:8081/health | Select-Object -ExpandProperty Content
java -jar .\dpp-sdk-demo\dpp-integration-demo\target\dpp-integration-demo-0.5.1.jar http http://localhost:8081 http://localhost:8080 --debug=false
```

Linux/macOS Bash:

```bash
curl --fail http://localhost:8080/health
curl --fail http://localhost:8081/health
java -jar ./dpp-sdk-demo/dpp-integration-demo/target/dpp-integration-demo-0.5.1.jar http http://localhost:8081 http://localhost:8080 --debug=false
```

Success signals are `status` `UP` from both health endpoints and `HTTP services demo complete` from the runner. The runner creates, reads, updates, registers, and deletes a demo DPP; registry registration verifies the active repository DPP internally.

### Service browser entry points

Opening `http://localhost:8080/` or `http://localhost:8081/` redirects directly to the corresponding Swagger UI. Use `/health` for machine-readable status and `/v3/api-docs` for OpenAPI JSON.

## Stop and clean

Run from the repository root. Stop the environment while retaining the PostgreSQL volumes:

PowerShell:

```powershell
docker compose -f .\dpp-sdk-demo\docker-compose.yml down
```

Linux/macOS Bash:

```bash
docker compose -f ./dpp-sdk-demo/docker-compose.yml down
```

Stop the environment and delete persisted demo data:

PowerShell:

```powershell
docker compose -f .\dpp-sdk-demo\docker-compose.yml down -v
```

Linux/macOS Bash:

```bash
docker compose -f ./dpp-sdk-demo/docker-compose.yml down -v
```

## Local execution and advanced control

For local JAR execution, default memory mode, local or mixed PostgreSQL modes, focused service control, debugging, port changes, and advanced Docker options, see [dpp-sdk-demo/README.md](dpp-sdk-demo/README.md).

## Repository/module map

| Area | Responsibility | Documentation |
| --- | --- | --- |
| `dpp-datamodel` | Domain models, validation, payload mapping, and JSON transport | [README](dpp-datamodel/README.md) · [model guide](dpp-datamodel/MODEL_GUIDE.md) |
| `dpp-postgres` | Optional PostgreSQL relational persistence | [README](dpp-postgres/README.md) |
| `dpp-sdk-clients` | Generic repository/registry HTTP clients and payloads | [README](dpp-sdk-clients/README.md) |
| `dpp-sdk-demo` | Mock services, Docker runtime, integration demo, and Postman collections | [README](dpp-sdk-demo/README.md) · [demo guide](dpp-sdk-demo/DEMO_GUIDE.md) |

## Choose what you need

| Goal | Start here |
| --- | --- |
| Build, validate, map, serialize, or immutably edit DPPs | [Datamodel README](dpp-datamodel/README.md) |
| Look up every model field and validation rule | [Model guide](dpp-datamodel/MODEL_GUIDE.md) |
| Call repository or registry HTTP APIs | [Clients README](dpp-sdk-clients/README.md) |
| Use direct PostgreSQL persistence | [PostgreSQL README](dpp-postgres/README.md) |
| Run Docker, local JARs, memory mode, or PostgreSQL mode | [Demo README](dpp-sdk-demo/README.md) |
| Present the SDK step by step | [Demo guide](dpp-sdk-demo/DEMO_GUIDE.md) |
| View the repository architecture | [Architecture diagram](docs/dpp-sdk-architecture.drawio.png) |
| Review release changes | [CHANGELOG.md](CHANGELOG.md) |
| Review licensing terms | [LICENSE](LICENSE) |

## Consumer artifacts

All artifacts are version `0.5.1`. Build and install the relevant module locally before consuming it from another project.

| Need | Maven coordinate |
| --- | --- |
| Reusable DPP core | `dpp.datamodel:dpp-core:0.5.1` |
| Furniture-specific DPP SDK | `dpp.datamodel:dpp4fun:0.5.1` |
| Generic PostgreSQL support | `dpp.postgres:dpp-postgres-core:0.5.1` |
| Dpp4Fun PostgreSQL repository | `dpp.postgres:dpp4fun-postgres:0.5.1` |
| Repository payloads/client | `dpp.client:dpp-repo-payloads:0.5.1`, `dpp.client:dpp-repo-client:0.5.1` |
| Registry payloads/client | `dpp.client:dpp-registry-payloads:0.5.1`, `dpp.client:dpp-registry-client:0.5.1` |

`dpp-sdk-demo` is runnable reference code, not a consumer library dependency.

## Short SDK usage flow

Build or obtain a valid `Dpp4Fun`, then connect it to the generic repository client. The following method assumes `dpp` has already been constructed:

```java
import dpp.repo.client.DppRepoClient;
import dpp.repo.client.HttpDppRepoClient;
import dpp.repo.client.core.DppCodec;
import dpp.repo.client.core.DppValidator;
import dppsdk.dpp4fun.model.Dpp4Fun;
import dppsdk.dpp4fun.transport.Dpp4FunJsonCodec;
import dppsdk.dpp4fun.validation.Dpp4FunValidationService;

static Dpp4Fun createAndRead(Dpp4Fun dpp, String repositoryBaseUrl) {
    // The SDK codec serializes the concrete Dpp4Fun model used by this application.
    Dpp4FunJsonCodec jsonCodec = new Dpp4FunJsonCodec();

    // Semantic validation belongs to the datamodel layer and runs before createDpp sends HTTP.
    Dpp4FunValidationService validation = new Dpp4FunValidationService();
    validation.validate(dpp);

    // Adapt the concrete SDK codec to the model-independent client interface.
    DppCodec<Dpp4Fun> codec = new DppCodec<>() {
        @Override
        public String toJson(Dpp4Fun value) { return jsonCodec.toJson(value); }

        @Override
        public Dpp4Fun fromJson(String json) { return jsonCodec.fromJson(json); }
    };

    // The validator is supplied separately because the HTTP client is model-independent.
    DppRepoClient<Dpp4Fun> repository = new HttpDppRepoClient<>(
            repositoryBaseUrl,
            codec,
            validation::validate
    );

    // Create sends the full DPP; the typed read requests representation=full internally.
    repository.createDpp(dpp);
    return repository.readDppById(dpp.getDppId());
}
```

The method validates the object, supplies the concrete codec and validator to the generic client, creates the DPP, and reads it back as a typed full representation. For complete DPP construction, validation, mapping, serialization, and immutable editing, see the [datamodel README](dpp-datamodel/README.md). For the full generic-client setup, lifecycle operations, fine-granular behavior, and exception handling, see the [clients README](dpp-sdk-clients/README.md). Direct PostgreSQL repository usage is documented only in the [PostgreSQL README](dpp-postgres/README.md).

## Root build and verification commands

Run from the repository root:

PowerShell:

```powershell
.\mvnw.cmd clean verify
```

Linux/macOS Bash:

```bash
./mvnw clean verify
```

The root reactor builds `dpp-datamodel`, `dpp-postgres`, `dpp-sdk-clients`, and `dpp-sdk-demo` in that order.

## Swagger/OpenAPI and Postman shortcuts

With the Docker stack running:

- Repository API base: `http://localhost:8080`
- Registry API base: `http://localhost:8081`
- Repository Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Repository OpenAPI: `http://localhost:8080/v3/api-docs`
- Registry Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- Registry OpenAPI: `http://localhost:8081/v3/api-docs`
- Opening either API base URL in a browser redirects to its Swagger UI.
- Postman collections: [`dpp-sdk-demo/postman`](dpp-sdk-demo/postman/)

## Current limitations

- The mock repository and registry are demo services, not production services; the registry stores metadata only, while the repository stores complete DPPs.
- Fine-granular paths are only partially implemented: they support the documented singular subset of RFC 9535 JSONPath, not full JSONPath.
- Full-DPP GET routes default to a project-defined compressed summary; formal payload-specification conformity is not claimed.
- Public client routes are under `/v1/...`; demo-only support routes are under `/internal/...` and are not client methods.
- Some public API contracts were designed with reference to confidential external technical specifications that are not included in this repository.
- This repository documents only its implemented behavior and does not claim formal specification compliance.
- No real EU registry integration, production security hardening, or production-operational guarantee is provided.
