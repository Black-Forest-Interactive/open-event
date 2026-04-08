# Code Style Rules

## Formatting

- No semicolons anywhere — not in TypeScript, not in templates
- Single quotes for strings
- 2-space indentation
- Use `const` for never reassigned values like `const eventId = this.eventId()`

---

## Data Modeling

### File structure

For each business object there is a separate folder that contains the code.
Like `libs/portal/src/lib/account` for the account related data.
If the core api is used the directory contains only a service with the name of the business object (same like the
directory)
So if the business object is an account the directory is called account and the service is called `account.service.ts`
If some specific api for the app is necessary, cause there is some limitation of data for security or privacy reasons,
there will be also an file for the api (interfaces and classes) with `*.api.ts`. In that example its called
`account.api.ts`

### Interfaces — for data coming FROM the backend (responses, DTOs)

Use `interface` when the shape is owned by the backend. These are read-only contracts — no constructor, no methods.

```typescript
// ✅ correct
export interface Account {
    id: number
    externalId: string | undefined
    name: string
    iconUrl: string

    registrationDate: string
    lastLoginDate: string | undefined

    serviceAccount: boolean
    idpLinked: boolean
}

export interface AccountInfo {
    id: number
    name: string
    iconUrl: string
    email: string
    firstName: string
    lastName: string
}

// ❌ wrong — class used for backend response
class UserResponse {
  id: string
  email: string
}
```

### Classes — for data going TO the backend (request bodies, commands)

Use `class` when you are constructing a payload to send. These may have a constructor and helper methods for
building/transforming the payload.

```typescript
// ✅ correct
export class AccountChangeRequest {
    constructor(
        public name: string,
        public iconUrl: string,
        public externalId: string | undefined
    ) {}
}

// ❌ wrong — interface used for outgoing request
interface CreateUserRequest {
    email: string
    password: string
}
```

---

## Backend Services — thin access layer, no logic

Backend services are HTTP adapters only. They map Angular `HttpClient` calls to typed responses. **No business logic, no
data transformation, no state.**

Rules:

- One method per endpoint
- Return the `Observable` directly — no `.pipe()` unless it's a single `map` to cast the type
- No `if` statements, no data manipulation, no side effects
- No injected stores or other services
- always extend BaseService and use the methods from there
- File naming: `*.service.ts`

```typescript
// ✅ correct

@Injectable({
    providedIn: 'root'
})
export class AccountService extends BaseService {
    constructor() {
        super('app/account')
        this.retryCount = 1
    }

    getAccount(): Observable<Account> {
        return this.get('')
    }

    updateAccount(request: AccountChangeRequest): Observable<Account> {
        return this.put('', request)
    }

    validate(lang: string): Observable<AccountValidationResult> {
        const params = new HttpParams().set('lang', lang)
        return this.get('validate', params)
    }

    getProfile(): Observable<Profile> {
        return this.get('profile')
    }

    updateProfile(request: ProfileChangeRequest): Observable<Profile> {
        return this.put('profile', request)
    }

    getPreferences(): Observable<Preferences> {
        return this.get('preferences')
    }

    updatePreferences(request: PreferencesChangeRequest): Observable<Preferences> {
        return this.put('preferences', request)
    }
}


// ❌ wrong — logic inside the API service
@Injectable({providedIn: 'root'})
export class UserApiService {
    private readonly http = inject(HttpClient)
    private readonly store = inject(UserStore) // ❌ no store injection

    getById(id: string): Observable<UserResponse> {
        return this.http.get<UserResponse>(`/api/users/${id}`).pipe(
            tap(user => this.store.setUser(user)), // ❌ no side effects
            map(user => ({...user, displayName: user.email.split('@')[0]})) // ❌ no transformation
        )
    }

    create(request: CreateUserRequest): Observable<UserResponse> {
        if (!request.email) throw new Error('Email required') // ❌ no validation/logic
        return this.http.post<UserResponse>('/api/users', request)
    }
}
```

