export class Principal {
  constructor(
    public id: string,
    public email: string,
    public username: string,
    public given_name: string,
    public family_name: string,
    public roles: string[]
  ) {
  }
}
