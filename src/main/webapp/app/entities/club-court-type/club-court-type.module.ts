import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SportClubSharedModule } from 'app/shared/shared.module';
import { ClubCourtTypeComponent } from './club-court-type.component';
import { ClubCourtTypeDetailComponent } from './club-court-type-detail.component';
import { ClubCourtTypeUpdateComponent } from './club-court-type-update.component';
import { ClubCourtTypeDeleteDialogComponent } from './club-court-type-delete-dialog.component';
import { clubCourtTypeRoute } from './club-court-type.route';

@NgModule({
  imports: [SportClubSharedModule, RouterModule.forChild(clubCourtTypeRoute)],
  declarations: [ClubCourtTypeComponent, ClubCourtTypeDetailComponent, ClubCourtTypeUpdateComponent, ClubCourtTypeDeleteDialogComponent],
  entryComponents: [ClubCourtTypeDeleteDialogComponent],
})
export class SportClubClubCourtTypeModule {}
