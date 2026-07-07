import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { CartService } from '../../services/cart';
import { PurchaseService } from '../../services/purchase';
import { coverGradient, coverInitials } from '../../util/cover';

@Component({
  selector: 'app-cart',
  imports: [RouterLink],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart {
  protected cart = inject(CartService);
  private purchaseService = inject(PurchaseService);
  private router = inject(Router);

  paymentMethod = signal('CREDIT_CARD');
  processing = signal(false);
  error = signal('');

  cover = coverGradient;
  initials = coverInitials;

  get isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  checkout() {
    const items = this.cart.items();
    if (items.length === 0) return;

    this.error.set('');
    if (!this.isLoggedIn) {
      this.router.navigate(['/login']);
      return;
    }

    this.processing.set(true);
    const purchases = items.map((g) => this.purchaseService.buy(g.id, this.paymentMethod()));

    forkJoin(purchases).subscribe({
      next: () => {
        this.processing.set(false);
        this.cart.clear();
        this.router.navigate(['/library']);
      },
      error: () => {
        this.processing.set(false);
        this.error.set('Checkout failed. Some items may not have been purchased.');
      }
    });
  }
}
