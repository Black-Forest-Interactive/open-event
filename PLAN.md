# Open Event Design Integration — Status

Tracks implementation of the Claude Design handoff ("Open Event" prototype) into `frontend/apps/portal-app`.
Full approved spec: `/home/oli.e/.claude/plans/fetch-this-design-file-linear-clover.md` (Phase 1).
Source design files: `/tmp/design-fetch/open-house/project/` (`Open Event.html`, `app/app.jsx`, `app/data.jsx`, `app/ui.jsx`).

**Hard constraints (still in force):**
- Do **not** touch `AppFooterComponent` (`frontend/libs/ui/src/lib/layout/app-footer/`).
- App shell (toolbar/sidenav) already matches the design — no changes needed there.
- Reuse the existing backend wherever possible; add only minimal stubs for missing data.

---

## Done

### 1. Shared UI primitives (`frontend/libs/ui/src/lib/`)
- `theme/theme.scss` — added `--ev-ok/mid/warn/danger/on-danger` availability tokens to both `html` and `html.dark` blocks.
- `theme/base.scss` — added `--color-secondary*` mappings to `@theme inline` (needed for `lib-avatar`).
- `category/category-style.ts` (NEW) — `getCategoryStyle(name)`: hue + Material Symbols icon lookup per category, hash fallback for unmapped names.
- `category/category-chip/` (NEW) — `lib-category-chip`, renders `.catchip` pill with OKLCH hue-tinted background, dark-mode variant via `:host-context(.dark)`.
- `avatar/` (NEW) — `lib-avatar`, initials circle (`bg-secondary`/`text-on-secondary`), sizes sm/md/lg.
- `registration/registration-status/` (REWRITTEN) — new `.status` bar with `level` (`ok|mid|low|full`), color-coded fill + pulsing dot on low availability. Kept existing `data`/`entry`/`public` input setters. **Removed the `vertical` input** (see "Known breakages" below).

### 2. Backend stub: category filter
- `backend/.../search/api/EventSearchRequest.kt` — added `categories: Set<String> = emptySet()`.
- `backend/.../search/event/EventSearchQueryBuilder.kt` — added `terms()` filter on `categories` when non-empty.
- `frontend/libs/core/src/lib/search/search.api.ts` — `EventSearchRequest` class + `defaultEventSearchRequest()` extended with `categories: string[] = []`.

### 3. Events board service (`event-board.service.ts`)
- Added `layout` signal (`'cards'|'rows'|'calendar'|'map'`, default `'cards'`) + `setLayout()`.
- Added `categoryFilter` signal (`Set<string>`) + `toggleCategory(name)`, wired into the `criteria` computed's `EventSearchRequest` (7th arg).
- `handlePreselectionChanged` reworked for the design's "when" pills: `any` (clears range), `today`, `weekend` (Sat+Sun of current week), `next_week`.
- `resetFilter()` now also clears `categoryFilter`.

### 4. Events board shell + filter
- `event-board.component.ts` / `.html` — local `mode` signal removed, layout driven by `service.layout()`. Header row: search field, result count, 4-way `mat-button-toggle-group` (cards/rows/calendar/map, icons `grid_view`/`view_list`/`calendar_month`/`map`), mobile filter icon button (badge = `activeFilterCount()`, opens `EventBoardFilterComponent` in a `MatBottomSheet` via `#filterSheet` template), "Veranstaltung erstellen" button (`matButton="filled"` `color="accent"`, `routerLink="./create"`). Active-filter chip row below the header (when/category/only-available chips with `close` icon to remove + "reset" chip), shown when `activeFilterCount() > 0`. `@switch (service.layout())` renders `EventBoardMapComponent` / `EventBoardCalendarComponent` / `EventBoardListComponent` (default). Desktop sidebar keeps `EventBoardFilterComponent` (hidden on mobile, `!mobileView()`).
- `event-board-filter.component.html` — restyled to design's `FilterControls` inside a single `mat-card`: "when" pill row (`whenOptions`, filled/outlined toggle via `selectWhen`/`isWhenActive`), category pill row (`lib-category-chip` per `CategoryService` category, ring highlight when active in `service.categoryFilter()`), "only available" `mat-slide-toggle`, and a `mat-accordion`/`mat-expansion-panel` "Erweitert" section holding the existing date-range picker + history/own/participating toggles (all preserved). "Reset filter" button at the bottom.

