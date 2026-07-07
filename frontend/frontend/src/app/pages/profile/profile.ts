import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { UserService } from '../../services/user';
import { User } from '../../model/user.model';

interface OwnedGame {
  id: number;
  gameName: string;
  pricePaid: number;
  status: string;
  paymentMethod: string;
}

@Component({
  selector: 'app-profile',
  imports: [RouterLink],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit {
  private userService = inject(UserService);

  user = signal<User | null>(null);
  ownedGames = signal<OwnedGame[]>([]);
  loading = signal(true);

  owned = computed(() => this.ownedGames().filter((g) => g.status !== 'REFUNDED'));
  totalSpent = computed(() =>
    this.owned().reduce((sum, g) => sum + Number(g.pricePaid), 0)
  );

  ngOnInit() {
    this.userService.getProfile().subscribe({
      next: (data: any) => {
        this.user.set(data);
        this.ownedGames.set(data.ownedGames ?? []);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }
}
