import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SportClubTestModule } from '../../../test.module';
import { ClubManagerUpdateComponent } from 'app/entities/club-manager/club-manager-update.component';
import { ClubManagerService } from 'app/entities/club-manager/club-manager.service';
import { ClubManager } from 'app/shared/model/club-manager.model';

describe('Component Tests', () => {
  describe('ClubManager Management Update Component', () => {
    let comp: ClubManagerUpdateComponent;
    let fixture: ComponentFixture<ClubManagerUpdateComponent>;
    let service: ClubManagerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SportClubTestModule],
        declarations: [ClubManagerUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ClubManagerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClubManagerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClubManagerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ClubManager(123);
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
        const entity = new ClubManager();
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
