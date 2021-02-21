import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IClubCourt } from 'app/shared/model/club-court.model';

type EntityResponseType = HttpResponse<IClubCourt>;
type EntityArrayResponseType = HttpResponse<IClubCourt[]>;

@Injectable({ providedIn: 'root' })
export class ClubCourtService {
  public resourceUrl = SERVER_API_URL + 'api/club-courts';

  constructor(protected http: HttpClient) {}

  create(clubCourt: IClubCourt): Observable<EntityResponseType> {
    return this.http.post<IClubCourt>(this.resourceUrl, clubCourt, { observe: 'response' });
  }

  update(clubCourt: IClubCourt): Observable<EntityResponseType> {
    return this.http.put<IClubCourt>(this.resourceUrl, clubCourt, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClubCourt>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClubCourt[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
