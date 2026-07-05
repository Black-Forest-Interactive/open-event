# Backend Code Style

Kotlin / Micronaut backend. No detekt/ktlint/spotless is configured — these conventions are enforced manually.

## Controllers are pure REST definitions

A `@Controller` class must contain nothing but the HTTP route definitions: annotations, method signature, and a
one-line delegation to a service. No permission checks, no audit logging, no business logic, no branching.

All authorization (`checkPermission`), audit logging (`AuditService` / `traceCreate` / `traceUpdate` / ...), and any
non-trivial logic belongs in a dedicated `<Feature>GuardService` (`@Singleton`) living in the same package as the
controller. The controller injects the guard service instead of the underlying CRUD/domain services and simply
forwards each call.

Reference implementation: `gateway/app/event/EventController.kt` + `gateway/app/event/EventGuardService.kt`.

```kotlin
// ✅ correct — EventController.kt
@Controller("/api/app/event")
class EventController(private val service: EventGuardService) {
    @Post()
    fun create(auth: Authentication, @Body request: EventChangeRequest) = service.create(auth, request)
}

// EventGuardService.kt
@Singleton
class EventGuardService(private val service: EventCrudService, ...) {
    companion object {
        private const val PERMISSION_WRITE = "event.write"
    }

    fun create(auth: Authentication, request: EventChangeRequest): Event =
        auth.checkPermission(PERMISSION_WRITE) {
            probe.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
}

// ❌ wrong — permission check and audit logging living in the controller
@Controller("/api/app/event")
class EventController(private val service: EventCrudService, private val accountService: AccountCrudService) {
    @Post()
    fun create(auth: Authentication, @Body request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_WRITE) {
            service.create(accountService.find(auth), request)
        }
    }
}
```

Only skip the guard service when a controller genuinely has no authorization/business logic to extract (e.g. public
`gateway/external/*` endpoints secured with `@Secured(SecurityRule.IS_ANONYMOUS)` that just forward to a service).

Controllers under `core/*` and `infrastructure/*` (not `gateway/*`) are core API implementations, not gateways — this
rule does not apply to them.

## Controller and guard service methods are expression bodies

Write every method as a single expression (`fun x(...) = EXPR`), not a block body with an explicit `return`. This
holds even when the delegated call takes a multi-statement lambda (e.g. `checkPermission(...) { ... }`) — what matters
is that the *function* body is one top-level statement, not that the whole line is short.

```kotlin
// ✅ correct
@Post()
fun create(auth: Authentication, @Body request: EventChangeRequest) = service.create(auth, request)

fun update(auth: Authentication, id: Long, request: EventChangeRequest) =
    auth.checkPermission(PERMISSION_WRITE) {
        val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission create(auth, request)
        probe.traceUpdate(auth, request) { service.update(account, event.id, request) }
    }

// ❌ wrong — block body with explicit return for a single-expression method
@Post()
fun create(auth: Authentication, @Body request: EventChangeRequest): Event {
    return service.create(auth, request)
}
```

Only keep a block body when the method genuinely has more than one top-level statement (e.g. two independent guard
clauses followed by a separate `return`, as in `ImageGuardService.getImage`).

Drop the explicit return type on expression-body functions unless the lambda has multiple exit points with values the
compiler can't unify without help (e.g. an early `return@checkPermission 0` alongside a `Long`-returning branch, or a
generic factory like `Page.empty()`) — in that case keep the return type so it provides the expected-type context.
