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

### 11. Phase 1 follow-up: card review ("Karten um die Events")
- **`event-details.component.html`** — removed the nested `mat-card appearance="outlined"` wrappers around the main-column content (`event-details-info` + `event-host-block` + guest-list/participants-stack, and `share-details`). These sections were already designed with their own `border-t border-outline-variant` dividers (one-card-with-sections pattern), so wrapping them in additional outlined cards inside the page-level `mat-card` produced a "card-in-card" double-border/background look. They now sit directly on the page-level card's surface, separated by their existing dividers. The aside column (booking card + location card) keeps its `appearance="outlined"` cards — those are genuinely standalone sidebar panels.
- **`share-details.component.html`** — added `border-t border-outline-variant` to its title row (was previously the top of its own `mat-card`, now flows directly after `event-guest-list`/`event-participants-stack` in the flattened main column).
- **`event-card`/`event-row`** (board `.ecard`/`.erow`) — changed from default `mat-card` appearance (always-on shadow) to `appearance="outlined"` (flat by default), and added `cursor: pointer` + hover state (`border-color: var(--mat-sys-primary)` + elevation shadow) in their `.scss`, since these cards are fully clickable (`routerLink`) but previously had no hover/clickable affordance.
- Verified via `npx nx build portal-app --configuration=development` (success) and `npx nx lint portal-app` (same 3 pre-existing issues as before, no new ones).
- **Not done / blocked**: full visual/browser QA of these changes — design source files (`/tmp/design-fetch/...`) were no longer available (ephemeral `/tmp`), and the full stack (Postgres + Keycloak) isn't running in this environment, so changes are code-reviewed but not pixel-verified.

---

### 12. Phase 2: Create/Edit event form

Full approved spec: `/home/oli.e/.claude/plans/fetch-this-design-file-linear-clover.md` (Phase 2). Reworked
`/event/create`, `/event/edit/:id`, `/event/copy/:id` (all share `lib-event-change`) into a single sectioned form
matching the design's `CreateEventSheet`/`EditEventSheet`. **Backend untouched.**

- **`libs/core/src/lib/address/address.api.ts`** — added optional `createAddress?(request: AddressChangeRequest):
  Observable<Address>` to `AddressReadAPI`, implemented by the portal-app pages (`event-create`/`event-edit`/`event-copy`)
  via their already-injected `AddressService.createAddress`.
- NEW `category/category-picker/` (`lib-category-picker`) — `.catopt` pill-grid, multi-select, `getCategoryStyle()`
  hue/icon, OKLCH selected state with dark-mode override.
- NEW `stepper-input/` (`lib-stepper-input`) — `.stepper` pill with `−`/value/`+` `matIconButton`s, `step`/`min` inputs.
- **Dropped stepper mode**: `EventChangeComponent`'s `mode` input removed, always renders `EventChangeSingleComponent`.
  Deleted unused `event-change-stepper/` and `event-change-upload/` (+ barrel exports) — cover-image editing already
  lives on the event-details hero from Phase 1.
- **`event-change.component.ts`** — un-hid `shortText` (removed from `hiddenFields`); added `submitLabel` input
  (default `action.submit`); on submit, if the location form is in "new address" mode with `saveAddress` checked, fires
  `addressReadAPI().createAddress?.(...)` and toasts via `address.message.saved`/`address.message.error`.
- **`event-change-single.component.ts/.html`** — restyled into 3 `<section>`s (`event.step.event/location/registration`
  headers, `.cesec__h`-style `border-b`), `submitLabel` input passed through; footer `matButton="text"` Cancel /
  `matButton="filled" color="accent"` Submit.
- **`event-change-general.component.html`** — start/end date and start/end time rows reflowed into
  `grid grid-cols-2 gap-3` (`.cefields2`). `shortText` field now visible (label/hint i18n keys added).
- **`event-change-location.component.ts/.html`** — added `addressMode` (`'saved'|'new'`) and `saveAddress` (bool)
  controls to `fg`. When saved addresses exist: `mat-button-toggle-group` (`event.form.address.saved/new`) +
  `mat-radio-group` of address cards (selecting one calls existing field-fill logic). "New" mode shows the manual
  street/nr/zip/city/country/additionalInfo fields (2-col grid pairs) plus a `.cecheck`-style `saveAddress` checkbox
  (`event.form.address.save/saveHint`). All form-bound elements stay in the DOM (`[class.hidden]`, never `@if`) to avoid
  Angular's `FormControlName`-removal-on-destroy. For edit/copy, `addressMode` defaults to `'new'` and `saveAddress` to
  `false` (don't silently add the event's existing location to the address book on every save).
