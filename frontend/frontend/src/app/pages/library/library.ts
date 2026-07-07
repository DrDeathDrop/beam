import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PurchaseService, OwnedGame } from '../../services/purchase';
import { coverGradient, coverInitials } from '../../util/cover';

@Component({
  selector: 'app-library',
  imports: [RouterLink],
  templateUrl: './library.html',
  styleUrl: './library.css',
})
export class Library implements OnInit {
  private purchaseService = inject(PurchaseService);

  games = signal<OwnedGame[]>([]);
  loading = signal(true);
  error = signal('');
  refunding = signal<number | null>(null);
  message = signal('');

  cover = coverGradient;
  initials = coverInitials;

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading.set(true);
    this.error.set('');
    this.purchaseService.library().subscribe({
      next: (data) => {
        this.games.set(data.ownedGames ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load your library.');
        this.loading.set(false);
      }
    });
  }

  refund(game: OwnedGame) {
    if (this.refunding() !== null) return;
    this.message.set('');
    this.error.set('');
    this.refunding.set(game.id);

    this.purchaseService.refund(game.id).subscribe({
      next: () => {
        this.refunding.set(null);
        this.message.set(`"${game.gameName}" was refunded.`);
        this.load();
      },
      error: () => {
        this.refunding.set(null);
        this.error.set('Refund failed. This purchase may already be refunded.');
      }
    });
  }
}
