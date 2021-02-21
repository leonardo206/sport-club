import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ClubTypeService } from 'app/entities/club-type/club-type.service';
import { IClubType, ClubType } from 'app/shared/model/club-type.model';
import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

describe('Service Tests', () => {
  describe('ClubType Service', () => {
    let injector: TestBed;
    let service: ClubTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IClubType;
    let expectedResult: IClubType | IClubType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ClubTypeService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new ClubType(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', ActiveInactiveStatus.Active);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ClubType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ClubType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ClubType', () => {
        const returnedFromService = Object.assign(
          {
            clubTypeCode: 'BBBBBB',
            clubTypeName: 'BBBBBB',
            clubTypeDescription: 'BBBBBB',
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ClubType', () => {
        const returnedFromService = Object.assign(
          {
            clubTypeCode: 'BBBBBB',
            clubTypeName: 'BBBBBB',
            clubTypeDescription: 'BBBBBB',
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ClubType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
