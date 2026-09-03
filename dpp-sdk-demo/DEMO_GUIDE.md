# DPP SDK Demo Guide

This is a presenter guide, not a setup guide. Complete execution modes, commands, ports, cleanup, and troubleshooting are in the [demo README](README.md).

When using any command referenced by this guide, work from the repository root;
the demo README uses root-relative Compose, JAR, and Maven-wrapper paths.

## Preflight checklist

- Build the demo JARs and start the chosen service mode by following the README.
- Confirm `http://localhost:8080/health` and `http://localhost:8081/health` return `UP` for an HTTP demo.
- Open `http://localhost:8080/` and `http://localhost:8081/`; each base URL redirects directly to its Swagger UI.
- Open repository Swagger UI at `http://localhost:8080/swagger-ui/index.html` and registry Swagger UI at `http://localhost:8081/swagger-ui/index.html`.
- Import the three collections in [`postman/`](postman/) and set their base URL variables.
- Decide whether to demonstrate `sdk`, `http`, or `all`; use `all` for a complete walkthrough.

The runner modes are passed as the first argument to
`dpp-integration-demo-0.5.1.jar`; use the exact command from the [demo README](README.md).

## What to open

Keep three views available: the integration-runner terminal, repository Swagger UI, and registry Swagger UI. Open either service base URL to reach its Swagger UI directly. Have Postman ready for the optional request-by-request sequence. The runner is the most reliable narrative spine because it prints each operation and expected error handling.

## Suggested flow

1. **Set the boundary.** Explain that this is a mock repository plus a metadata-only mock registry. It demonstrates SDK and client behavior; it is not a production registry or certification claim.
2. **Show health and API discovery.** Refresh both `/health` endpoints, then show the two Swagger UIs. Point out that `/v1/...` is the public mock API and `/internal/...` is demo support.
3. **Show the SDK capability flow.** Run `sdk` or start with the SDK section of `all`. Explain immutable builders, semantic validation, mapping to/from payloads, JSON codec round-trip, and invalid data rejection.
4. **Create and read a DPP.** In the HTTP flow, show `CreateDPP`, the compressed representation, typed full-DPP read, and read by product ID. The client asks for the full representation when decoding a typed DPP.
5. **Update and inspect history.** Show the partial-DPP update, then the DPP-ID historical read and the current read by product ID. Explain that these are mock lifecycle/version behaviors.
6. **Show fine-granular operations.** Use `$.characteristics.productName`. If the demo DPP includes bill-of-material entries, also show `$.billOfMaterials.materials[0].name`. Only singular root/member/index selectors are supported.
7. **Register with the registry.** Highlight that the registry request records identifiers and a repository endpoint, then independently sends `HEAD /internal/dpps/{dppId}` to verify that the active DPP exists. It does not copy full DPP JSON.
8. **Finish with deletion and limits.** The runner soft-deletes its DPP and prints completion. Reiterate persistence choice and mock boundaries.

## Positive cases to call out

- Builders reject missing required values and the validator rejects semantic errors.
- A valid full DPP can be created, read, partially updated, addressed at a data element, read historically, and soft-deleted.
- The registry accepts metadata only after repository verification succeeds.
- `sdk` ends with `SDK capability demo complete`; HTTP flows end with `HTTP services demo complete`.

## Negative cases to show

- A DPP with an invalid supplier role fails client-side validation before HTTP.
- Registering a missing or deleted repository DPP is rejected because the registry's internal HEAD verification does not find an active record.
- Reading a nonexistent DPP produces a handled 404 client error.
- An unreachable registry is reported as a handled network client error.
- Fine-granular malformed paths produce 400, valid-but-missing paths 404, and unsupported selector forms 501. Do not describe this bounded subset as full RFC 9535 support.

## Postman sequence

Use the collections in this order:

1. `dpp-lifecycle-api.verified-export-shape.postman_collection.json` — create/read/update/version/delete repository flow.
2. `dpp-fine-granular-api.import-safe.postman_collection.json` — singular reads and direct JSON PATCH values.
3. `dpp-registry-api.verified-export-shape.postman_collection.json` — register an active repository DPP, then inspect mock lookup responses.

Run the repository collection before registry registration so the referenced DPP exists. If a collection mutates seeded data, restart memory mode or reset the PostgreSQL demo volumes using the README's cleanup instructions.

## Presenter notes

The registry lookup routes are demo-only `/internal/...` endpoints and are not part of the public registry client.

Avoid spending time on Docker implementation details during the demo. If attendees need setup, port changes, local-memory versus PostgreSQL selection, mixed operation, or failure diagnosis, link them to the [README](README.md). Keep the presentation centered on the DPP flow and its observable API behavior.

The intended takeaway is that the SDK separates domain modeling, validation,
transport, persistence, repository access, and registry metadata while keeping
the mock runtime replaceable.
