import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface Publisher {
  id: number;
  name: string;
}

@Injectable({ providedIn: 'root' })
export class PublisherService {
  private http = inject(HttpClient);

  getAllPublishers() {
    return this.http.get<Publisher[]>('/api/publishers/all');
  }
}
