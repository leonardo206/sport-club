import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SportClubTestModule } from '../../../test.module';
import { ClubCourtTypeUpdateComponent } from 'app/entities/club-court-type/club-court-type-update.component';
import { ClubCourtTypeService } from 'app/entities/club-court-type/club-court-type.service';
import { ClubCourtType } from 'app/shared/model/club-court-type.model';

describe('Component Tests', () => {
  describe('ClubCourtType Management Update Component', () => {
    let comp: ClubCourtTypeUpdateComponent;
    let fixture: ComponentFixture<ClubCourtTypeUpdateComponent>;
    let service: ClubCourtTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SportClubTestModule],
        declarations: [ClubCourtTypeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ClubCourtTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClubCourtTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClubCourtTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ClubCourtType(123);
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
        const entity = new ClubCourtType();
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
