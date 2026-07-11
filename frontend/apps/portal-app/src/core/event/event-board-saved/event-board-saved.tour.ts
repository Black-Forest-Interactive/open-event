import { TourDefinition } from '@open-event/shared'

export const eventBoardSavedTour: TourDefinition = {
  id: 'event-board-saved',
  steps: [
    { title: 'tour.eventBoardSaved.welcome.title', description: 'tour.eventBoardSaved.welcome.description' },
    { element: '[data-tour="event-search"]', title: 'tour.eventBoardSaved.search.title', description: 'tour.eventBoardSaved.search.description' },
    { element: '[data-tour="event-card"]', title: 'tour.eventBoardSaved.card.title', description: 'tour.eventBoardSaved.card.description' },
    { element: '[data-tour="event-card"] [data-tour="row-details"]', title: 'tour.eventBoardSaved.details.title', description: 'tour.eventBoardSaved.details.description' },
    { element: '[data-tour="event-card"] [data-tour="row-remove"]', title: 'tour.eventBoardSaved.remove.title', description: 'tour.eventBoardSaved.remove.description' }
  ]
}
