import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IClubManager, ClubManager } from 'app/shared/model/club-manager.model';
import { ClubManagerService } from './club-manager.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IClub } from 'app/shared/model/club.model';
import { ClubService } from 'app/entities/club/club.service';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from 'app/entities/organization/organization.service';

type SelectableEntity = IUser | IClub | IOrganization;

@Component({
  selector: 'jhi-club-manager-update',
  templateUrl: './club-manager-update.component.html',
})
export class ClubManagerUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  clubs: IClub[] = [];
  organizations: IOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    status: [],
    userId: [null, Validators.required],
    clubs: [],
    organizationId: [],
  });

  constructor(
    protected clubManagerService: ClubManagerService,
    protected userService: UserService,
    protected clubService: ClubService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clubManager }) => {
      this.updateForm(clubManager);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.clubService.query().subscribe((res: HttpResponse<IClub[]>) => (this.clubs = res.body || []));

      this.organizationService.query().subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));
    });
  }

  updateForm(clubManager: IClubManager): void {
    this.editForm.patchValue({
      id: clubManager.id,
      status: clubManager.status,
      userId: clubManager.userId,
      clubs: clubManager.clubs,
      organizationId: clubManager.organizationId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clubManager = this.createFromForm();
    if (clubManager.id !== undefined) {
      this.subscribeToSaveResponse(this.clubManagerService.update(clubManager));
    } else {
      this.subscribeToSaveResponse(this.clubManagerService.create(clubManager));
    }
  }

  private createFromForm(): IClubManager {
    return {
      ...new ClubManager(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      clubs: this.editForm.get(['clubs'])!.value,
      organizationId: this.editForm.get(['organizationId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClubManager>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IClub[], option: IClub): IClub {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
