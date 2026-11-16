import { TourDefinition } from '@open-event/shared'

export const eventBoardTour: TourDefinition = {
  id: 'event-board',
  steps: [
    { title: 'tour.eventBoard.welcome.title', description: 'tour.eventBoard.welcome.description' },
    { element: '[data-tour="nav:/event"]', title: 'tour.eventBoard.navDiscover.title', description: 'tour.eventBoard.navDiscover.description' },
    { element: '[data-tour="nav:/event/saved"]', title: 'tour.eventBoard.navSaved.title', description: 'tour.eventBoard.navSaved.description' },
    { element: '[data-tour="nav:/event/regs"]', title: 'tour.eventBoard.navRegs.title', description: 'tour.eventBoard.navRegs.description' },
    { element: '[data-tour="nav:/event/own"]', title: 'tour.eventBoard.navOwn.title', description: 'tour.eventBoard.navOwn.description' },
    { element: '[data-tour="event-search"]', title: 'tour.eventBoard.search.title', description: 'tour.eventBoard.search.description' },
    { element: '[data-tour="event-filter"]', title: 'tour.eventBoard.filter.title', description: 'tour.eventBoard.filter.description' },
    { element: '[data-tour="event-filter-reset"]', title: 'tour.eventBoard.filterReset.title', description: 'tour.eventBoard.filterReset.description' },
    { element: '[data-tour="event-layout"]', title: 'tour.eventBoard.layout.title', description: 'tour.eventBoard.layout.description' },
    { element: '[data-tour="event-card"]', title: 'tour.eventBoard.details.title', description: 'tour.eventBoard.details.description' },
    { element: '[data-tour="event-create"]', title: 'tour.eventBoard.create.title', description: 'tour.eventBoard.create.description' }
  ]
}
