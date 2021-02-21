import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClubManager } from 'app/shared/model/club-manager.model';
import { ClubManagerService } from './club-manager.service';

@Component({
  templateUrl: './club-manager-delete-dialog.component.html',
})
export class ClubManagerDeleteDialogComponent {
  clubManager?: IClubManager;

  constructor(
    protected clubManagerService: ClubManagerService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clubManagerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('clubManagerListModification');
      this.activeModal.close();
    });
  }
}
