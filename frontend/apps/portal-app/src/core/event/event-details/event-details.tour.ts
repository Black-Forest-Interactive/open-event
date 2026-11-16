import { TourDefinition } from '@open-event/shared'

export const eventDetailsTour: TourDefinition = {
  id: 'event-details',
  steps: [
    { title: 'tour.eventDetails.welcome.title', description: 'tour.eventDetails.welcome.description' },
    { element: '[data-tour="details-info"]', title: 'tour.eventDetails.info.title', description: 'tour.eventDetails.info.description' },
    { element: '[data-tour="details-host"]', title: 'tour.eventDetails.host.title', description: 'tour.eventDetails.host.description' },
    { element: '[data-tour="details-registration"]', title: 'tour.eventDetails.registration.title', description: 'tour.eventDetails.registration.description' },
    { element: '[data-tour="details-location"]', title: 'tour.eventDetails.location.title', description: 'tour.eventDetails.location.description' },
    { element: '[data-tour="details-bookmark"]', title: 'tour.eventDetails.bookmark.title', description: 'tour.eventDetails.bookmark.description' },
    { element: '[data-tour="details-share"]', title: 'tour.eventDetails.share.title', description: 'tour.eventDetails.share.description' },
    { element: '[data-tour="details-guests"]', title: 'tour.eventDetails.guests.title', description: 'tour.eventDetails.guests.description' },
    { element: '[data-tour="details-share-settings"]', title: 'tour.eventDetails.shareSettings.title', description: 'tour.eventDetails.shareSettings.description' },
    { element: '[data-tour="details-edit"]', title: 'tour.eventDetails.edit.title', description: 'tour.eventDetails.edit.description' }
  ]
}
