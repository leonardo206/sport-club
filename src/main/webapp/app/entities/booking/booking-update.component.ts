import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IBooking, Booking } from 'app/shared/model/booking.model';
import { BookingService } from './booking.service';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client/client.service';
import { IClubCourt } from 'app/shared/model/club-court.model';
import { ClubCourtService } from 'app/entities/club-court/club-court.service';

type SelectableEntity = IClient | IClubCourt;

@Component({
  selector: 'jhi-booking-update',
  templateUrl: './booking-update.component.html',
})
export class BookingUpdateComponent implements OnInit {
  isSaving = false;
  clients: IClient[] = [];
  clubcourts: IClubCourt[] = [];

  editForm = this.fb.group({
    id: [],
    bookingCode: [null, [Validators.required]],
    bookingTime: [null, [Validators.required]],
    status: [],
    clientId: [],
    clubCourtId: [],
  });

  constructor(
    protected bookingService: BookingService,
    protected clientService: ClientService,
    protected clubCourtService: ClubCourtService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booking }) => {
      if (!booking.id) {
        const today = moment().startOf('day');
        booking.bookingTime = today;
      }

      this.updateForm(booking);

      this.clientService.query().subscribe((res: HttpResponse<IClient[]>) => (this.clients = res.body || []));

      this.clubCourtService.query().subscribe((res: HttpResponse<IClubCourt[]>) => (this.clubcourts = res.body || []));
    });
  }

  updateForm(booking: IBooking): void {
    this.editForm.patchValue({
      id: booking.id,
      bookingCode: booking.bookingCode,
      bookingTime: booking.bookingTime ? booking.bookingTime.format(DATE_TIME_FORMAT) : null,
      status: booking.status,
      clientId: booking.clientId,
      clubCourtId: booking.clubCourtId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const booking = this.createFromForm();
    if (booking.id !== undefined) {
      this.subscribeToSaveResponse(this.bookingService.update(booking));
    } else {
      this.subscribeToSaveResponse(this.bookingService.create(booking));
    }
  }

  private createFromForm(): IBooking {
    return {
      ...new Booking(),
      id: this.editForm.get(['id'])!.value,
      bookingCode: this.editForm.get(['bookingCode'])!.value,
      bookingTime: this.editForm.get(['bookingTime'])!.value
        ? moment(this.editForm.get(['bookingTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      status: this.editForm.get(['status'])!.value,
      clientId: this.editForm.get(['clientId'])!.value,
      clubCourtId: this.editForm.get(['clubCourtId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooking>>): void {
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
