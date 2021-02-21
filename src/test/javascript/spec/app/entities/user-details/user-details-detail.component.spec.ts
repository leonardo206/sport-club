import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { SportClubTestModule } from '../../../test.module';
import { UserDetailsDetailComponent } from 'app/entities/user-details/user-details-detail.component';
import { UserDetails } from 'app/shared/model/user-details.model';

describe('Component Tests', () => {
  describe('UserDetails Management Detail Component', () => {
    let comp: UserDetailsDetailComponent;
    let fixture: ComponentFixture<UserDetailsDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ userDetails: new UserDetails(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SportClubTestModule],
        declarations: [UserDetailsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(UserDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserDetailsDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load userDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
