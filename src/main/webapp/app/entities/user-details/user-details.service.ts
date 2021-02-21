import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUserDetails } from 'app/shared/model/user-details.model';

type EntityResponseType = HttpResponse<IUserDetails>;
type EntityArrayResponseType = HttpResponse<IUserDetails[]>;

@Injectable({ providedIn: 'root' })
export class UserDetailsService {
  public resourceUrl = SERVER_API_URL + 'api/user-details';

  constructor(protected http: HttpClient) {}

  create(userDetails: IUserDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userDetails);
    return this.http
      .post<IUserDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(userDetails: IUserDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userDetails);
    return this.http
      .put<IUserDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUserDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(userDetails: IUserDetails): IUserDetails {
    const copy: IUserDetails = Object.assign({}, userDetails, {
      dateOfBirth: userDetails.dateOfBirth && userDetails.dateOfBirth.isValid() ? userDetails.dateOfBirth.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? moment(res.body.dateOfBirth) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((userDetails: IUserDetails) => {
        userDetails.dateOfBirth = userDetails.dateOfBirth ? moment(userDetails.dateOfBirth) : undefined;
      });
    }
    return res;
  }
}
