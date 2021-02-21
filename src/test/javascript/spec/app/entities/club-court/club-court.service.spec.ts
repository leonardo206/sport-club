import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ClubCourtService } from 'app/entities/club-court/club-court.service';
import { IClubCourt, ClubCourt } from 'app/shared/model/club-court.model';
import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

describe('Service Tests', () => {
  describe('ClubCourt Service', () => {
    let injector: TestBed;
    let service: ClubCourtService;
    let httpMock: HttpTestingController;
    let elemDefault: IClubCourt;
    let expectedResult: IClubCourt | IClubCourt[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ClubCourtService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new ClubCourt(0, 'AAAAAAA', 'AAAAAAA', ActiveInactiveStatus.Active);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ClubCourt', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ClubCourt()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ClubCourt', () => {
        const returnedFromService = Object.assign(
          {
            courtCode: 'BBBBBB',
            courtName: 'BBBBBB',
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

      it('should return a list of ClubCourt', () => {
        const returnedFromService = Object.assign(
          {
            courtCode: 'BBBBBB',
            courtName: 'BBBBBB',
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

      it('should delete a ClubCourt', () => {
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