- **`event-change-registration.component.ts/.html`** — removed dead `get ticketsEnabled()` getter (referenced
  non-existent `this.form`); `maxGuestAmount` numeric field replaced by `lib-stepper-input` in a `.cecheck`-style row,
  `min` = `max(1, sum of non-waitlisted info()?.registration?.participants[].size)` (`1` when no registration data, e.g.
  create); `lib-chip-select-pane` categories replaced by `lib-category-picker` (`allCategories` now exposes `Category[]`
  directly, `toggleCategory(id)` mutates the `categories` FormControl); `shared` checkbox box restyled to
  `bg-surface-container rounded-xl p-4`.
- **Portal pages** (`event-create`/`event-edit`/`event-copy`) — each now implements/exposes `createAddress`; output
  wrapped in `mat-card appearance="outlined" class="m-0 sm:!m-3 overflow-hidden"` (toolbar stays outside, per Phase 1
  pattern). `event-create` drops the `mat-slide-toggle` stepper/single mode switch, passes
  `submitLabel="event.action.publish"`. `event-edit` passes `submitLabel="event.action.saveChanges"` (new key).
  `event-copy` keeps the default `submitLabel`.
- **i18n** — added `event.action.saveChanges`, `event.form.shortText`, `event.form.hint.shortText`,
  `event.form.address.{saved,new,save,saveHint}` (converting `event.form.address` from a leaf string — only used by the
  now-removed `mat-select` dropdown — into an object), `address.message.{saved,error}` (DE+EN).
- Verified via `npx nx lint ui` / `npx nx lint portal-app` (same pre-existing issues only, none new — confirmed by diffing
  against `b5c179f`) and `npx nx build portal-app --configuration=development` (success, same pre-existing Sass
  deprecation warning only). No `libs/ui` → `libs/portal` import introduced.
- **Not done / blocked**: browser QA of the new sectioned form, saved/new address toggle, category pill-grid and
  capacity stepper — no running stack (Postgres + Keycloak) in this environment.

---

### 13. Events board polish — agenda calendar, harmonious filter, header reorder

Full approved spec: `/home/oli.e/.claude/plans/fetch-this-design-file-linear-clover.md` (Events Board polish).
Follow-up after Phase 2 review of the live board. **Backend untouched.**

- **`event-board-calendar/`** — FullCalendar removed entirely (`@fullcalendar/angular`/`core`/`daygrid` uninstalled
  from `package.json`; only consumer was this component). `event-board-calendar.component.scss` deleted. New
  implementation groups `EventBoardService.entries()` by date (`entry.start.substring(0, 10)`) into a `groups`
  computed signal and renders the design's `.agenda` concept: a date header (day number, weekday/month, `N× Event`
  count badge, divider rule) per day, followed by the existing `<portal-event-row>` (`mat-card`-based) for each
  entry — fulfils both "kalender komplett rauswerfen" and "die card ist selbst gebasteltes statt der material card"
  (the FullCalendar day-grid/event chips were the only non-Material "cards" on the board). Same empty state as
  `event-board-list`. This also completes the `.agenda` calendar rebuild previously deferred to Phase 4.
- **`event-board-filter.component.html/.ts`** — removed the `mat-accordion`/`mat-expansion-panel` "Erweitert" section.
  Its contents (date-range picker + history/own/participating toggle buttons) now render directly as a labeled group
  (same `flex flex-col gap-2` + label pattern as the "when"/"category" groups, using the existing
  `event.filter.advanced` key as the group heading), right before the reset button — one flat, harmonious filter
  panel. Removed the now-unused `MatExpansionModule` import.
- **`event-board.component.html`** — desktop header reordered into 3 flex zones: left (`flex-1`) = search +
  result-count, center = "Veranstaltung erstellen" button, right (`flex-1 justify-end`) = mobile filter icon button +
  the cards/rows/calendar/map `mat-button-toggle-group`. Same elements as before, just regrouped — degrades cleanly on
  mobile too.
