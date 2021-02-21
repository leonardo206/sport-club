import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SportClubSharedModule } from 'app/shared/shared.module';
import { ClubManagerComponent } from './club-manager.component';
import { ClubManagerDetailComponent } from './club-manager-detail.component';
import { ClubManagerUpdateComponent } from './club-manager-update.component';
import { ClubManagerDeleteDialogComponent } from './club-manager-delete-dialog.component';
import { clubManagerRoute } from './club-manager.route';

@NgModule({
  imports: [SportClubSharedModule, RouterModule.forChild(clubManagerRoute)],
  declarations: [ClubManagerComponent, ClubManagerDetailComponent, ClubManagerUpdateComponent, ClubManagerDeleteDialogComponent],
  entryComponents: [ClubManagerDeleteDialogComponent],
})
export class SportClubClubManagerModule {}
