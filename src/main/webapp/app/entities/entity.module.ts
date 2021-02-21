import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'organization',
        loadChildren: () => import('./organization/organization.module').then(m => m.SportClubOrganizationModule),
      },
      {
        path: 'client',
        loadChildren: () => import('./client/client.module').then(m => m.SportClubClientModule),
      },
      {
        path: 'user-details',
        loadChildren: () => import('./user-details/user-details.module').then(m => m.SportClubUserDetailsModule),
      },
      {
        path: 'club-manager',
        loadChildren: () => import('./club-manager/club-manager.module').then(m => m.SportClubClubManagerModule),
      },
      {
        path: 'club-type',
        loadChildren: () => import('./club-type/club-type.module').then(m => m.SportClubClubTypeModule),
      },
      {
        path: 'club',
        loadChildren: () => import('./club/club.module').then(m => m.SportClubClubModule),
      },
      {
        path: 'club-court-type',
        loadChildren: () => import('./club-court-type/club-court-type.module').then(m => m.SportClubClubCourtTypeModule),
      },
      {
        path: 'club-court',
        loadChildren: () => import('./club-court/club-court.module').then(m => m.SportClubClubCourtModule),
      },
      {
        path: 'booking',
        loadChildren: () => import('./booking/booking.module').then(m => m.SportClubBookingModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class SportClubEntityModule {}
