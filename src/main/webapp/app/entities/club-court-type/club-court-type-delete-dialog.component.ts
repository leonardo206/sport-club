import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClubCourtType } from 'app/shared/model/club-court-type.model';
import { ClubCourtTypeService } from './club-court-type.service';

@Component({
  templateUrl: './club-court-type-delete-dialog.component.html',
})
export class ClubCourtTypeDeleteDialogComponent {
  clubCourtType?: IClubCourtType;

  constructor(
    protected clubCourtTypeService: ClubCourtTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clubCourtTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('clubCourtTypeListModification');
      this.activeModal.close();
    });
  }
}
