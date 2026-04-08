## Migration Rules

- DO NOT restructure folder layout — keep existing feature boundaries
- Migrate services to `inject()` function, not constructor DI
- Replace `@Input()` / `@Output()` with signal inputs where applicable
- Use `resource()` for async data fetching, replacing RxJS-based HTTP calls gradually
- Keep RxJS for complex event streams — don't force-replace everything