Business logic, caching, error handling, and state updates belong in a dedicated feature service or store — not in the
API service.

---

## Components

For a component use the following structure

- Injected services and classes
- Inputs
- Outputs
- Signals
- Resources and computed signals
- Constructor
- Methods

### Usage of resources

For the usage of the resources api use the following pattern

- The resource itself should be always private
- Use computed signals for providing the data
- If there is more then one signal triggering the resource use a signal called *Criteria
- Try to write the code as compact as possible
- Always add the abortSignal-

```typescript
export class AccountComponent {
    // ✅ correct
    data = input.required<Account>()

    private page = signal(0)
    private size = signal(20)

    private addressCriteria = computed(() => ({
        data: this.data(),
        page: this.page(),
        size: this.size()
    }))

    private addressResource = resource({
        params: this.addressCriteria,
        loader: (param) => toPromise(this.service.getAddress(param.params.data.id, param.params.page, param.params.size), param.abortSignal)
    })

    private result = computed(this.addressResource.value ?? undefined)

    readonly address = computed(() => this.result()?.content ?? [])
    readonly totalSize = computed(() => this.result()?.totalSize ?? 0)
    readonly loading = this.addressResource.isLoading
    readonly error = this.addressResource.error


    handlePageChange($event: PageEvent) {
        this.page.set($event.pageIndex)
        this.size.set($event.pageSize)
    }


    // ❌ wrong — not using the resource in a compact way
    private addressResource = resource({
        params: this.addressCriteria,
        loader: (param) => {
            return toPromise(this.service.getAddress(param.params.data.id, param.params.page, param.params.size))
        }
    })

    // ❌ wrong — expose the data public and not read only
    page = signal(0)
    size = signal(20)

    addressCriteria = computed(() => ({
        data: this.data(),
        page: this.page(),
        size: this.size()
    }))

    addressResource = resource({
        params: this.addressCriteria,
        loader: (param) => {
            return toPromise(this.service.getAddress(param.params.data.id, param.params.page, param.params.size))
        }
    })

    result = computed(this.addressResource.value ?? undefined)

    address = computed(() => this.result()?.content ?? [])
    totalSize = computed(() => this.result()?.totalSize ?? 0)
    loading = this.addressResource.isLoading
    error = this.addressResource.error
}
```

### Loading data from route params

When a component loads data based on a route param, use `toSignal()` on `route.paramMap` to derive the id as a signal,
then drive a `resource()` from it. This replaces the `OnInit` + `paramMap.subscribe()` pattern entirely.

```typescript
// ✅ correct — reactive, no OnInit, no manual loading state
private eventId = toSignal(this.route.paramMap.pipe(map(p => { const id = p.get('id'); return id ? +id : undefined })))

private eventResource = resource({
    params: this.eventId,
    loader: (p) => p.params ? toPromise(this.service.getEvent(p.params), p.abortSignal) : Promise.resolve(undefined)
})

private event = computed(() => this.eventResource.value())
readonly reloading = this.eventResource.isLoading
```

When the component also needs a manual re-fetch trigger (e.g. after a mutation), call `resource.reload()`:

```typescript
// ✅ correct
reload() { this.infoResource.reload() }
```

When a mutation returns the updated resource value directly, set it on the resource instead of re-fetching:

```typescript
// ✅ correct — avoids a round-trip when the response contains the updated value
setSharingEnabled(enabled
:
boolean
)
{
    const id = this.eventId()
    if (!id) return
    this.service.setShared(id, enabled).subscribe((d) => this.infoResource.set(d))
}
```

```typescript
// ❌ wrong — manual subscribe/signal pattern
constructor()
{
    this.route.paramMap.pipe(takeUntilDestroyed()).subscribe((p) => {
        const idParam = p.get('id')
        this.eventId.set(idParam !== null ? +idParam : undefined)
        this.reload()
    })
}

reload()
{
    const id = this.eventId()
    if (!id || this.reloading()) return
    this.reloading.set(true)
    this.service.getEventInfo(id).subscribe((d) => {
        this.info.set(d);
        this.reloading.set(false)
    })
}
```

