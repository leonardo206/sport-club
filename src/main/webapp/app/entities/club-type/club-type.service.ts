import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IClubType } from 'app/shared/model/club-type.model';

type EntityResponseType = HttpResponse<IClubType>;
type EntityArrayResponseType = HttpResponse<IClubType[]>;

@Injectable({ providedIn: 'root' })
export class ClubTypeService {
  public resourceUrl = SERVER_API_URL + 'api/club-types';

  constructor(protected http: HttpClient) {}

  create(clubType: IClubType): Observable<EntityResponseType> {
    return this.http.post<IClubType>(this.resourceUrl, clubType, { observe: 'response' });
  }

  update(clubType: IClubType): Observable<EntityResponseType> {
    return this.http.put<IClubType>(this.resourceUrl, clubType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClubType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClubType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
