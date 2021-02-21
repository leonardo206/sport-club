import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IClubCourt, ClubCourt } from 'app/shared/model/club-court.model';
import { ClubCourtService } from './club-court.service';
import { IClubCourtType } from 'app/shared/model/club-court-type.model';
import { ClubCourtTypeService } from 'app/entities/club-court-type/club-court-type.service';
import { IClub } from 'app/shared/model/club.model';
import { ClubService } from 'app/entities/club/club.service';

type SelectableEntity = IClubCourtType | IClub;

@Component({
  selector: 'jhi-club-court-update',
  templateUrl: './club-court-update.component.html',
})
export class ClubCourtUpdateComponent implements OnInit {
  isSaving = false;
  clubcourttypes: IClubCourtType[] = [];
  clubs: IClub[] = [];

  editForm = this.fb.group({
    id: [],
    courtCode: [null, [Validators.required]],
    courtName: [null, [Validators.required]],
    status: [],
    clubCourtTypeId: [null, Validators.required],
    clubId: [],
  });

  constructor(
    protected clubCourtService: ClubCourtService,
    protected clubCourtTypeService: ClubCourtTypeService,
    protected clubService: ClubService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clubCourt }) => {
      this.updateForm(clubCourt);

      this.clubCourtTypeService
        .query({ 'clubCourtId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IClubCourtType[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IClubCourtType[]) => {
          if (!clubCourt.clubCourtTypeId) {
            this.clubcourttypes = resBody;
          } else {
            this.clubCourtTypeService
              .find(clubCourt.clubCourtTypeId)
              .pipe(
                map((subRes: HttpResponse<IClubCourtType>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IClubCourtType[]) => (this.clubcourttypes = concatRes));
          }
        });

      this.clubService.query().subscribe((res: HttpResponse<IClub[]>) => (this.clubs = res.body || []));
    });
  }

  updateForm(clubCourt: IClubCourt): void {
    this.editForm.patchValue({
      id: clubCourt.id,
      courtCode: clubCourt.courtCode,
      courtName: clubCourt.courtName,
      status: clubCourt.status,
      clubCourtTypeId: clubCourt.clubCourtTypeId,
      clubId: clubCourt.clubId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clubCourt = this.createFromForm();
    if (clubCourt.id !== undefined) {
      this.subscribeToSaveResponse(this.clubCourtService.update(clubCourt));
    } else {
      this.subscribeToSaveResponse(this.clubCourtService.create(clubCourt));
    }
  }

  private createFromForm(): IClubCourt {
    return {
      ...new ClubCourt(),
      id: this.editForm.get(['id'])!.value,
      courtCode: this.editForm.get(['courtCode'])!.value,
      courtName: this.editForm.get(['courtName'])!.value,
      status: this.editForm.get(['status'])!.value,
      clubCourtTypeId: this.editForm.get(['clubCourtTypeId'])!.value,
      clubId: this.editForm.get(['clubId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClubCourt>>): void {
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
