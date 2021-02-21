import { Moment } from 'moment';
import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

export interface IUserDetails {
  id?: number;
  mobileNumber?: string;
  dateOfBirth?: Moment;
  profilePicContentType?: string;
  profilePic?: any;
  description?: string;
  status?: ActiveInactiveStatus;
  userLogin?: string;
  userId?: number;
}

export class UserDetails implements IUserDetails {
  constructor(
    public id?: number,
    public mobileNumber?: string,
    public dateOfBirth?: Moment,
    public profilePicContentType?: string,
    public profilePic?: any,
    public description?: string,
    public status?: ActiveInactiveStatus,
    public userLogin?: string,
    public userId?: number
  ) {}
}
