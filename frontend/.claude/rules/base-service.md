# BaseService

`BaseService` is the abstract base class for all HTTP services. It lives at `libs/shared/src/lib/base-service.ts` and is exported from `@open-event/shared`.

## Setup

Every service extends `BaseService` and passes a URL prefix to `super()`. The prefix is appended after the injected `BASE_API_URL` (default: `api/`).

```typescript
@Injectable({ providedIn: 'root' })
export class AccountService extends BaseService {
    constructor() {
        super('app/account')
        this.retryCount = 1   // optional — default is 3
    }
}
```

Resulting base URL: `api/app/account`

## retryCount

`protected retryCount = 3` — number of automatic retries on failed GET/PUT/PATCH requests. Override in the constructor when retries are not desired (e.g. set to `1` for one-time actions).

`post` and `delete` do **not** retry automatically.

## Methods

| Method | Signature | Description |
|--------|-----------|-------------|
| `get` | `get<T>(suffix, params?)` | GET request, returns `Observable<T>` |
| `getAll` | `getAll<T>(suffix?)` | GET request returning an array `Observable<T[]>` |
| `getPaged` | `getPaged<T>(suffix, page, size, params?, queryParams?)` | GET with `?page=&size=` pagination, returns `Observable<Page<T>>` |
| `postPaged` | `postPaged<T>(suffix, body, page, size, params?)` | POST with pagination (search endpoints), returns `Observable<Page<T>>` |
| `post` | `post<T>(suffix, body, params?)` | POST request, returns `Observable<T>` |
| `put` | `put<T>(suffix, body)` | PUT request, returns `Observable<T>` |
| `patch` | `patch<T>(suffix, body)` | PATCH request, returns `Observable<T>` |
| `delete` | `delete<T>(suffix)` | DELETE request, returns `Observable<T>` |
| `getBlob` | `getBlob(suffix)` | GET binary file, returns `Observable<HttpResponse<Blob>>` |
| `postBlob` | `postBlob(suffix, body)` | POST then receive binary file, returns `Observable<HttpResponse<Blob>>` |

## URL construction

`createUrl(suffix)` builds the final URL:

- `suffix` is empty → `{api}{urlPrefix}` (e.g. `api/app/account`)
- `suffix` is set → `{api}{urlPrefix}/{suffix}` (e.g. `api/app/account/profile`)

Pass `''` as suffix when calling the root of the prefix.

## Examples

```typescript
// GET api/app/account
getAccount(): Observable<Account> {
    return this.get('')
}

// GET api/app/account/profile
getProfile(): Observable<Profile> {
    return this.get('profile')
}

// GET api/app/account with query params
validate(lang: string): Observable<AccountValidationResult> {
    const params = new HttpParams().set('lang', lang)
    return this.get('validate', params)
}

// GET api/app/account/addresses?page=0&size=20
getAddresses(page: number, size: number): Observable<Page<Address>> {
    return this.getPaged('addresses', page, size)
}

// PUT api/app/account
updateAccount(request: AccountChangeRequest): Observable<Account> {
    return this.put('', request)
}

// DELETE api/app/account/addresses/42
deleteAddress(id: number): Observable<void> {
    return this.delete(`addresses/${id}`)
}
```
