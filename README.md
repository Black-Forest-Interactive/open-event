# Open Event

[![CI](https://github.com/Black-Forest-Interactive/open-event/actions/workflows/ci.yml/badge.svg)](https://github.com/Black-Forest-Interactive/open-event/actions/workflows/ci.yml)
[![Coverage](badges/coverage.svg)](badges/coverage.svg)
[![Branches](badges/branches.svg)](badges/branches.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Black-Forest-Interactive_open-event&metric=alert_status)](https://sonarcloud.io/dashboard?id=Black-Forest-Interactive_open-event)
[![GitHub issues](https://img.shields.io/github/issues/Black-Forest-Interactive/open-event)](https://github.com/Black-Forest-Interactive/open-event/issues)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

Open Event is a self-hosted platform for communities to publish events and manage registrations. Members create events,
others sign up with a few clicks, and organizers keep track of participants, waiting lists and announcements. Events can
also be shared with people who have no account.

## Features

**For participants**

- Browse events as cards, list, calendar or map, with full-text search and filters (date, category, audience, availability)
- Register with a group size, get placed on a waiting list automatically when an event is full, and move up when seats free up
- Cancel with an optional message to the organizer
- Email notifications: confirmation with calendar file (`.ics`), reminder one day before the event, changes and cancellations
- Bookmarks, a personal "finder" to work through matching events, and a daily newsletter with new events

**For organizers**

- Create events in a guided wizard: description, location, capacity, categories, audiences, tags, banner image
- Guest list with contact details, CSV export and manual registration of participants
- Announcements to all participants and an optional phone number that only registered participants can see
- Share an event by link, QR code or printable poster; people without an account register by email confirmation
- PDF exports (flyer, notice, cards)

**For administrators (backoffice)**

- Manage events, accounts, categories, audiences and addresses
- Mail queue and history, newsletter subscribers, a switch per notification type
- Search index maintenance, cache control, settings, audit history, usage metrics, feedback and issue reports

## Architecture

```
                    ┌──────────────┐   ┌──────────────┐   ┌──────────────┐
                    │  portal-app  │   │ external-app │   │  admin-app   │
                    │   (members)  │   │ (no login)   │   │ (backoffice) │
                    └──────┬───────┘   └──────┬───────┘   └──────┬───────┘
                           │   nginx: static files + /api proxy  │
                           └──────────────────┬──────────────────┘
                                       ┌──────┴───────┐
                                       │   backend    │
                                       │  (REST API)  │
                                       └──┬───┬───┬───┘
                          ┌───────────────┘   │   └───────────────┐
                    ┌─────┴──────┐     ┌──────┴─────┐      ┌──────┴─────┐
                    │ PostgreSQL │     │ OpenSearch │      │    SMTP    │
                    └────────────┘     └────────────┘      └────────────┘
```

Authentication is delegated to [Keycloak](https://www.keycloak.org/) (OpenID Connect). The frontends obtain a token from
Keycloak; the backend validates it and authorizes requests by realm roles.

| Part     | Stack                                                                                    |
|----------|------------------------------------------------------------------------------------------|
| Backend  | Kotlin, Micronaut 5, JDK 25, Micronaut Data JDBC, Flyway, Velocity (mail and PDF templates) |
| Frontend | Angular 22, Angular Material, Tailwind CSS, Nx monorepo                                  |
| Data     | PostgreSQL, OpenSearch                                                                   |
| Auth     | Keycloak (JWT, realm roles)                                                              |

## Repository layout

```
backend/      Kotlin/Micronaut REST API
  src/main/kotlin/de/sambalmueslie/openevent/
    core/             domain logic (event, registration, participant, notification, search, export, ...)
    gateway/          REST controllers: portal (members), backoffice (admins), external (anonymous)
    infrastructure/   mail, audit, metrics, cache, settings, geo lookup, Telegram
  src/main/resources/db/migration/   Flyway schema
frontend/     Nx workspace
  apps/portal-app     member portal
  apps/external-app   public pages for shared events
  apps/admin-app      backoffice
  libs/               core (API models), portal / admin / external (API services), ui (components), shared (utilities)
templates/    HTML mail templates (imported into the database via the backoffice)
bruno/        API request collection for Bruno
```

## Getting started

### Prerequisites

- JDK 25
- Node.js 24 and npm
- Docker or Podman (for PostgreSQL, OpenSearch and the integration tests)
- A Keycloak realm with a public client for the frontends

### Backend

The backend expects PostgreSQL on `localhost:5432` (database `postgres`, user and password `postgres` by default; the
schema `open_event` is created by Flyway) and OpenSearch on `localhost:9200`.

```sh
cd backend
./gradlew run
```

Configuration is done through Micronaut properties, which can be set as environment variables
(`mail.server` becomes `MAIL_SERVER`). The most important ones:

| Property                                                    | Purpose                                              | Default                 |
|-------------------------------------------------------------|------------------------------------------------------|-------------------------|
| `datasources.default.url` / `.username` / `.password`       | PostgreSQL connection                                | local `postgres`        |
| `micronaut.security.token.jwt.signatures.jwks.keycloak.url` | JWKS endpoint of the Keycloak realm                  | empty (required)        |
| `opensearch.host` / `.port` / `.https` / `.user` / `.password` | OpenSearch connection                             | `localhost:9200`, HTTP  |
| `opensearch.prefix`                                         | Index name prefix, to share one cluster              | empty                   |
| `mail.enabled`                                              | Send mails; when `false` they are only stored        | `true`                  |
| `mail.server` / `.port` / `.username` / `.password`         | SMTP server                                          | port `25`               |
| `mail.transport-strategy`                                   | `SMTP`, `SMTPS` or `SMTP_TLS`                        | `SMTPS`                 |
| `mail.from-address` / `mail.reply-to-address`               | Sender addresses                                     | empty                   |
| `img.upload-path`                                           | Directory for event banners                          | `/opt/upload`           |
| `img.max-file-size`                                         | Upload limit                                         | `1MB`                   |
| `app.max-participant-size`                                  | Largest group a participant can register             | `10`                    |
| `app.external-participant-expires`                          | Lifetime of an unconfirmed external registration (ISO-8601 duration) | 24 hours |
| `telegram.enabled` / `.bot-token` / `.chat-id`              | Optional Telegram notifications about event changes  | disabled                |
| `metrics.visitor-tracking-enabled`                          | Count anonymous visitors of shared events (sets a cookie) | `false`            |

The API is documented with OpenAPI; Swagger UI is served at `/swagger-ui`.

### Frontend

```sh
cd frontend
npm ci
npx nx serve portal-app      # http://localhost:4200
npx nx serve admin-app       # http://localhost:4201
npx nx serve external-app    # http://localhost:4202
```

The dev servers proxy `/api` to `http://localhost:8080`. The Keycloak URL, realm and client id are set in
`apps/<app>/src/environments/environment*.ts`.

### Roles

Access is controlled by Keycloak realm roles named `<area>.<level>`, for example `event.read`, `event.write`,
`registration.write` or `account.write`. A regular member needs the `read` and `write` roles of `account`, `event`,
`registration`, `address`, `activity`, `category` and `audience`, plus `feedback.write`. Backoffice users need
`backoffice.access` and the `admin` role of each area they manage (`event.admin`, `mail.admin`, `settings.admin`, ...).
The full list is in `frontend/apps/*/src/shared/roles.ts`.

### Mail templates

Mails are rendered from templates stored in the database, one per notification type (`event.reminder`,
`registration.participant.accepted`, ...). The HTML files in `templates/` are the reference versions; import them in the
backoffice. A notification type without a template is skipped with a warning in the log. Values inserted into HTML
templates are escaped automatically.

## Testing

```sh
cd backend
./gradlew test
```

Integration tests start PostgreSQL and OpenSearch with Testcontainers, so a container runtime must be running. With
Podman, point Testcontainers at the user socket:

```sh
DOCKER_HOST=unix://$XDG_RUNTIME_DIR/podman/podman.sock TESTCONTAINERS_RYUK_DISABLED=true ./gradlew test
```

The frontend is a thin client and has no unit tests; check it with:

```sh
cd frontend
npx nx run-many -t lint build
```

## Building images

```sh
cd backend
./gradlew jibDockerBuild                                   # image "open-event-backend"

cd frontend
docker build -f Dockerfile_portal_prd -t open-event-portal .   # portal-app + external-app behind nginx
docker build -f Dockerfile_admin_prd  -t open-event-admin  .   # admin-app behind nginx
```

The frontend images serve the static files with nginx and proxy `/api/` to a host named `open-event-backend-service`
on port 8080 (see `frontend/nginx.conf`). The portal image reads `MAPS_API_KEY` at container start for the map tiles.

## Contributing

Issues and pull requests are welcome. Development happens on the `development` branch; `master` holds releases. Please
read the [Code of Conduct](CODE_OF_CONDUCT.md) first.

To report a security problem, please contact the maintainers privately instead of opening a public issue.

## License

[Apache License 2.0](LICENSE)