### 5. Event cards/rows
- NEW `event-card/` (`.ecard`) — `mat-card` with `routerLink`, category-hue-tinted media header (`mat-icon` from `getCategoryStyle`), category chips, title, short text, date/time + location meta rows, `lib-registration-status`, "Details ansehen" button.
- NEW `event-row/` (`.erow`) — compact horizontal `mat-card` variant: small hue-tinted icon tile, title + category chips, date/location meta, `lib-registration-status` (compact, hidden below `sm`), chevron.
- `event-board-list.component.html` rewritten — empty state restyled (`search_off` icon + title/subtitle), `@switch (service.layout())` renders a responsive grid of `EventCardComponent` (`cards`) or stacked `EventRowComponent` (`rows`); infinite-scroll (`ScrollNearEndDirective`) and "load more" button preserved. `event-board-list-entry/` deleted.

### 6. Map/calendar/table
- `event-board-map-popup/` (`EventBoardMapPopupComponent`) — restyled to `.evpop`: category chips row, bold title, time/owner/location meta rows (`text-on-surface-variant`, MD3 tokens — no more hardcoded `#e0e0e0`/`#555`/`#888`), `lib-registration-status` (compact), Details/Close buttons. SCSS reduced to `.evpop { font-family: inherit }`.
- `event-board-map/event-board-map.component.scss` — `.map-container` now has `border-radius: inherit; overflow: hidden;` so the Leaflet canvas respects the wrapping `mat-card`'s rounded corners. Leaflet/`markerClusterGroup`/CARTO tile setup unchanged. `.mapview` venue-grouped sidebar list **deferred to Phase 4** (not implemented).
- `event-board-calendar/` — wrapped `full-calendar` in a padded (`p-3`) div inside `mat-card`; added `:host` CSS variable overrides (`--fc-*`) mapping FullCalendar's theme to `--mat-sys-*` / `--ev-danger` tokens (border/button/today/event/now-indicator colors) so it adapts to light/dark MD3 themes. FullCalendar `dayGridMonth` view itself unchanged — full `.agenda` rebuild deferred to Phase 4.
- **`event-board-table/` deleted** entirely (component/html/scss) — no remaining references; `npx nx build portal-app --configuration=development` succeeds.

---

### 7. Event detail composition (task 7/10)
- `event-details-banner` → hero: category-tinted `.hero-placeholder` (OKLCH hue from `getCategoryStyle`) shown when no banner image is set, floating category chips (top-left via `CategoryChipComponent`), edit/upload overlay kept (restyled `matIconButton` + `mat-progress-bar`).
- NEW `event-fact-row/` — `.factrow`-style 3-card row (date, start–end time + duration in minutes, venue street/zip-city from `info().location`); venue card hidden when `!hasLocation()`. New i18n keys `event.detail.when`/`event.detail.minutes`.
- `event-details-info/` rewritten — `event-fact-row` at top, `<h1>` title + `shortText` lead, category chips via `CategoryChipComponent` + plain `#tag` pills, "about" block (`longText` via `[innerHTML]` with Tailwind `prose`/`prose-sm`, dark-mode `--tw-prose-*` overrides scoped to the component). Removed old owner/date header row (now in fact-row) and the unused `info().event.imageUrl` secondary image. New i18n keys `event.detail.about`/`event.detail.host`.
- NEW `event-host-block/` — `.organizer`-style block (`lib-avatar` + `AccountDisplayNamePipe` name + "Veranstalter"/`event.detail.host` label), `border-t` divider.
- `event-details.component.html` — main column is now a single `mat-card` containing `event-details-info` + `event-host-block`; `share-details` (if `canEdit() && share()`) is a separate card below. Aside column (`registration-details` booking card [still old rendering, task 8] + `event-details-location`) is `lg:sticky lg:top-3 lg:self-start`.
- `libs/ui/src/lib/theme/base.scss` — registered `@plugin "@tailwindcss/typography"` (dependency was already present, now wired up) for the `prose` classes used in the about section.

