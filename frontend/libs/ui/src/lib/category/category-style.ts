export interface CategoryStyle {
  hue: number
  icon: string
}

const CATEGORY_STYLES: Record<string, CategoryStyle> = {
  // 12 Kategorien — Farbtöne gleichmäßig über den Farbkreis verteilt (30° Abstand)
  familie: { hue: 0, icon: 'family_restroom' },
  family: { hue: 0, icon: 'family_restroom' },
  café: { hue: 30, icon: 'local_cafe' },
  kulinarik: { hue: 60, icon: 'restaurant' },
  food: { hue: 60, icon: 'restaurant' },
  touren: { hue: 90, icon: 'hiking' },
  sport: { hue: 120, icon: 'directions_run' },
  gemeinschaft: { hue: 150, icon: 'groups' },
  glaube: { hue: 180, icon: 'church' },
  bildung: { hue: 210, icon: 'school' },
  film: { hue: 240, icon: 'movie' },
  spiele: { hue: 270, icon: 'casino' },
  musik: { hue: 300, icon: 'music_note' },
  music: { hue: 300, icon: 'music_note' },
  kreativ: { hue: 330, icon: 'palette' },
  // Legacy-Aliase (andere Namensschemata)
  tour: { hue: 90, icon: 'explore' },
  führung: { hue: 90, icon: 'explore' },
  fuehrung: { hue: 90, icon: 'explore' },
  workshop: { hue: 330, icon: 'handyman' },
  contest: { hue: 0, icon: 'emoji_events' },
  wettbewerb: { hue: 0, icon: 'emoji_events' },
  talk: { hue: 210, icon: 'mic' },
  vortrag: { hue: 210, icon: 'mic' }
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
