import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { GameService } from '../../services/game';
import { CartService } from '../../services/cart';
import { Game } from '../../model/game.model';
import { coverGradient, coverInitials } from '../../util/cover';

@Component({
  selector: 'app-game-list',
  imports: [RouterLink],
  templateUrl: './game-list.html',
  styleUrl: './game-list.css'
})
export class GameList implements OnInit {
  private gameService = inject(GameService);
  protected cart = inject(CartService);

  games = signal<Game[]>([]);
  loading = signal(true);
  error = signal('');

  search = signal('');
  genre = signal('All');

  genres = computed(() => {
    const set = new Set(this.games().map((g) => g.genre).filter(Boolean));
    return ['All', ...Array.from(set).sort()];
  });

  filtered = computed(() => {
    const q = this.search().trim().toLowerCase();
    const genre = this.genre();
    return this.games().filter((game) => {
      const matchesGenre = genre === 'All' || game.genre === genre;
      const matchesText =
        !q ||
        game.title?.toLowerCase().includes(q) ||
        game.publisherName?.toLowerCase().includes(q);
      return matchesGenre && matchesText;
    });
  });

  cover = coverGradient;
  initials = coverInitials;

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

  onSearch(event: Event) {
    this.search.set((event.target as HTMLInputElement).value);
  }
}
