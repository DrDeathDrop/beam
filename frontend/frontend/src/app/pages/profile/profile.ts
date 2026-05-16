import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user';
import { User } from '../../model/user.model';

interface OwnedGame {
  gameName: string;
  pricePaid: number;
  status: string;
  paymentMethod: string;
}

@Component({
  selector: 'app-profile',
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit {
  private userService = inject(UserService);

  user = signal<User | null>(null);
  ownedGames = signal<OwnedGame[]>([]);
  loading = signal(true);

  ngOnInit() {
    this.userService.getProfile().subscribe({
      next: (data: any) => {
        this.user.set(data);
        if (data.ownedGames) {
          this.ownedGames.set(data.ownedGames);
        }
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }
}
