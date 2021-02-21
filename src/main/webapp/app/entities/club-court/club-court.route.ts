import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IClubCourt, ClubCourt } from 'app/shared/model/club-court.model';
import { ClubCourtService } from './club-court.service';
import { ClubCourtComponent } from './club-court.component';
import { ClubCourtDetailComponent } from './club-court-detail.component';
import { ClubCourtUpdateComponent } from './club-court-update.component';

@Injectable({ providedIn: 'root' })
export class ClubCourtResolve implements Resolve<IClubCourt> {
  constructor(private service: ClubCourtService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClubCourt> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((clubCourt: HttpResponse<ClubCourt>) => {
          if (clubCourt.body) {
            return of(clubCourt.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClubCourt());
  }
}

export const clubCourtRoute: Routes = [
  {
    path: '',
    component: ClubCourtComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'sportClubApp.clubCourt.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClubCourtDetailComponent,
    resolve: {
      clubCourt: ClubCourtResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubCourt.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClubCourtUpdateComponent,
    resolve: {
      clubCourt: ClubCourtResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubCourt.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClubCourtUpdateComponent,
    resolve: {
      clubCourt: ClubCourtResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubCourt.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