- Verified via `npx nx lint portal-app` (same 3 pre-existing issues only, none new) and
  `npx nx build portal-app --configuration=development` (success, same pre-existing Sass deprecation warning only).
- **Not done / blocked**: browser QA of the new agenda view, flattened filter panel, and reordered header — no running
  stack in this environment.

---

### 14. Events board polish round 2 — flat filter, "empty" registration pill, attendee buttons

Follow-up after the user reviewed the live board (dev server + full stack running). **Backend untouched.**

- **`event-board-filter.component.html`** — removed the "Erweitert" grouping entirely (point: "warum erweitert ..
  es gibt einfach Filter und gut"). The "when" preset pills (any/today/weekend/next week) and the date-range picker
  — previously split between the top of the card and the now-removed "Erweitert" section — are now ONE group under
  the existing `event.filter.whenLabel` ("Zeitraum"/"Period") heading, which already fit both concepts. The
  history/own/participating toggle buttons now render as a plain unlabeled row (no special "advanced" tier) right
  before the reset button — one flat, harmonious filter list. Removed the now-unused `event.filter.advanced` key
  from `de.json`/`en.json`.
- **`event-card`/`event-row`** — added `hasRegistration = computed(() => entry().maxGuestAmount > 0)` and wrapped
  `<lib-registration-status>` in `@if (hasRegistration())`. Root cause of "die card ist kompletter Blödsinn": the
  backend defaults `maxGuestAmount`/`remainingSpace` to `0` for events with no `Registration` configured
  (`EventSearchEntryData.kt:61-65`), so every such event showed a misleading "0/0 · 0 Plätze frei" status pill —
  i.e. nearly every card on the board looked identical/broken regardless of the actual event. Now cards for
  events without a registration/capacity system simply omit the status pill.
- **`event-details.component.ts`** — `userParticipant` email match against the Keycloak principal is now
  case-insensitive. `registration-details`/`event-bookbar` already implement the "Teilnahme ändern"/"Absagen"
  buttons for attendees (gated on `userParticipant()`); an exact-case email mismatch was the most likely reason a
  registered attendee never matched and only ever saw the "Teilnehmen" button.
- Verified via `npx nx lint portal-app` (same 3 pre-existing issues only) and
  `npx nx build portal-app --configuration=development` (success, same pre-existing Sass deprecation warning only).
- **Not done / blocked**: live browser confirmation — headless Firefox crashes in this sandbox (XPIProvider abort,
  no working profile found), no chromium/playwright installed, and a portal-app session needs a Keycloak login this
  session has no credentials for. The registration-status fix and the email-match fix are both best-effort
  diagnoses pending user confirmation against the live app (dev server on :4200, backend on :8080 are running).

---

### 15. Events board polish round 3 — "Vergangenheit anzeigen" joins "Zeitraum", real root cause of "die card ist kompletter Blödsinn"

- **`event-card.component.ts`/`event-row.component.ts`** — **root cause found**: both templates use
  `<mat-card appearance="outlined" ...>`, but `MatCard` was never added to the standalone component's `imports`
  array (unlike `event-board-filter.component.ts`, which imports it correctly). Angular therefore rendered
  `<mat-card>` as an unrecognized element — none of Angular Material's `mat-mdc-card`/`mat-mdc-card-outlined` host
  classes were applied, so the card got no `border-radius`, no border/background/elevation in its resting state.
  Only the hand-written `.ecard:hover`/`.erow:hover` rules (`border-color` + `box-shadow`) ever made the card
  visible — exactly "die ecken sind nicht rund und die card ist nur als hover sichtbar". Fixed by importing
  `MatCard` in both components; the existing `.mat-mdc-card-outlined` styles (already bundled, since the filter
  panel uses `MatCard` on the same page) now apply normally. The previous round's `hasRegistration` fix was correct
  but addressed a separate, secondary issue.
- **`event-board-filter.component.html`** — "Vergangenheit anzeigen" moved from the unlabeled own/participating row
  into the "Zeitraum" pill row, alongside the when-presets (any/today/weekend/next week), as its own toggle pill
  (`service.toggleShowHistory()`).
- **`event-board.service.ts`** — `handlePreselectionChanged`: selecting "Jederzeit" (`value === 'any'`) now also
  calls `includeHistory.set(false)`, turning "Vergangenheit anzeigen" off.
- **`event-board-filter.component.ts`** — `isWhenActive('any')` now additionally requires `!service.showHistory()`,
  so the "Jederzeit" pill is shown as inactive while "Vergangenheit anzeigen" is active. Together with the service
  change, "Jederzeit" and "Vergangenheit anzeigen" are mutually exclusive in both directions, as requested ("wenn
  vergangenheit anzeigen, dann nicht 'jederzeit'").
- Verified via `npx nx lint portal-app` (same 3 pre-existing issues only) and
  `npx nx build portal-app --configuration=development` (success, same pre-existing Sass deprecation warning only);
  confirmed `.mat-mdc-card-outlined` rules are present in the bundled output (`chunk-Z5SOKIBA.js`).
- **Not done / blocked**: live browser confirmation — same sandbox limitation as round 2 (no working headless
  browser, no Keycloak credentials).

---

### 16. Events board polish round 4 — icon sizing on cards, organizer-as-participant buttons

- **`event-card.component.html`/`event-row.component.html`** — replaced the `!w-X !h-X !text-Y` icon-size overrides
  with `<mat-icon inline>`, following the established pattern from `search.component.html` (icon scales via
  inherited `font-size` instead of fixed pixel width/height). The media icon's size now comes from `text-5xl` on
  the surrounding `.ecard__media` container; the date/place icons inherit the row's `text-sm`/`text-xs`.
- **`registration-details.component.html`** — root cause for "Teilnahme ändern/Absagen-Button fehlt immer noch":
  the template used `@if (canEdit()) {...} @else {...}`, so an organizer who is *also* registered as a participant
  in their own event only ever saw the organizer view (badge, capacity, edit-event/export) — the
  `userParticipant()`-gated "Angemeldet" badge + "Teilnahme ändern"/"Absagen" buttons were in the `@else` branch and
  never rendered for them. Restructured so the organizer block and the participant block are independent
  conditions — an organizer who is also a participant now sees both. Also converted its two `!w-...!h-...!text-...`
  icons (`shield`, `check`) to `<mat-icon inline>`.
- **`event-bookbar.component.ts`/`.html`** (mobile bottom bar) — added a missing `edit` output and "Teilnahme
  ändern" button next to "Absagen" for registered participants, mirroring the desktop sidebar. Wired
  `(edit)="editSelf()"` in `event-details.component.html`.
- Verified via `npx nx lint portal-app` (same 3 pre-existing issues only) and
  `npx nx build portal-app --configuration=development` (success, same pre-existing Sass deprecation warning only).
- **Not done / blocked**: live browser confirmation — same sandbox limitation as previous rounds.

---

### 17. Events board polish round 5 — self-review: remaining icon-size overrides, mat-card import audit

Self-driven review pass (no new user feedback) across `event-board*`/`event-details*`/`registration/*`, continuing
rounds 3-4's `<mat-icon inline>` and `MatCard` import fixes consistently.

- **`mat-card` import audit** — checked every component rendering `<mat-card>` in the event/registration areas
  (`event-details`, `event-copy`, `event-edit`, `event-board-filter`, `event-board-map`, `event-card`, `event-row`,
  `registration-moderation`); all already import `MatCard` correctly (the two missing cases were fixed in round 3).
  No forbidden `mat-card-header`/`-content`/`-actions`/`-title`/`-subtitle`/`-footer` sub-parts found anywhere.
- **Remaining `!w-X !h-X !text-Y` icon overrides converted to `<mat-icon inline>`** (ancestor already carries the
  matching `text-*` size, same pattern as rounds 3-4):
  - `event-board-list.component.html` / `event-board-calendar.component.html` — empty-state `search_off` icon;
    added `text-5xl` to the wrapping `flex flex-col` container (sibling spans keep their own `text-lg`/`text-sm`).
  - `event-board.component.html` — the 3 active-filter-chip `close` icons (when/category/availability), each inside
    a `text-xs` button.
  - `event-board-map-popup.component.html` — `schedule`/`person`/`pin_drop` meta-row icons, inside a `text-xs`
    ancestor.
  - `event-details-banner.component.html` — hero-placeholder category icon; added `text-6xl` to the `.hero-placeholder`
    flex container.
  - `event-guest-list.component.html` — the "nur für Veranstalter sichtbar" `shield` badge (identical pattern to the
    one already fixed in `registration-details` round 4) and the `mail`/`phone` contact-link icons.
  - `event-participants-stack.component.html` — the "Angemeldet" `check` badge (same pattern as
    `registration-details`/`event-participants-stack`'s sibling badge fixed in round 4).
- **Deliberately left untouched**: `registration-participate-sheet.component.html`'s success-circle `check` icon
  (`!text-4xl !w-9 !h-9` inside a `w-16 h-16` circle, `grid place-items-center`) — a fixed-icon-inside-larger-circle
  pattern that doesn't map cleanly onto `inline`'s inherit-from-ancestor model without risking a size regression;
  and `registration-moderation.component.html` (admin-only registration table) — already flagged out of scope in
  task 10, uses a different inline-CSS-variable styling approach that needs its own pass, not just icon sizing.
  Also confirmed no hardcoded hex colors remain anywhere in `event/`/`registration/`.
- Verified via `npx nx lint portal-app` (same 3 pre-existing issues only) and
  `npx nx build portal-app --configuration=development` (success, same pre-existing Sass deprecation warning only).
- **Not done / blocked**: live browser confirmation — same sandbox limitation as previous rounds.

---

### 18. Events board polish round 6 — self-review: legacy Material button syntax

Continuation of round 5's self-review, this time covering the third item ("Buttons") of the original review request.
Pure syntax migration to Angular Material 21's directive form (`matButton="filled"/"outlined"`, `matIconButton`) —
same visual styling, no behavior change, `MatButton`/`MatIconButton` were already imported everywhere.

- **`event-board.component.html`** — mobile filter-icon button: `mat-icon-button` → `matIconButton`.
- **`event-delete-dialog.component.html`** — both confirm-dialog buttons: `mat-button mat-stroked-button` →
  `matButton="outlined"`.
- **`registration-cancel-dialog.component.html`**, **`registration-edit-dialog.component.html`**,
  **`registration-participate-manual-dialog.component.html`** — submit/cancel buttons: `mat-flat-button` →
  `matButton="filled"`.
- These 4 dialogs are the only remaining `MatDialog`-based components in the event/registration area (kept as-is per
  task 9); only their button directive syntax was touched, not their structure/styling.
- Verified via `npx nx lint portal-app` (same 3 pre-existing issues only) and
  `npx nx build portal-app --configuration=development` (success, same pre-existing Sass deprecation warning only).
- **Not done / blocked**: live browser confirmation — same sandbox limitation as previous rounds.

---

### 19. Events board polish round 7 — event-details page card appearance consistency

- **`event-details.component.html`** — top-level page-wrapper `<mat-card>` now has `appearance="outlined"`, matching
  the `event-edit`/`event-copy` page-wrapper cards (`mat-card appearance="outlined" class="m-0 sm:!m-3
  overflow-hidden"`, the "Phase 1 pattern" referenced in task 12). Previously the only full-page wrapper card without
  `appearance="outlined"` (default elevated/shadow style instead of flat/bordered) — now all three full-page wrapper
  cards (`event-details`, `event-edit`, `event-copy`) render consistently.
- Verified via `npx nx build portal-app --configuration=development` (success, same pre-existing Sass deprecation
  warning only).
- **Not done / blocked**: live browser confirmation — same sandbox limitation as previous rounds.

---

### 20. Phase 3 — Addresses + Profile restyle

- **`address.component.{html,ts,scss}`** — replaced the `<mat-table>` + colored-header-bar layout with the "Phase 1
  pattern" page-wrapper (`<mat-card appearance="outlined" class="m-0 sm:!m-3 overflow-hidden">`), a title/subtitle
  header (`address.title`/`address.subtitle`) with Import/Add buttons, an `event-board-list`-style empty state
  (`address.empty.title`/`.subtitle`), and a responsive `grid-cols-1 sm:grid-cols-2 lg:grid-cols-3` card grid — each
  `Address` rendered as an outlined card with a location icon badge, street/streetNumber/zip/city/additionalInfo, and
  a `border-t border-outline-variant` edit/delete `matIconButton` action row. `<mat-paginator>` kept below the grid.
  Removed unused `MatTableModule`/`MatDivider`/`displayedColumns` and emptied the dead `mat-card.address` SCSS rule.
  No "default address" feature — the `Address` model has no such field; flagged as possible future backend work.
- **`address-change-dialog.component.html`** / **`address-delete-dialog.component.html`** — `mat-flat-button` /
  `mat-button mat-stroked-button` → `matButton="filled"`/`matButton="outlined"`, same pairing as the round-6
  `event-delete-dialog` transform.
- **`libs/ui/src/lib/address/address-change/address-change.component.html`** — replaced
  `flex flex-row gap-3` + inline `style="flex: 0 1 100px"`/`style="flex: 0 1 200px"` with Tailwind grids
  (`grid grid-cols-[1fr_6rem] gap-3` for street/streetNumber, `grid grid-cols-[6.5rem_1fr] gap-3` for zip/city),
  removed the invalid Bootstrap class `align-items-stretch` and collapsed the nested wrapper divs. No field/logic
  changes.
- **`account-profile.component.html`** — `<mat-card>` → `<mat-card appearance="outlined" class="h-full">`; replaced
  the colored icon header bar with a plain title/subtitle header (`profile.title`/`profile.subtitle`, new
  `profile.subtitle` key) and a `border-t border-outline-variant p-4` avatar hero (`<lib-avatar [name]="firstName +
  ' ' + lastName" size="lg">` + name/email, mirroring `event-host-block.component.html`), added `AvatarComponent`
  import. Edit mode now uses `<mat-label>` inside `<mat-form-field appearance="outline" class="dense-1"
  subscriptSizing="dynamic">` for the remaining 8 fields + language select, in a `grid grid-cols-1 sm:grid-cols-2
  gap-3`; view mode renders the remaining 6 properties + language in a `grid grid-cols-1 sm:grid-cols-2 gap-x-6
  gap-y-2`. Dropped the redundant hardcoded email/firstName/lastName rows (now covered by the hero). No `.ts` logic
  changes.
- **`account-preferences.component.html`** — `<mat-card class="h-full">` → `<mat-card appearance="outlined"
  class="h-full">`; replaced the colored icon header bar + `<mat-divider>` with a plain `profile.title`-style header
  and a `border-t border-outline-variant p-4` content section; removed the now-unused `MatDivider` import.
- **Dead-code cleanup** — deleted `apps/portal-app/src/core/account/account-address/` (broken import to a
  non-existent `AddressBoardComponent`, zero references) and `apps/portal-app/src/core/address/address-change/`
  (unreferenced placeholder stub `<p>address-change works!</p>` — the real form is `libs/ui/src/lib/address/address-
  change/`, only tidied per above). `account-activity/` remains orphaned but out of scope.
- **i18n** — added `address.subtitle`, restructured `address.empty` into `address.empty.title`/`.subtitle`, and added
  `profile.subtitle` in both `de.json`/`en.json`.
- **Bugfix (reported during this phase)** — `event-row.component.html` (used by both the board's "rows" layout and
  the agenda calendar view) had `<mat-card appearance="outlined" class="erow flex items-center gap-3 p-3">`. Angular
  Material's `.mat-mdc-card` ships an unlayered `flex-direction: column` rule that overrides Tailwind's layered
  `flex`/`items-center` utilities, collapsing the row onto a column on desktop. Fixed by adding `!flex-row`
  (`class="erow flex !flex-row items-center gap-3 p-3"`) — the `!important` flag is required for a Tailwind utility
  to win against Angular Material's unlayered component styles.
- Verified via `npx nx lint portal-app` (same 3 pre-existing issues only) and
  `npx nx build portal-app --configuration=development` (success, same pre-existing Sass deprecation warning only).
- **Not done / blocked**: live browser confirmation — same sandbox limitation as previous rounds.

---

## Pending

Browser-based QA per the plan's "Verification" checklist (board layouts incl. the new card hover/outline styling and
the new agenda calendar view, the flattened filter panel and reordered header, event-detail page with the flattened
main column, and the Phase 2 create/edit event form) is still recommended before merging.

---

## Known breakages (intentional, mid-refactor)

- `registration-details/registration-details.component.html` — the `[vertical]="true"` binding on `lib-registration-status` (removed in task 1) was already stripped as an interim fix so the build passes; the **full booking-card restyle** still happens in task 8.

---

## Phase 3-4 (future sessions, not detailed here)

- **Phase 3** — Addresses page + Profile screen restyle. Done, see task 20.
- **Phase 4** — Default-address backend stub (if needed), notifications restyle, dark-mode QA pass, `event-board-map`
  `.mapview` venue-grouped sidebar (deferred from task 6).
