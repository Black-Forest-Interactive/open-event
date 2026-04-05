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
- Keep signals `private` inside components; expose only `readonly` computed signals for the template
- Do not pass signals as `@Input()` — use signal inputs (`input()`) instead
- Do not subscribe to signals manually — use `computed()` or `resource()` to react to changes

```typescript
// ✅ correct — private signal, readonly computed exposed
private page = signal(0)
readonly currentPage = computed(() => this.page())

// ❌ wrong — public mutable signal
page = signal(0)
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

Do not manually call `load()` or imperatively trigger re-fetches — update a signal instead and let the resource react.
