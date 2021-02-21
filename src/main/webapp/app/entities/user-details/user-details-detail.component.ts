import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IUserDetails } from 'app/shared/model/user-details.model';

@Component({
  selector: 'jhi-user-details-detail',
  templateUrl: './user-details-detail.component.html',
})
export class UserDetailsDetailComponent implements OnInit {
  userDetails: IUserDetails | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userDetails }) => (this.userDetails = userDetails));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
