import { IBooking } from 'app/shared/model/booking.model';
import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

export interface IClubCourt {
  id?: number;
  courtCode?: string;
  courtName?: string;
  status?: ActiveInactiveStatus;
  clubCourtTypeClubCourtTypeName?: string;
  clubCourtTypeId?: number;
  bookings?: IBooking[];
  clubClubName?: string;
  clubId?: number;
}

export class ClubCourt implements IClubCourt {
  constructor(
    public id?: number,
    public courtCode?: string,
    public courtName?: string,
    public status?: ActiveInactiveStatus,
    public clubCourtTypeClubCourtTypeName?: string,
    public clubCourtTypeId?: number,
    public bookings?: IBooking[],
    public clubClubName?: string,
    public clubId?: number
  ) {}
}
