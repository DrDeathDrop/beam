import { Component, OnInit, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar implements OnInit {
  role = signal<string | null>(null);
  loggedIn = signal(false);

  constructor(private router: Router) {}

  ngOnInit() {
    const token = localStorage.getItem('token');
    this.loggedIn.set(!!token);
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      this.role.set(payload.role);
    }
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
