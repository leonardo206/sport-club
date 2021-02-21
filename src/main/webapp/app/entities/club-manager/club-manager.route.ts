import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IClubManager, ClubManager } from 'app/shared/model/club-manager.model';
import { ClubManagerService } from './club-manager.service';
import { ClubManagerComponent } from './club-manager.component';
import { ClubManagerDetailComponent } from './club-manager-detail.component';
import { ClubManagerUpdateComponent } from './club-manager-update.component';

@Injectable({ providedIn: 'root' })
export class ClubManagerResolve implements Resolve<IClubManager> {
  constructor(private service: ClubManagerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClubManager> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((clubManager: HttpResponse<ClubManager>) => {
          if (clubManager.body) {
            return of(clubManager.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClubManager());
  }
}

export const clubManagerRoute: Routes = [
  {
    path: '',
    component: ClubManagerComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'sportClubApp.clubManager.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClubManagerDetailComponent,
    resolve: {
      clubManager: ClubManagerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubManager.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClubManagerUpdateComponent,
    resolve: {
      clubManager: ClubManagerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubManager.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClubManagerUpdateComponent,
    resolve: {
      clubManager: ClubManagerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sportClubApp.clubManager.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
