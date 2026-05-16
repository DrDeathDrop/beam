import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../model/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);

  login(email: string, password: string) {
    return this.http.post('/api/users/login', { email, password }, { responseType: 'text' });
  }

  register(name: string, email: string, password: string) {
    return this.http.post('/api/users/register', { name, email, password }, { responseType: 'text' });
  }

  getAllUsers() {
    return this.http.get<User[]>('/api/users/show/all');
  }

  getProfile() {
    return this.http.get<User>('/api/users/profile');
  }

  updateUser(id: number, data: any) {
    return this.http.put(`/api/users/update/${id}`, data, { responseType: 'text' });
  }

  deleteUser(id: number) {
    return this.http.delete(`/api/users/delete/${id}`);
  }
}
