export interface IOrganization {
  id?: number;
  organizationOwnerJhiUserId?: string;
  organizationName?: string;
  taxNumber?: string;
  status?: string;
  userLogin?: string;
  userId?: number;
}

export class Organization implements IOrganization {
  constructor(
    public id?: number,
    public organizationOwnerJhiUserId?: string,
    public organizationName?: string,
    public taxNumber?: string,
    public status?: string,
    public userLogin?: string,
    public userId?: number
  ) {}
}
