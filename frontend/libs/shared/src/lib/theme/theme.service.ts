import { effect, inject, Injectable, signal } from '@angular/core'
import { DOCUMENT } from '@angular/common'

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly doc = inject(DOCUMENT)
  readonly darkMode = signal(this.loadPreference())

  constructor() {
    effect(() => this.doc.documentElement.classList.toggle('dark', this.darkMode()))
  }

  toggle() {
    this.darkMode.update(v => !v)
    localStorage.setItem('darkMode', String(this.darkMode()))
  }

  private loadPreference(): boolean {
    const stored = localStorage.getItem('darkMode')
    if (stored !== null) return stored === 'true'
    return window.matchMedia?.('(prefers-color-scheme: dark)').matches ?? false
  }
}
