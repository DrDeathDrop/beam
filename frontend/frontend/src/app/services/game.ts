import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Game } from '../model/game.model';

@Injectable({ providedIn: 'root' })
export class GameService {
  private http = inject(HttpClient);

  buyGame(gameId: number, userId: string, paymentMethod: string, token: string) {
    return this.http.post(
      `/api/purchases/buy/${gameId}`,
      { paymentMethod },
      {
        responseType: 'text',
        headers: { Authorization: `Bearer ${token}` }
      }
    );
  }

  getAllGames() {
    return this.http.get<Game[]>('/api/games/all');
  }

  getGame(id: number) {
    return this.http.get<Game>(`/api/games/view/${id}`);
  }

  createGame(data: any) {
    return this.http.post('/api/games/add', data, { responseType: 'text' });
  }

  updateGame(id: number, data: any) {
    return this.http.put(`/api/games/update/${id}`, data, { responseType: 'text' });
  }

  deleteGame(id: number) {
    return this.http.delete(`/api/games/delete/${id}`);
  }
}
