import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SportClubTestModule } from '../../../test.module';
import { ClubCourtUpdateComponent } from 'app/entities/club-court/club-court-update.component';
import { ClubCourtService } from 'app/entities/club-court/club-court.service';
import { ClubCourt } from 'app/shared/model/club-court.model';

describe('Component Tests', () => {
  describe('ClubCourt Management Update Component', () => {
    let comp: ClubCourtUpdateComponent;
    let fixture: ComponentFixture<ClubCourtUpdateComponent>;
    let service: ClubCourtService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SportClubTestModule],
        declarations: [ClubCourtUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ClubCourtUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClubCourtUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClubCourtService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ClubCourt(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ClubCourt();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
