import { TourDefinition } from '@open-event/shared'

const eventCreateTour: TourDefinition = {
  id: 'event-create',
  steps: [
    { title: 'tour.eventCreate.welcome.title', description: 'tour.eventCreate.welcome.description' },
    { element: '.wizard-stepper .mat-horizontal-stepper-header-container', title: 'tour.eventCreate.steps.title', description: 'tour.eventCreate.steps.description' },
    { element: '[data-tour="create-title"]', title: 'tour.eventCreate.title.title', description: 'tour.eventCreate.title.description' },
    { element: '[data-tour="create-short-text"]', title: 'tour.eventCreate.shortText.title', description: 'tour.eventCreate.shortText.description' },
    { element: '[data-tour="create-long-text"]', title: 'tour.eventCreate.longText.title', description: 'tour.eventCreate.longText.description' },
    { element: '[data-tour="create-date"]', title: 'tour.eventCreate.date.title', description: 'tour.eventCreate.date.description' },
    { element: '[data-tour="create-time"]', title: 'tour.eventCreate.time.title', description: 'tour.eventCreate.time.description' },
    { element: '[data-tour="create-nav"]', title: 'tour.eventCreate.nav.title', description: 'tour.eventCreate.nav.description' }
  ]
}

const eventCreateAddressTour: TourDefinition = {
  id: 'event-create-address',
  steps: [
    { element: '[data-tour="create-address-mode"]', title: 'tour.eventCreateAddress.mode.title', description: 'tour.eventCreateAddress.mode.description' },
    { element: '[data-tour="create-address-saved"]', title: 'tour.eventCreateAddress.saved.title', description: 'tour.eventCreateAddress.saved.description' },
    { element: '[data-tour="create-address-new"]', title: 'tour.eventCreateAddress.new.title', description: 'tour.eventCreateAddress.new.description' }
  ]
}

const eventCreateMiscTour: TourDefinition = {
  id: 'event-create-misc',
  steps: [
    { element: '[data-tour="create-participants"]', title: 'tour.eventCreateMisc.participants.title', description: 'tour.eventCreateMisc.participants.description' },
    { element: '[data-tour="create-categories"]', title: 'tour.eventCreateMisc.categories.title', description: 'tour.eventCreateMisc.categories.description' },
    { element: '[data-tour="create-audiences"]', title: 'tour.eventCreateMisc.audiences.title', description: 'tour.eventCreateMisc.audiences.description' },
    { element: '[data-tour="create-tags"]', title: 'tour.eventCreateMisc.tags.title', description: 'tour.eventCreateMisc.tags.description' },
    { element: '[data-tour="create-share"]', title: 'tour.eventCreateMisc.share.title', description: 'tour.eventCreateMisc.share.description' }
  ]
}

const eventCreateSummaryTour: TourDefinition = {
  id: 'event-create-summary',
  steps: [
    { element: '[data-tour="create-summary"]', title: 'tour.eventCreateSummary.overview.title', description: 'tour.eventCreateSummary.overview.description' },
    { element: '[data-tour="create-save"]', title: 'tour.eventCreateSummary.save.title', description: 'tour.eventCreateSummary.save.description' }
  ]
}

// one tour per wizard step, indexed by the stepper's selectedIndex
export const eventCreateTours: TourDefinition[] = [eventCreateTour, eventCreateAddressTour, eventCreateMiscTour, eventCreateSummaryTour]
