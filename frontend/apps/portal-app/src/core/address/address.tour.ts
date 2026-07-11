import { TourDefinition } from '@open-event/shared'

export const addressTour: TourDefinition = {
  id: 'address',
  steps: [
    { title: 'tour.address.welcome.title', description: 'tour.address.welcome.description' },
    { element: '[data-tour="address-add"]', title: 'tour.address.add.title', description: 'tour.address.add.description' },
    { element: '[data-tour="address-import"]', title: 'tour.address.import.title', description: 'tour.address.import.description' },
    { element: '[data-tour="address-card"]', title: 'tour.address.card.title', description: 'tour.address.card.description' },
    { element: '[data-tour="address-card"] [data-tour="address-default"]', title: 'tour.address.default.title', description: 'tour.address.default.description' },
    { element: '[data-tour="address-card"] [data-tour="address-actions"]', title: 'tour.address.actions.title', description: 'tour.address.actions.description' }
  ]
}
