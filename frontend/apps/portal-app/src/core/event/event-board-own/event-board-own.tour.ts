import { TourDefinition } from '@open-event/shared'

export const eventBoardOwnTour: TourDefinition = {
  id: 'event-board-own',
  steps: [
    { title: 'tour.eventBoardOwn.welcome.title', description: 'tour.eventBoardOwn.welcome.description' },
    { element: '[data-tour="event-search"]', title: 'tour.eventBoardOwn.search.title', description: 'tour.eventBoardOwn.search.description' },
    { element: '[data-tour="event-create"]', title: 'tour.eventBoardOwn.create.title', description: 'tour.eventBoardOwn.create.description' },
    { element: '[data-tour="event-card"]', title: 'tour.eventBoardOwn.card.title', description: 'tour.eventBoardOwn.card.description' },
    { element: '[data-tour="event-card"] [data-tour="own-chips"]', title: 'tour.eventBoardOwn.chips.title', description: 'tour.eventBoardOwn.chips.description' },
    { element: '[data-tour="event-card"] [data-tour="own-text"]', title: 'tour.eventBoardOwn.text.title', description: 'tour.eventBoardOwn.text.description' },
    { element: '[data-tour="event-card"] [data-tour="own-stats"]', title: 'tour.eventBoardOwn.stats.title', description: 'tour.eventBoardOwn.stats.description' },
    { element: '[data-tour="event-card"] [data-tour="own-action-manage"]', title: 'tour.eventBoardOwn.manage.title', description: 'tour.eventBoardOwn.manage.description' },
    { element: '[data-tour="event-card"] [data-tour="own-action-edit"]', title: 'tour.eventBoardOwn.edit.title', description: 'tour.eventBoardOwn.edit.description' },
    { element: '[data-tour="event-card"] [data-tour="own-action-message"]', title: 'tour.eventBoardOwn.message.title', description: 'tour.eventBoardOwn.message.description' },
    { element: '[data-tour="event-card"] [data-tour="own-action-promote"]', title: 'tour.eventBoardOwn.promote.title', description: 'tour.eventBoardOwn.promote.description' },
    { element: '[data-tour="event-card"] [data-tour="own-action-cancel"]', title: 'tour.eventBoardOwn.cancel.title', description: 'tour.eventBoardOwn.cancel.description' }
  ]
}
