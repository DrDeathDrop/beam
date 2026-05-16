import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../../services/user';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email = '';
  password = '';

  loading = signal(false);
  error = signal<string | null>(null);

  constructor(private userService: UserService, private router: Router) {}

  onSubmit() {
    this.error.set(null);
    this.loading.set(true);

    this.userService.login(this.email, this.password).subscribe({
      next: (token) => {
        localStorage.setItem('token', token);

        const payload = JSON.parse(atob(token.split('.')[1]));

        if (payload.role === 'ADMIN') {
          this.router.navigate(['/dashboard']);
        } else {
          this.router.navigate(['/store']);
        }
      },
      error: () => {
        this.error.set('Invalid credentials');

      }
    });
  }


}
