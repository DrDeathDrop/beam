import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Game } from '../model/game.model';

@Injectable({ providedIn: 'root' })
export class GameService {
  private http = inject(HttpClient);

  getAllGames() {
    return this.http.get<Game[]>('/api/games/all');
  }

  getGame(id: number) {
    return this.http.get<Game>(`/api/games/view/${id}`);
  }
}
