# State Management

## No global store

There is no NgRx or any other global state library. All state is managed locally using Angular Signals.

## Where state lives

- **Component-local state** — signals declared inside the component itself
- **Feature service state** — if multiple components in a feature need shared state, it lives in the feature service as signals
- No shared cross-feature state — the backend is the source of truth; re-fetch instead of syncing

## Rules

- Use `signal()` for mutable state
- Use `computed()` for derived state — never derive in a method called from a template
- If a signal is only written internally and never read by the template, make it `private`
- If a signal is read by the template, declare it `readonly` (not `private`) — `readonly` prevents reference reassignment while still allowing `.set()` internally; do not duplicate it as a separate `computed()`
- Do not pass signals as `@Input()` — use signal inputs (`input()`) instead
- Do not subscribe to signals manually — use `computed()` or `resource()` to react to changes

```typescript
// ✅ correct — template-readable signal declared readonly
readonly loading = signal(false)

// ✅ correct — signal only used internally, stays private
private page = signal(0)

// ❌ wrong — public mutable signal exposed without readonly
loading = signal(false)

// ❌ wrong — unnecessary duplication; just use readonly signal directly
private loadingSignal = signal(false)
readonly loading = computed(() => this.loadingSignal())
```

## Triggering re-fetches

When multiple signals drive a data fetch, combine them into a criteria computed signal and pass it to `resource()`. Changing any input signal automatically re-triggers the loader.

```typescript
private criteria = computed(() => ({ page: this.page(), size: this.size(), filter: this.filter() }))

private resource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.service.search(p.params), p.abortSignal)
})
```

Do not manually call `load()` or imperatively trigger re-fetches by updating state signals — update a signal instead and let the resource react. Exception: `resource.reload()` is acceptable for one-off re-fetches after user actions (e.g. after a mutation when the response doesn't contain the updated value). `resource.set(value)` is preferred when the mutation response already contains the new value.
