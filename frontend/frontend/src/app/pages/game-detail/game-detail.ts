import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { GameService } from '../../services/game';
import { PurchaseService } from '../../services/purchase';
import { CartService } from '../../services/cart';
import { Game } from '../../model/game.model';
import { coverGradient, coverInitials } from '../../util/cover';

@Component({
  selector: 'app-game-detail',
  imports: [RouterLink],
  templateUrl: './game-detail.html',
  styleUrl: './game-detail.css',
})
export class GameDetail implements OnInit {
  private gameService = inject(GameService);
  private purchaseService = inject(PurchaseService);
  protected cart = inject(CartService);
  private route = inject(ActivatedRoute);

  game = signal<Game | null>(null);
  loading = signal(true);
  error = signal('');

  paymentMethod = signal('CREDIT_CARD');
  buying = signal(false);
  purchased = signal(false);
  buyError = signal('');

  cover = coverGradient;
  initials = coverInitials;

  get isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.gameService.getGame(id).subscribe({
      next: (data) => {
        this.game.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load game. Is the backend running?');
        this.loading.set(false);
      }
    });
  }

  buyNow() {
    const game = this.game();
    if (!game) return;

    this.buyError.set('');
    if (!this.isLoggedIn) {
      this.buyError.set('Please sign in to buy this game.');
      return;
    }

    this.buying.set(true);
    this.purchaseService.buy(game.id, this.paymentMethod()).subscribe({
      next: () => {
        this.buying.set(false);
        this.purchased.set(true);
        this.cart.remove(game.id);
      },
      error: () => {
        this.buying.set(false);
        this.buyError.set('Purchase failed. Please try again.');
      }
    });
  }
}
