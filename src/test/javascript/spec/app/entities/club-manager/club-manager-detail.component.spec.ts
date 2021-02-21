import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SportClubTestModule } from '../../../test.module';
import { ClubManagerDetailComponent } from 'app/entities/club-manager/club-manager-detail.component';
import { ClubManager } from 'app/shared/model/club-manager.model';

describe('Component Tests', () => {
  describe('ClubManager Management Detail Component', () => {
    let comp: ClubManagerDetailComponent;
    let fixture: ComponentFixture<ClubManagerDetailComponent>;
    const route = ({ data: of({ clubManager: new ClubManager(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SportClubTestModule],
        declarations: [ClubManagerDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ClubManagerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClubManagerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load clubManager on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.clubManager).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
