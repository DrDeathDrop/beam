import { Component, OnInit, signal, OnDestroy, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, NavigationEnd } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { CartService } from '../services/cart';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar implements OnInit, OnDestroy {
  protected cart = inject(CartService);

  role = signal<string | null>(null);
  loggedIn = signal(false);
  private routerSub!: Subscription;

  constructor(private router: Router) {}

  private checkAuth() {
    const token = localStorage.getItem('token');
    this.loggedIn.set(!!token);
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        this.role.set(payload.role);
      } catch {
        this.role.set(null);
      }
    } else {
      this.role.set(null);
    }
  }

  ngOnInit() {
    this.checkAuth();

    this.routerSub = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => this.checkAuth());
  }

  logout() {
    localStorage.removeItem('token');
    this.cart.clear();
    this.loggedIn.set(false);
    this.role.set(null);
    this.router.navigate(['/login']);
  }

  ngOnDestroy() {
    this.routerSub.unsubscribe();
  }
}
