import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IClub, Club } from 'app/shared/model/club.model';
import { ClubService } from './club.service';
import { IClubType } from 'app/shared/model/club-type.model';
import { ClubTypeService } from 'app/entities/club-type/club-type.service';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from 'app/entities/organization/organization.service';

type SelectableEntity = IClubType | IOrganization;

@Component({
  selector: 'jhi-club-update',
  templateUrl: './club-update.component.html',
})
export class ClubUpdateComponent implements OnInit {
  isSaving = false;
  clubtypes: IClubType[] = [];
  organizations: IOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    clubName: [null, [Validators.required]],
    status: [],
    clubTypeId: [null, Validators.required],
    organizationId: [],
  });

  constructor(
    protected clubService: ClubService,
    protected clubTypeService: ClubTypeService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ club }) => {
      this.updateForm(club);

      this.clubTypeService
        .query({ 'clubId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IClubType[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IClubType[]) => {
          if (!club.clubTypeId) {
            this.clubtypes = resBody;
          } else {
            this.clubTypeService
              .find(club.clubTypeId)
              .pipe(
                map((subRes: HttpResponse<IClubType>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IClubType[]) => (this.clubtypes = concatRes));
          }
        });

      this.organizationService.query().subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));
    });
  }

  updateForm(club: IClub): void {
    this.editForm.patchValue({
      id: club.id,
      clubName: club.clubName,
      status: club.status,
      clubTypeId: club.clubTypeId,
      organizationId: club.organizationId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const club = this.createFromForm();
    if (club.id !== undefined) {
      this.subscribeToSaveResponse(this.clubService.update(club));
    } else {
      this.subscribeToSaveResponse(this.clubService.create(club));
    }
  }

  private createFromForm(): IClub {
    return {
      ...new Club(),
      id: this.editForm.get(['id'])!.value,
      clubName: this.editForm.get(['clubName'])!.value,
      status: this.editForm.get(['status'])!.value,
      clubTypeId: this.editForm.get(['clubTypeId'])!.value,
      organizationId: this.editForm.get(['organizationId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClub>>): void {
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
}
