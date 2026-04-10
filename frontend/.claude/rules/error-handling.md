# Error Handling

## User-facing errors — HotToastService

All user-visible error and success messages go through `HotToastService` from `@ngxpert/hot-toast`. Never use
`MatSnackBar` directly for feedback.

```typescript
private toast = inject(HotToastService)
```

Always use translated strings — never hardcode text. Resolve the translation first, then pass it to the toast:

```typescript
// ✅ correct
this.translateService.get('event.message.saved').subscribe(t => this.toast.success(t))
this.translateService.get('event.message.error').subscribe(t => this.toast.error(t))

// ❌ wrong — hardcoded text
this.toast.success('Saved successfully')
this.toast.error('Something went wrong')
```

Translation keys for feedback messages follow the pattern `<feature>.message.<action>` (see i18n rules).

## Error handling in subscriptions

Handle errors in the `error` callback of `subscribe`. Reset loading state and show a toast:

```typescript
this.service.save(request).subscribe({
    next: () => {
        this.translateService.get('event.message.saved').subscribe(t => this.toast.success(t))
        this.loading = false
    },
    error: () => {
        this.translateService.get('event.message.error').subscribe(t => this.toast.error(t))
        this.loading = false
    }
})
```

## Error handling with resource()

When using `resource()`, expose the error signal and handle display in the template:

```typescript
readonly error = this.myResource.error
```

```html
@if (error()) {
    <p>{{ 'event.message.error' | translate }}</p>
}
```

Do not show raw error objects or HTTP status codes to the user.
