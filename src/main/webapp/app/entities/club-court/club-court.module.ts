import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SportClubSharedModule } from 'app/shared/shared.module';
import { ClubCourtComponent } from './club-court.component';
import { ClubCourtDetailComponent } from './club-court-detail.component';
import { ClubCourtUpdateComponent } from './club-court-update.component';
import { ClubCourtDeleteDialogComponent } from './club-court-delete-dialog.component';
import { clubCourtRoute } from './club-court.route';

@NgModule({
  imports: [SportClubSharedModule, RouterModule.forChild(clubCourtRoute)],
  declarations: [ClubCourtComponent, ClubCourtDetailComponent, ClubCourtUpdateComponent, ClubCourtDeleteDialogComponent],
  entryComponents: [ClubCourtDeleteDialogComponent],
})
export class SportClubClubCourtModule {}
