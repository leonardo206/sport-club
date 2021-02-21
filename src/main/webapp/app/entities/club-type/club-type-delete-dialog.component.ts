import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClubType } from 'app/shared/model/club-type.model';
import { ClubTypeService } from './club-type.service';

@Component({
  templateUrl: './club-type-delete-dialog.component.html',
})
export class ClubTypeDeleteDialogComponent {
  clubType?: IClubType;

  constructor(protected clubTypeService: ClubTypeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clubTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('clubTypeListModification');
      this.activeModal.close();
    });
  }
}
