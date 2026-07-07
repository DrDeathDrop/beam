import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GameService } from '../../services/game';
import { UserService } from '../../services/user';
import { Game } from '../../model/game.model';
import { User } from '../../model/user.model';
import { Router } from '@angular/router';
import {Publisher, PublisherService} from '../../services/publisher';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  private gameService = inject(GameService);
  private userService = inject(UserService);
  private router = inject(Router);
  private publisherService = inject(PublisherService);
  publishers = signal<Publisher[]>([]);

  games = signal<Game[]>([]);
  users = signal<User[]>([]);
  error = signal('');

  gameForm = { title: '', genre: '', price: 0, publisherId: 0, description: '', releaseDate: '' };
  editingGame = signal<Game | null>(null);

  userForm = { name: '', email: '', password: '' };
  editingUser = signal<User | null>(null);

  ngOnInit() {
    this.loadGames();
    this.loadUsers();
    this.publisherService.getAllPublishers().subscribe(data => this.publishers.set(data));
  }

  loadGames() {
    this.gameService.getAllGames().subscribe(data => this.games.set(data));
  }

  loadUsers() {
    this.userService.getAllUsers().subscribe(data => this.users.set(data));
  }

  submitGame() {
    if (this.editingGame()) {
      this.gameService.updateGame(this.editingGame()!.id, this.gameForm).subscribe(() => {
        this.loadGames();
        this.resetGameForm();
      });
    } else {
      this.gameService.createGame(this.gameForm).subscribe(() => {
        this.loadGames();
        this.resetGameForm();
      });
    }
  }

  editGame(game: Game) {
    this.editingGame.set(game);
    this.gameForm = {
      title: game.title,
      genre: game.genre,
      price: game.price,
      publisherId: game.publisherId,
      description: game.description,
      releaseDate: game.releaseDate
    };
  }

  deleteGame(id: number) {
    this.error.set('');
    this.gameService.deleteGame(id).subscribe({
      next: () => this.loadGames(),
      error: () => this.error.set('Could not delete that game.')
    });
  }

  resetGameForm() {
    this.editingGame.set(null);
    this.gameForm = { title: '', genre: '', price: 0, publisherId: 0, description: '', releaseDate: '' };
  }

  submitUser() {
    if (this.editingUser()) {
      this.userService.updateUser(this.editingUser()!.id, this.userForm).subscribe(() => {
        this.loadUsers();
        this.resetUserForm();
      });
    }
  }

  editUser(user: User) {
    this.editingUser.set(user);
    this.userForm = { name: user.name, email: user.email, password: '' };
  }

  deleteUser(id: number) {
    this.error.set('');
    this.userService.deleteUser(id).subscribe({
      next: () => this.loadUsers(),
      error: () => this.error.set('Could not delete that user.')
    });
  }

  resetUserForm() {
    this.editingUser.set(null);
    this.userForm = { name: '', email: '', password: '' };
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }



}
