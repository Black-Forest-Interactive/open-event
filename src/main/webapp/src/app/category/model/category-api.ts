export interface Category {
  id: number,
  name: string,
  iconUrl: string
}

export class CategoryChangeRequest {
  constructor(
    public name: string,
    public iconUrl: string
  ) {
  }
}