---

### Computed signals for template properties

When a component holds an optional or complex signal (e.g. set from outside or loaded async), expose each property used
in the template as a dedicated `readonly computed` signal with a safe default value. Never access `data()?.property`
directly in templates.

Default value rules:

- `string` → `''`
- `number` → `0`
- `boolean` → `false`
- objects / arrays → no `?? null` needed — optional chaining (`?.`) already returns `undefined`, which is falsy in
  templates; only add a default when the type contract requires it

```typescript
// ✅ correct
data = signal<EventSearchEntry | undefined>(undefined)

readonly title = computed(() => this.data()?.title ?? '')
readonly owner = computed(() => this.data()?.owner)
readonly start = computed(() => this.data()?.start ?? '')
```

```html
<!-- ✅ correct — use the computed signals -->
<h2>{{ title() }}</h2>
<portal-account [account]="owner()"></portal-account>
<p>{{ start() | date:'medium' }}</p>

<!-- ❌ wrong — accessing signal properties directly in template -->
<h2>{{ data().title }}</h2>
<p>{{ data()?.start | date:'medium' }}</p>
```

---

### SCSS/CSS Classes

Use always the tailwind classes and use as little as possible. Keep it simple stupid.

---

## Internationalisation

Always use `@ngx-translate/core` for user-visible strings. Never hardcode display text.

- Import `TranslatePipe` in the component's `imports` array
- Use the pipe in templates: `{{ 'some.key' | translate }}`
- Pass translation keys as string inputs where the component itself renders the label — the pipe is applied inside the
  component template, not by the caller
  appp

### Key structure

Top-level keys are lowercase feature names (`event`, `account`, `address`, …). Each feature follows a consistent set of
standard sub-namespaces:

| Sub-key                  | Purpose                                                       | Example                             |
|--------------------------|---------------------------------------------------------------|-------------------------------------|
| `type`                   | Singular noun for the feature (nav labels, chips)             | `event.type` → "Event"              |
| `title`                  | Page / section heading                                        | `event.title` → "Event"             |
| `search`                 | Search field placeholder                                      | `event.search` → "Search for event" |
| `create`                 | Top-level create button label                                 | `event.create` → "Create new event" |
| `action.*`               | User-triggered actions (buttons, links)                       | `event.action.publish`              |
| `form.*`                 | Form field labels                                             | `event.form.title`                  |
| `form.hint.*`            | Field hints / helper text                                     | `event.form.hint.startDate`         |
| `form.error.*`           | Validation error messages                                     | `event.form.error.title`            |
| `table.*`                | Table column headers                                          | `event.table.status`                |
| `property.*`             | Read-only property labels (detail views)                      | `event.property.published`          |
| `dialog.<type>.title`    | Dialog heading — type is `create`, `edit`, `change`, `delete` | `event.dialog.delete.title`         |
| `dialog.<type>.question` | Confirmation question inside a dialog                         | `event.dialog.delete.question`      |
| `message.*`              | Success / error feedback (snackbar, inline)                   | `registration.message.accepted`     |
| `status.*`               | Status enum display values                                    | `registration.status.full`          |
| `filter.*`               | Filter panel labels                                           | `event.filter.reset`                |
| `details.*`              | Tab / section labels in detail views                          | `account.details.profile`           |
| `step.*`                 | Stepper step labels                                           | `event.step.location`               |

**Global (not feature-scoped):**

- `action.*` — shared actions reused across features: `action.save`, `action.cancel`, `action.logout`, …
- `paginator.*` — shared paginator labels
- `lang.*` — language names

**Naming conventions inside keys:**

- Sub-keys are camelCase: `startDate`, `maxGuests`, `firstName`
- Enum value keys are SCREAMING_SNAKE_CASE matching the backend enum: `status.ACCEPTED`, `status.IN_PROGRESS`
- Dynamic interpolation uses double curly braces: `"Update event: {{ event }}"`

