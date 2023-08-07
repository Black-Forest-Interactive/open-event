import {Pageable} from "./pageable";

export interface Page<T> {
  content: T[];
  pageable: Pageable;
  totalPages: number;
  totalSize: number;
  offset: number;
  size: number;
  empty: boolean;
}
