import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SportClubTestModule } from '../../../test.module';
import { ClubCourtTypeDetailComponent } from 'app/entities/club-court-type/club-court-type-detail.component';
import { ClubCourtType } from 'app/shared/model/club-court-type.model';

describe('Component Tests', () => {
  describe('ClubCourtType Management Detail Component', () => {
    let comp: ClubCourtTypeDetailComponent;
    let fixture: ComponentFixture<ClubCourtTypeDetailComponent>;
    const route = ({ data: of({ clubCourtType: new ClubCourtType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SportClubTestModule],
        declarations: [ClubCourtTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ClubCourtTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClubCourtTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load clubCourtType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.clubCourtType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
