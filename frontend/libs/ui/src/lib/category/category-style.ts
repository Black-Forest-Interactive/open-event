export interface CategoryStyle {
  hue: number
  icon: string
}

const CATEGORY_STYLES: Record<string, CategoryStyle> = {
  tour: { hue: 205, icon: 'explore' },
  führung: { hue: 205, icon: 'explore' },
  fuehrung: { hue: 205, icon: 'explore' },
  workshop: { hue: 268, icon: 'handyman' },
  family: { hue: 30, icon: 'family_restroom' },
  familie: { hue: 30, icon: 'family_restroom' },
  contest: { hue: 350, icon: 'emoji_events' },
  wettbewerb: { hue: 350, icon: 'emoji_events' },
  food: { hue: 45, icon: 'restaurant' },
  kulinarik: { hue: 45, icon: 'restaurant' },
  music: { hue: 305, icon: 'music_note' },
  musik: { hue: 305, icon: 'music_note' },
  sport: { hue: 165, icon: 'directions_run' },
  talk: { hue: 232, icon: 'mic' },
  vortrag: { hue: 232, icon: 'mic' }
}

function hashString(value: string): number {
  let hash = 0
  for (let i = 0; i < value.length; i++) {
    hash = (hash << 5) - hash + value.charCodeAt(i)
    hash |= 0
  }
  return Math.abs(hash)
}

export function getCategoryStyle(name: string): CategoryStyle {
  const key = name.trim().toLowerCase()
  return CATEGORY_STYLES[key] ?? { hue: hashString(key) % 360, icon: 'label' }
}
