# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

No unreleased changes.

## [0.5.1] - 2026-08-06

### Fixed

- Aligned the approved cross-language data contracts, including Unicode
  whitespace handling, finite numeric validation, JSON mapping boundaries, and
  defensive null-member validation evidence.
- Added direct regression evidence for null registry requests and null
  bill-of-material members.

### Changed

- Bumped the monorepo, published module artifacts, runnable demo JARs, and
  maintained demo-image references from `0.5.0` to `0.5.1`.

## [0.5.0] - 2026-07-14

### Changed
- Bumped the monorepo and all published module artifacts from `0.4.0` to `0.5.0` for the breaking API and standards-alignment changes in this release.
- Moved all demo-only repository and registry business endpoints to `/internal/...` in a hard cutover; `/v1` and conventional health and OpenAPI routes are unchanged.
- Changed the mock repository and registry root URLs to redirect directly to `/swagger-ui/index.html`.

### Documentation
- Audited root and module documentation against the current Maven reactor, source contracts, mock-service routes, runtime configuration, and implementation scope.
- Corrected standards overclaims, registry payload wording, internal Docker build commands, stale version references, and guidance for the non-buildable `dpp-persistence` path.

### Validation
- Revalidated the root and module Maven coordinates, generated JAR naming, and documented commands for the `0.5.0` release.

## [0.4.0] - 2026-07-02

### Added
- Root-level `dpp-postgres` module for optional PostgreSQL persistence.
- `dpp-postgres-core` reusable PostgreSQL support for DPP identity, versioning, `DppCore`, paging, and lifecycle events.
- `dpp4fun-postgres` relational `Dpp4Fun` persistence with version/history, soft delete, lifecycle events, batch lookup, and search support.
- Optional PostgreSQL backend wiring for `dpp-sdk-demo/mock-dpp-repo`.
- Optional PostgreSQL backend wiring for `dpp-sdk-demo/mock-eu-registry`.
- `dpp.repo.backend=memory|postgres` and `dpp.registry.backend=memory|postgres` mock backend settings, with memory remaining the default for direct local runs.
- PostgreSQL-backed mock endpoint tests and PostgreSQL usage documentation.

### Changed
- Bumped the monorepo and main module artifact version from `0.3.0` to `0.4.0`.
- Preserved mock endpoint paths and response shapes while allowing `mock-dpp-repo` to run on either in-memory or PostgreSQL storage.
- Preserved mock endpoint paths and response shapes while allowing `mock-eu-registry` to run on either in-memory or PostgreSQL storage.
- Kept validation, JSON handling, merge patching, fine-granular element-path logic, and registry metadata verification outside the PostgreSQL repository layers.
- Added lightweight comments and Javadocs to the PostgreSQL persistence and mock backend integration classes.

### Documentation
- Reworked the root README into a current standalone monorepo entry point with aligned build, demo, Docker, Swagger UI, Postman, and module-routing guidance.
- Consolidated the `dpp-datamodel` docs, removed redundant datamodel docs, and kept the model/validation guidance in the current maintained files.
- Consolidated the `dpp-sdk-clients` docs into the main module README and removed redundant per-submodule README duplication.
- Updated `dpp-sdk-demo` docs to distinguish optional `.env.example`-based overrides from built-in defaults.
- Updated the root architecture diagram to include `dpp-postgres`, optional PostgreSQL-backed mock storage paths, and the current legend/arrow behavior.

### Validation
- Root-level Maven test and package validation was rerun after the `0.4.0` version bump so the updated coordinates and jar names remain consistent with the documented commands.

## [0.3.0] - 2026-06-12

### Added
- Initial public release of the DPP SDK, a Java toolkit for building and validating Digital Product Passports.
- `dpp-datamodel`, reusable DPP domain model and payload DTOs.
- `dpp-sdk-clients`, client library for DPP services.
- `dpp-sdk-demo`, runnable Spring Boot example showing end-to-end usage including mocked services for dpp-registry and dpp-repository based on selected API shapes from the draft standardisation documents published by the JTC24 of CEN/CENELEC.

### Documentation
- Root README describing the monorepo split across the three modules.
- Architecture diagram under `docs/`.

[0.4.0]: https://github.com/CIR4FUN-EU/dpp-sdk/releases/tag/v0.4.0
[0.5.0]: https://github.com/CIR4FUN-EU/dpp-sdk/releases/tag/v0.5.0
[0.5.1]: https://github.com/CIR4FUN-EU/dpp-sdk/releases/tag/v0.5.1
[0.3.0]: https://github.com/CIR4FUN-EU/dpp-sdk/releases/tag/v0.3.0
