import { effect, inject, Injectable, Signal } from '@angular/core'
import { NavigationStart, Router } from '@angular/router'
import { filter } from 'rxjs'

/**
 * The app shell (`lib-app-layout`) keeps the document itself fixed (`h-screen overflow-hidden`) and scrolls
 * `<mat-sidenav-content>` instead, so `window`/`document` never scroll — this resolves the actual scrollable
 * element, falling back to `window` for pages rendered outside that shell (e.g. external-app).
 */
function getScrollElement(): HTMLElement | null {
  return document.querySelector('mat-sidenav-content')
}

function getScrollTop(): number {
  const el = getScrollElement()
  return el ? el.scrollTop : window.scrollY
}

function scrollToY(y: number): void {
  const el = getScrollElement()
  if (el) el.scrollTo({ top: y, left: 0, behavior: 'instant' })
  else window.scrollTo({ top: y, left: 0, behavior: 'instant' })
}

/**
 * Remembers the scroll position per route URL, captured right before the router leaves that URL.
 * Only browser back/forward (popstate) navigation is treated as a "return" worth restoring — a fresh,
 * user-triggered navigation to the same URL (e.g. clicking a nav tab again) always starts at the top.
 */
@Injectable({ providedIn: 'root' })
export class ScrollMemoryService {
  private router = inject(Router)
  private positions = new Map<string, number>()
  private lastTrigger: string | null = null

  constructor() {
    this.router.events.pipe(filter((e) => e instanceof NavigationStart)).subscribe((e) => {
      this.positions.set(this.router.url, getScrollTop())
      this.lastTrigger = e.navigationTrigger ?? null
    })
  }

  consume(url: string): number | undefined {
    if (this.lastTrigger !== 'popstate') return undefined
    const y = this.positions.get(url)
    this.positions.delete(url)
    return y
  }
}

/**
 * Restores the remembered scroll position for the current route once the list content behind `ready` is
 * actually rendered — call from a component constructor, e.g.:
 *
 * restoreScrollOnBack(computed(() => !this.reloading() && this.entries().length > 0))
 *
 * The target URL is resolved lazily, once `ready` fires, not at construction time: while a component is
 * being constructed during route activation, the router hasn't necessarily committed the new URL yet, so
 * reading `router.url` right away can still return the URL of the page being left.
 */
export function restoreScrollOnBack(ready: Signal<boolean>): void {
  const scrollMemory = inject(ScrollMemoryService)
  const router = inject(Router)
  let restored = false

  effect(() => {
    if (restored || !ready()) return
    restored = true
    const y = scrollMemory.consume(router.url)
    if (y == null) return
    requestAnimationFrame(() => requestAnimationFrame(() => scrollToY(y)))
  })
}
