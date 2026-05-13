import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);

  login(email: string, password: string) {
    return this.http.post('/api/users/login', { email, password }, { responseType: 'text' });
  }

  register(name: string, email: string, password: string) {
    return this.http.post('/api/users/register', { name, email, password }, { responseType: 'text' });
  }
}
