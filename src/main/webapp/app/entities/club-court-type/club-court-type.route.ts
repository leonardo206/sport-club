import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IClubCourtType, ClubCourtType } from 'app/shared/model/club-court-type.model';
import { ClubCourtTypeService } from './club-court-type.service';
import { ClubCourtTypeComponent } from './club-court-type.component';
import { ClubCourtTypeDetailComponent } from './club-court-type-detail.component';
import { ClubCourtTypeUpdateComponent } from './club-court-type-update.component';

@Injectable({ providedIn: 'root' })
export class ClubCourtTypeResolve implements Resolve<IClubCourtType> {
  constructor(private service: ClubCourtTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClubCourtType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((clubCourtType: HttpResponse<ClubCourtType>) => {
          if (clubCourtType.body) {
            return of(clubCourtType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClubCourtType());
  }
}

export const clubCourtTypeRoute: Routes = [
  {
    path: '',
    component: ClubCourtTypeComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'sportClubApp.clubCourtType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClubCourtTypeDetailComponent,
    resolve: {
      clubCourtType: ClubCourtTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubCourtType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClubCourtTypeUpdateComponent,
    resolve: {
      clubCourtType: ClubCourtTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubCourtType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClubCourtTypeUpdateComponent,
    resolve: {
      clubCourtType: ClubCourtTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubCourtType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
