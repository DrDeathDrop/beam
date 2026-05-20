import { Component, OnInit, signal, OnDestroy } from '@angular/core';
import { Router, RouterLink, NavigationEnd } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar implements OnInit, OnDestroy {
  role = signal<string | null>(null);
  loggedIn = signal(false);
  private routerSub!: Subscription;

  constructor(private router: Router) {}

  private checkAuth() {
    const token = localStorage.getItem('token');
    this.loggedIn.set(!!token);
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      this.role.set(payload.role);
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
    this.router.navigate(['/login']);
    this.loggedIn.set(false);
  }

  ngOnDestroy() {
    this.routerSub.unsubscribe();
  }
}
