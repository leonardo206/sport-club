import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SportClubTestModule } from '../../../test.module';
import { ClubTypeDetailComponent } from 'app/entities/club-type/club-type-detail.component';
import { ClubType } from 'app/shared/model/club-type.model';

describe('Component Tests', () => {
  describe('ClubType Management Detail Component', () => {
    let comp: ClubTypeDetailComponent;
    let fixture: ComponentFixture<ClubTypeDetailComponent>;
    const route = ({ data: of({ clubType: new ClubType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SportClubTestModule],
        declarations: [ClubTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ClubTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClubTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load clubType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.clubType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
