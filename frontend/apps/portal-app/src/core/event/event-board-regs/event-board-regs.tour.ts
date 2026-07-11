import { TourDefinition } from '@open-event/shared'

export const eventBoardRegsTour: TourDefinition = {
  id: 'event-board-regs',
  steps: [
    { title: 'tour.eventBoardRegs.welcome.title', description: 'tour.eventBoardRegs.welcome.description' },
    { element: '[data-tour="event-search"]', title: 'tour.eventBoardRegs.search.title', description: 'tour.eventBoardRegs.search.description' },
    { element: '[data-tour="event-card"]', title: 'tour.eventBoardRegs.card.title', description: 'tour.eventBoardRegs.card.description' }
  ]
}
