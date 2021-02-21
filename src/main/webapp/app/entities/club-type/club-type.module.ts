import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SportClubSharedModule } from 'app/shared/shared.module';
import { ClubTypeComponent } from './club-type.component';
import { ClubTypeDetailComponent } from './club-type-detail.component';
import { ClubTypeUpdateComponent } from './club-type-update.component';
import { ClubTypeDeleteDialogComponent } from './club-type-delete-dialog.component';
import { clubTypeRoute } from './club-type.route';

@NgModule({
  imports: [SportClubSharedModule, RouterModule.forChild(clubTypeRoute)],
  declarations: [ClubTypeComponent, ClubTypeDetailComponent, ClubTypeUpdateComponent, ClubTypeDeleteDialogComponent],
  entryComponents: [ClubTypeDeleteDialogComponent],
})
export class SportClubClubTypeModule {}
