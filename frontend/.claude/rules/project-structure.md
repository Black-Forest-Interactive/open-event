# Project Structure

## Monorepo Layout

```
apps/
├── admin-app/        Angular app — administrative tasks (requires login, admin role)
├── external-app/     Angular app — public-facing actions (no login required)
└── portal-app/       Angular app — authenticated user portal

libs/
├── admin/            API services + feature services scoped to admin-app only
├── core/             Shared API access layer — no feature services, used by all apps
├── external/         API services + feature services scoped to external-app only
├── portal/           API services + feature services scoped to portal-app only
├── shared/           Shared logic, models, utils, guards, interceptors across all apps
└── ui/               Shared UI — components, pipes, directives, used by all apps
```

---

## Library Rules

### `libs/core`
- Contains only API services (`*.api.service.ts`) — no feature services
- No app-specific logic
- Interfaces for backend responses live here when the endpoint is shared across apps
- All three apps may import from `core`

```
libs/core/
└── src/
    ├── lib/
    │   ├── user/
    │   │   ├── user.api.service.ts
    │   │   └── user.model.ts          ← UserResponse interface, CreateUserRequest class
    │   └── auth/
    │       ├── auth.api.service.ts
    │       └── auth.model.ts
    └── index.ts                       ← barrel export
```

### `libs/admin` / `libs/portal` / `libs/external`
- App-scoped libraries — only their matching app may import from them
- Contains API services for app-specific endpoints AND feature services with business logic
- Models (interfaces + request classes) live alongside the service they belong to

```
libs/portal/
└── src/
    ├── lib/
    │   ├── orders/
    │   │   ├── order.api.service.ts   ← HTTP access only
    │   │   ├── order.service.ts       ← business logic, uses order.api.service
    │   │   └── order.model.ts         ← OrderResponse, CreateOrderRequest
    │   └── profile/
    │       ├── profile.api.service.ts
    │       ├── profile.service.ts
    │       └── profile.model.ts
    └── index.ts
```

### `libs/shared`
- Framework-agnostic utilities, guards, interceptors, and base models shared across all apps
- No UI, no API services, no app-specific logic
- Examples: auth guard, HTTP error interceptor, pagination model, date utils

```
libs/shared/
└── src/
    ├── lib/
    │   ├── guards/
    │   ├── interceptors/
    │   ├── models/                    ← shared base interfaces (e.g. PagedResponse<T>)
    │   └── utils/
    └── index.ts
```

### `libs/ui`
- Presentational only — components, pipes, directives
- No API services, no business logic, no app-specific state
- Components receive data via signal inputs, emit events via outputs
- All apps may import from `ui`

```
libs/ui/
└── src/
    ├── lib/
    │   ├── button/
    │   ├── table/
    │   ├── modal/
    │   └── pipes/
    └── index.ts
```

---

## Import Rules

| Importer           | May import from                                 |
|--------------------|-------------------------------------------------|
| `apps/admin-app`   | `libs/admin`, `libs/core`, `libs/shared`, `libs/ui` |
| `apps/portal-app`  | `libs/portal`, `libs/core`, `libs/shared`, `libs/ui` |
| `apps/external-app`| `libs/external`, `libs/core`, `libs/shared`, `libs/ui` |
| `libs/admin`       | `libs/core`, `libs/shared`                      |
| `libs/portal`      | `libs/core`, `libs/shared`                      |
| `libs/external`    | `libs/core`, `libs/shared`                      |
| `libs/ui`          | `libs/shared`                                   |
| `libs/shared`      | nothing                                         |
| `libs/core`        | nothing                                         |

Cross-app lib imports are forbidden — `libs/admin` must never import from `libs/portal` or `libs/external`, and vice versa.

---

## Where to Put New Files

| What you're creating                        | Where it goes                        |
|---------------------------------------------|--------------------------------------|
| HTTP call used by all apps                  | `libs/core/src/lib/<feature>/`       |
| HTTP call used by one app only              | `libs/<app>/src/lib/<feature>/`      |
| Business logic for a feature                | `libs/<app>/src/lib/<feature>/`      |
| Response interface (shared endpoint)        | `libs/core/src/lib/<feature>/<feature>.model.ts` |
| Response interface (app-specific endpoint)  | `libs/<app>/src/lib/<feature>/<feature>.model.ts` |
| Reusable component / pipe / directive       | `libs/ui/src/lib/<element>/`         |
| Guard or interceptor                        | `libs/shared/src/lib/guards/` or `interceptors/` |
| Shared base model or utility                | `libs/shared/src/lib/`               |