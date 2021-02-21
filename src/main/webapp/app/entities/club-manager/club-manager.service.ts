import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IClubManager } from 'app/shared/model/club-manager.model';

type EntityResponseType = HttpResponse<IClubManager>;
type EntityArrayResponseType = HttpResponse<IClubManager[]>;

@Injectable({ providedIn: 'root' })
export class ClubManagerService {
  public resourceUrl = SERVER_API_URL + 'api/club-managers';

  constructor(protected http: HttpClient) {}

  create(clubManager: IClubManager): Observable<EntityResponseType> {
    return this.http.post<IClubManager>(this.resourceUrl, clubManager, { observe: 'response' });
  }

  update(clubManager: IClubManager): Observable<EntityResponseType> {
    return this.http.put<IClubManager>(this.resourceUrl, clubManager, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClubManager>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClubManager[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
