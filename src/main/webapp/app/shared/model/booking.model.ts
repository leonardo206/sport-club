import { Moment } from 'moment';
import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

export interface IBooking {
  id?: number;
  bookingCode?: string;
  bookingTime?: Moment;
  status?: ActiveInactiveStatus;
  clientId?: number;
  clubCourtCourtName?: string;
  clubCourtId?: number;
}

export class Booking implements IBooking {
  constructor(
    public id?: number,
    public bookingCode?: string,
    public bookingTime?: Moment,
    public status?: ActiveInactiveStatus,
    public clientId?: number,
    public clubCourtCourtName?: string,
    public clubCourtId?: number
  ) {}
}
