import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { UserDetailsService } from 'app/entities/user-details/user-details.service';
import { IUserDetails, UserDetails } from 'app/shared/model/user-details.model';
import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

describe('Service Tests', () => {
  describe('UserDetails Service', () => {
    let injector: TestBed;
    let service: UserDetailsService;
    let httpMock: HttpTestingController;
    let elemDefault: IUserDetails;
    let expectedResult: IUserDetails | IUserDetails[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(UserDetailsService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new UserDetails(0, 'AAAAAAA', currentDate, 'image/png', 'AAAAAAA', 'AAAAAAA', ActiveInactiveStatus.Active);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a UserDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.create(new UserDetails()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a UserDetails', () => {
        const returnedFromService = Object.assign(
          {
            mobileNumber: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
            profilePic: 'BBBBBB',
            description: 'BBBBBB',
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of UserDetails', () => {
        const returnedFromService = Object.assign(
          {
            mobileNumber: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_TIME_FORMAT),
            profilePic: 'BBBBBB',
            description: 'BBBBBB',
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a UserDetails', () => {
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