### 8. Guest list / participants / booking card / bookbar (task 8/10)
- **State ownership moved up**: `event-details.component.ts` now owns all registration-mutation logic (`participateSelf`/`editSelf`/`cancelSelf` + private `request*Self` + `handleParticipateResponse`, with a `registrationReloading` signal and `userParticipant` computed matching `info().registration?.participants` against `authService.getPrincipal()?.email`). This was moved out of `registration-details.component.ts` since both the booking card (desktop aside) and the new mobile `event-bookbar` are viewport-mutually-exclusive consumers of the same data + actions.
- NEW `event-guest-list/` (organizer, rendered inside the main info card when `canEdit()`): loads `RegistrationService.getDetails(registrationId)` via `resource()`, renders `.guestlist`-style rows (`lib-avatar`, name via `AccountDisplayNamePipe`, mailto/tel links, persons count, "seit"+timestamp, "Du" badge for the organizer's own entry via email match), separate waitlist section (reuses `registration.waitlist` divider pattern from the old participant list), "Alle anschreiben" (`mailto:?bcc=`) + "Exportieren" (CSV via `FileSaver.saveAs`, BOM-prefixed, `teilnehmer-{eventId}.csv`) text buttons, "Nur für dich als Veranstalter sichtbar" badge. New i18n keys `registration.guestlist.you`.
- NEW `event-participants-stack/` (attendee, rendered inside the main info card when `!canEdit() && registration()`): "you" row (avatar + name + size + "Angemeldet" badge, `bg-secondary-container`) if `userParticipant()` is set, plus an anonymized avatar stack (`.avstack-item`, OKLCH hue-cycled generic person icons, up to 6 + "+N" overflow) sized to total accepted persons. Empty state reuses `registration.empty`.
- `registration-details.component.ts/.html` rewritten as a "dumb" sticky booking card: `info = input.required<EventInfo>()`, `userParticipant = input<Participant|undefined>()`, `reloading = input(false)`, outputs `participate/edit/cancel`. Organizer variant: "Veranstalter" tag, big `taken/capacity Anmeldungen` count, `lib-registration-status`, "Bearbeiten" (`routerLink="/event/edit/{id}"`) + embedded `portal-event-action-export` icon button. Attendee variant: "Kostenlos" label, `lib-registration-status`, then either a "Angemeldet" confirmation pill + Edit/Cancel buttons, or a "Teilnehmen"/"Ausgebucht" primary button (disabled when full). New i18n keys `registration.organizer`/`registration.count`/`registration.free`.
- NEW `event-bookbar/` — `lg:hidden fixed bottom-0` bar, rendered when `registration()` is set. Organizer variant: `taken/capacity` + "Bearbeiten" button. Attendee variant: `lib-registration-status` (compact) + "Teilnehmen"/"Absagen"/"Ausgebucht" button. Same inputs/outputs as the booking card, wired to the same `event-details.component.ts` handlers.
- `event-details.component.html` — main card now also renders `event-guest-list`/`event-participants-stack` after `event-host-block`; aside booking card passes `[info]`/`[userParticipant]`/`[reloading]` + wires `(participate)/(edit)/(cancel)` to the parent; `event-bookbar` added as a sibling after the main `mat-card`. Content padding bumped to `pb-20 lg:pb-3` so the mobile bookbar doesn't overlap content.

### 9. Registration dialogs → bottom sheets (task 9/10)
- NEW shared util `libs/shared/src/lib/ics.ts` (`downloadIcs(event: IcsEvent)`, exported from `@open-event/shared`) — extracted from `event-details-location.component.ts`'s old `downloadICS()` (now a 4-line wrapper calling `downloadIcs({...})`). Takes a small structural type (`{title, longText, start, finish, location?}`) rather than `EventInfo`, since `libs/shared` cannot depend on `libs/core`.
- NEW `registration-participate-sheet/` (`portal-registration-participate-sheet`, `MatBottomSheet`) — replaces the old `registration-participate-dialog` (deleted) for the **self** participate/edit flow on the event-detail page only. Two-step UI: `'form'` (title from `data.titleKey` — reuses `registration.dialog.accept.title`/`registration.dialog.edit.title`, event name/time summary, persons stepper capped via `maxPersons` computed from `maxGuestAmount - taken + currentSize` (clamped 1-6), Confirm/Cancel) → `'success'` (checkmark, `registration.sheet.success.title/subtitle`, "Add to calendar" via `downloadIcs` reusing `location.action.calendar`, "Ok" via `action.ok` dismisses sheet with the `ParticipateResponse`).
- **Submission stays inside the sheet** via a `data.submit: (request: ParticipateRequest) => Observable<ParticipateResponse>` callback bound by the parent to `registrationService.addParticipant`/`changeParticipant` (with the registration id) — this lets the sheet show its own loading/success steps while `event-details.component.ts` keeps owning the `infoResource` update + toast via `handleParticipateResponse(response)` in `afterDismissed()`. `requestParticipateSelf`/`requestEditSelf` private methods removed (no longer needed).
- `registration-edit-dialog/` (old `MatDialog`) **kept as-is** — still used by `registration-moderation.component.ts` (organizer editing another participant's size, out of scope for Phase 1). Only `event-details.component.ts`'s self-edit flow was migrated to the new sheet.
- `registration-cancel-dialog` stays a `MatDialog` confirm — unchanged, no work needed.
- New i18n keys: `registration.sheet.subtitle/confirm/success.title/success.subtitle`. (`registration.sheet.persons` reuses existing `registration.peopleAmount`; success "add to calendar"/"done" buttons reuse `location.action.calendar`/`action.ok`.)

### 10. i18n + build verification (task 10/10)
- Verified all `| translate` keys used in `event/` and `registration/` directories exist in both `de.json`/`en.json`. Added missing keys discovered to be in "detail" scope (pre-existing, surfaced by the redesigned hero/header): `action.more`, `event.action.editBanner`, `event.message.uploading` (DE+EN). Left `event.admin.registration` (missing EN) and `registration.action.editParticipant`/`removeParticipant` (missing DE) alone — these belong to `registration-moderation.component.html`, the organizer admin table, which is untouched/out of Phase 1 scope.
- Fixed 2 `@angular-eslint/no-output-native` lint errors introduced in task 8: `cancel = output<void>()` collided with the native DOM `cancel` event in both `event-bookbar.component.ts` and `registration-details.component.ts`. Renamed to `cancelParticipation` in both components' `.ts`/`.html` (the `(click)="cancel.emit()"` bindings) and in the parent `event-details.component.html` bindings (`(cancelParticipation)="cancelSelf()"` on `portal-registration-details` and `portal-event-bookbar`).
- Remaining lint issues (`app.component.ts:35` non-null-assertion warning, `event-menu-item.ts:5` `no-unsafe-function-type`, `dashboard.component.ts:11` `enforce-module-boundaries`) verified via `git diff ee1d6f9` to be **pre-existing and unrelated** to this redesign (files untouched) — left as-is, out of scope.
- `npx nx lint portal-app` now passes (0 errors, only the 1 pre-existing warning). `npx nx build portal-app --configuration=development` succeeds (only the pre-existing Sass `@import "tailwindcss"` deprecation warning in `libs/ui/src/lib/theme/base.scss:60`).
- `npx nx serve portal-app` / interactive browser QA (board layouts/filters, detail page as organizer/attendee, registration sheet flow, dark mode) was **not performed** in this session — no browser tooling available. Build + lint verification only.

---

## Pending

None — Phase 1 (tasks 1-10) complete. Browser-based QA per the plan's "Verification" checklist is still recommended before merging.

---

## Known breakages (intentional, mid-refactor)

- `registration-details/registration-details.component.html` — the `[vertical]="true"` binding on `lib-registration-status` (removed in task 1) was already stripped as an interim fix so the build passes; the **full booking-card restyle** still happens in task 8.

---

## Phase 2-4 (future sessions, not detailed here)

- **Phase 2** — Create/Edit event sheets (sectioned bottom sheets, category picker, cover upload, hashtag input).
- **Phase 3** — Addresses page + Profile screen restyle.
- **Phase 4** — Default-address backend stub (if needed), notifications restyle, full `.agenda` calendar rebuild, dark-mode QA pass, `event-board-map` `.mapview` venue-grouped sidebar (deferred from task 6).
