import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IUserDetails, UserDetails } from 'app/shared/model/user-details.model';
import { UserDetailsService } from './user-details.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-user-details-update',
  templateUrl: './user-details-update.component.html',
})
export class UserDetailsUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    mobileNumber: [null, [Validators.required]],
    dateOfBirth: [null, [Validators.required]],
    profilePic: [],
    profilePicContentType: [],
    description: [],
    status: [],
    userId: [null, Validators.required],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected userDetailsService: UserDetailsService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userDetails }) => {
      if (!userDetails.id) {
        const today = moment().startOf('day');
        userDetails.dateOfBirth = today;
      }

      this.updateForm(userDetails);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(userDetails: IUserDetails): void {
    this.editForm.patchValue({
      id: userDetails.id,
      mobileNumber: userDetails.mobileNumber,
      dateOfBirth: userDetails.dateOfBirth ? userDetails.dateOfBirth.format(DATE_TIME_FORMAT) : null,
      profilePic: userDetails.profilePic,
      profilePicContentType: userDetails.profilePicContentType,
      description: userDetails.description,
      status: userDetails.status,
      userId: userDetails.userId,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('sportClubApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userDetails = this.createFromForm();
    if (userDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.userDetailsService.update(userDetails));
    } else {
      this.subscribeToSaveResponse(this.userDetailsService.create(userDetails));
    }
  }

  private createFromForm(): IUserDetails {
    return {
      ...new UserDetails(),
      id: this.editForm.get(['id'])!.value,
      mobileNumber: this.editForm.get(['mobileNumber'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value
        ? moment(this.editForm.get(['dateOfBirth'])!.value, DATE_TIME_FORMAT)
        : undefined,
      profilePicContentType: this.editForm.get(['profilePicContentType'])!.value,
      profilePic: this.editForm.get(['profilePic'])!.value,
      description: this.editForm.get(['description'])!.value,
      status: this.editForm.get(['status'])!.value,
      userId: this.editForm.get(['userId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserDetails>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
