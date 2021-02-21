import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SportClubTestModule } from '../../../test.module';
import { ClubTypeUpdateComponent } from 'app/entities/club-type/club-type-update.component';
import { ClubTypeService } from 'app/entities/club-type/club-type.service';
import { ClubType } from 'app/shared/model/club-type.model';

describe('Component Tests', () => {
  describe('ClubType Management Update Component', () => {
    let comp: ClubTypeUpdateComponent;
    let fixture: ComponentFixture<ClubTypeUpdateComponent>;
    let service: ClubTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SportClubTestModule],
        declarations: [ClubTypeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ClubTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClubTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClubTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ClubType(123);
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
        const entity = new ClubType();
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
