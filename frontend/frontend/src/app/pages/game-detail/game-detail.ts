import {Component, inject, OnInit, signal} from '@angular/core';
import {GameService} from '../../services/game';
import {Game} from '../../model/game.model';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-game-detail',
  imports: [],
  templateUrl: './game-detail.html',
  styleUrl: './game-detail.css',
})
export class GameDetail implements OnInit{
  private gameService = inject(GameService);
  private route = inject(ActivatedRoute);  // reads the URL

  game = signal<Game | null>(null);  // null until loaded
  loading = signal(true);
  error = signal('');

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));  // grabs :id from the URL

    this.gameService.getGame(id).subscribe({
      next: (data) => {
        this.game.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load game. Is the backend running?');
        this.loading.set(false);
      }
    });
  }
}
