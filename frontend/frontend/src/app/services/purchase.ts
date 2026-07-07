import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface OwnedGame {
  id: number;
  gameName: string;
  pricePaid: number;
  status: string;
  paymentMethod: string;
}

@Injectable({ providedIn: 'root' })
export class PurchaseService {
  private http = inject(HttpClient);

  /** Buy a single game. The auth token is attached by the HTTP interceptor. */
  buy(gameId: number, paymentMethod: string) {
    return this.http.post(`/api/purchases/buy/${gameId}`, { paymentMethod }, { responseType: 'text' });
  }

  /** Refund a previously completed purchase. */
  refund(purchaseId: number) {
    return this.http.post(`/api/refunds/${purchaseId}`, {}, { responseType: 'text' });
  }

  /** The signed-in user's owned games (from their profile). */
  library() {
    return this.http.get<{ ownedGames: OwnedGame[] }>('/api/users/profile');
  }
}
