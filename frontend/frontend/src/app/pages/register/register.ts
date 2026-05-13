import {Component, computed, signal} from '@angular/core';
import {UserService} from '../../services/user';
import {Router, RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';


@Component({
  selector: 'app-register',
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  email  = ""
  password = ""
  confirmPassword= ""
  name = ""


  loading = signal(false);
  error = signal<string | null>(null);

  get passwordMismatch(): boolean {
    return this.confirmPassword.length > 0 && this.password !== this.confirmPassword;
  }

  constructor(private userService: UserService, private router: Router) {}

  onSubmit(){
    if (this.passwordMismatch) return;
    this.error.set(null);
    this.loading.set(true);

    this.userService.register(this.name, this.email, this.password).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.error.set(err.error?.message ?? 'Register failed. Please try again.');
        this.loading.set(false);
      },
    })
  }


}
