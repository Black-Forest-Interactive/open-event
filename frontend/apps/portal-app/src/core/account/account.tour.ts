import { TourDefinition } from '@open-event/shared'

export const accountTour: TourDefinition = {
  id: 'account',
  steps: [
    { title: 'tour.account.welcome.title', description: 'tour.account.welcome.description' },
    { element: '[data-tour="account-profile"]', title: 'tour.account.profile.title', description: 'tour.account.profile.description' },
    { element: '[data-tour="profile-edit"]', title: 'tour.account.edit.title', description: 'tour.account.edit.description' },
    { element: '[data-tour="account-preferences"]', title: 'tour.account.preferences.title', description: 'tour.account.preferences.description' }
  ]
}
