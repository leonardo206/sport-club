import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SportClubTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { ClubCourtTypeDeleteDialogComponent } from 'app/entities/club-court-type/club-court-type-delete-dialog.component';
import { ClubCourtTypeService } from 'app/entities/club-court-type/club-court-type.service';

describe('Component Tests', () => {
  describe('ClubCourtType Management Delete Component', () => {
    let comp: ClubCourtTypeDeleteDialogComponent;
    let fixture: ComponentFixture<ClubCourtTypeDeleteDialogComponent>;
    let service: ClubCourtTypeService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SportClubTestModule],
        declarations: [ClubCourtTypeDeleteDialogComponent],
      })
        .overrideTemplate(ClubCourtTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClubCourtTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClubCourtTypeService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
