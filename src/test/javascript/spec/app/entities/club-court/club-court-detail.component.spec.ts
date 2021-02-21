import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SportClubTestModule } from '../../../test.module';
import { ClubCourtDetailComponent } from 'app/entities/club-court/club-court-detail.component';
import { ClubCourt } from 'app/shared/model/club-court.model';

describe('Component Tests', () => {
  describe('ClubCourt Management Detail Component', () => {
    let comp: ClubCourtDetailComponent;
    let fixture: ComponentFixture<ClubCourtDetailComponent>;
    const route = ({ data: of({ clubCourt: new ClubCourt(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SportClubTestModule],
        declarations: [ClubCourtDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ClubCourtDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClubCourtDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load clubCourt on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.clubCourt).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
