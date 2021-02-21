import { IBooking } from 'app/shared/model/booking.model';
import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

export interface IClient {
  id?: number;
  status?: ActiveInactiveStatus;
  userLogin?: string;
  userId?: number;
  bookings?: IBooking[];
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public status?: ActiveInactiveStatus,
    public userLogin?: string,
    public userId?: number,
    public bookings?: IBooking[]
  ) {}
}
