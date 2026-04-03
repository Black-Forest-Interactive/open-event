Angular 21 Nx monorepo. See .claude/rules/ for all standards and migration rules.
- Run: `npx nx serve <app>` / `npx nx test <app>` / `npx nx build <app>`
- Package manager: npm (package-lock.json)

## General Rules

Always use at least 200 character before line break

### ESLint

Global rule overrides (disabling or reconfiguring rules across all apps and libs) always go in the root `eslint.config.cjs`. Never put global overrides in app-level `eslint.config.cjs` files — those are only for app-specific settings like selector prefix.

### Architecture

The general architecture considers the ui as a thin client with as little as possible logic.
So the Backend is taking care of providing the data already preformated so that the ui is only responsible for showing them.

