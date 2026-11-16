/**
 * Tri-state filter: every key is either neutral (absent), "only these" (`in`) or "exclude" (`out`).
 * Shared by the events board and the finder, the resulting lists map onto the
 * `categories`/`excludedCategories` and `audiences`/`excludedAudiences` fields of the search request.
 */
export type TriState = 'in' | 'out'
export type TriFilter = Record<string, TriState>

export function triNext(state: TriState | undefined): TriState | undefined {
  if (state === 'in') return 'out'
  if (state === 'out') return undefined
  return 'in'
}

export function triToggle(filter: TriFilter, key: string): TriFilter {
  const result = { ...filter }
  const next = triNext(result[key])
  if (next) result[key] = next
  else delete result[key]
  return result
}

export function triRemove(filter: TriFilter, key: string): TriFilter {
  const result = { ...filter }
  delete result[key]
  return result
}

export function triIncluded(filter: TriFilter): string[] {
  return Object.keys(filter).filter((k) => filter[k] === 'in')
}

export function triExcluded(filter: TriFilter): string[] {
  return Object.keys(filter).filter((k) => filter[k] === 'out')
}

export function triCount(filter: TriFilter): number {
  return Object.keys(filter).length
}

export function triFromLists(included: string[] = [], excluded: string[] = []): TriFilter {
  const result: TriFilter = {}
  included.forEach((k) => (result[k] = 'in'))
  excluded.forEach((k) => (result[k] = 'out'))
  return result
}
