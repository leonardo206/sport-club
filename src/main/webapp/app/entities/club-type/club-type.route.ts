import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IClubType, ClubType } from 'app/shared/model/club-type.model';
import { ClubTypeService } from './club-type.service';
import { ClubTypeComponent } from './club-type.component';
import { ClubTypeDetailComponent } from './club-type-detail.component';
import { ClubTypeUpdateComponent } from './club-type-update.component';

@Injectable({ providedIn: 'root' })
export class ClubTypeResolve implements Resolve<IClubType> {
  constructor(private service: ClubTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClubType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((clubType: HttpResponse<ClubType>) => {
          if (clubType.body) {
            return of(clubType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClubType());
  }
}

export const clubTypeRoute: Routes = [
  {
    path: '',
    component: ClubTypeComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'sportClubApp.clubType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClubTypeDetailComponent,
    resolve: {
      clubType: ClubTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClubTypeUpdateComponent,
    resolve: {
      clubType: ClubTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClubTypeUpdateComponent,
    resolve: {
      clubType: ClubTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
