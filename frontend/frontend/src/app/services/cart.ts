import { Injectable, computed, effect, signal } from '@angular/core';
import { Game } from '../model/game.model';

const STORAGE_KEY = 'beam_cart';

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly _items = signal<Game[]>(this.load());

  readonly items = this._items.asReadonly();
  readonly count = computed(() => this._items().length);
  readonly total = computed(() =>
    this._items().reduce((sum, g) => sum + Number(g.price), 0)
  );

  constructor() {
    // Persist to localStorage whenever the cart changes.
    effect(() => localStorage.setItem(STORAGE_KEY, JSON.stringify(this._items())));
  }

  private load(): Game[] {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      return raw ? (JSON.parse(raw) as Game[]) : [];
    } catch {
      return [];
    }
  }

  has(id: number): boolean {
    return this._items().some((g) => g.id === id);
  }

  add(game: Game): void {
    if (!this.has(game.id)) {
      this._items.update((list) => [...list, game]);
    }
  }

  remove(id: number): void {
    this._items.update((list) => list.filter((g) => g.id !== id));
  }

  toggle(game: Game): void {
    if (this.has(game.id)) {
      this.remove(game.id);
    } else {
      this.add(game);
    }
  }

  clear(): void {
    this._items.set([]);
  }
}
