export interface Profile {
  id: number,

  email: string | undefined,
  phone: string | undefined,
  mobile: string | undefined,

  firstName: string,
  lastName: string,

  dateOfBirth: string | undefined,
  gender: string | undefined,
  profilePicture: string | undefined,
  website: string | undefined,

  language: string
}

export class ProfileChangeRequest {
  constructor(
    public email: string | undefined,
    public phone: string | undefined,
    public mobile: string | undefined,
    public firstName: string,
    public lastName: string,
    public dateOfBirth: string | undefined,
    public gender: string | undefined,
    public profilePicture: string | undefined,
    public website: string | undefined,
    public language: string
  ) {
  }
}
