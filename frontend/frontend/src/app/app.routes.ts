import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'store', pathMatch: 'full' },
  { path: 'store', loadComponent: () => import('./pages/game-list/game-list').then(m => m.GameList) },
  { path: 'game/:id', loadComponent: () => import('./pages/game-detail/game-detail').then(m => m.GameDetail) },
  { path: 'login', loadComponent: () => import('./pages/login/login').then(m => m.Login) },
  { path: 'register', loadComponent: () => import('./pages/register/register').then(m => m.Register) },
  { path: 'library', loadComponent: () => import('./pages/library/library').then(m => m.Library) },
];
