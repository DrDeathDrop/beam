import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameService } from '../../services/game';
import { Game } from '../../model/game.model';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-game-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './game-list.html',
  styleUrl: './game-list.css'
})
export class GameList implements OnInit {
  private gameService = inject(GameService);

  games = signal<Game[]>([]);
  loading = signal(true);
  error = signal('');

  ngOnInit() {
    this.gameService.getAllGames().subscribe({
      next: (data) => {
        this.games.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load games. Is the backend running?');
        this.loading.set(false);
      }
    });
  }
}
