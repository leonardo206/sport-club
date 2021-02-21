import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClubCourt } from 'app/shared/model/club-court.model';
import { ClubCourtService } from './club-court.service';

@Component({
  templateUrl: './club-court-delete-dialog.component.html',
})
export class ClubCourtDeleteDialogComponent {
  clubCourt?: IClubCourt;

  constructor(protected clubCourtService: ClubCourtService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clubCourtService.delete(id).subscribe(() => {
      this.eventManager.broadcast('clubCourtListModification');
      this.activeModal.close();
    });
  }
}
