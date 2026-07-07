import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';

import { PurchaseService } from './purchase';

describe('PurchaseService', () => {
  let service: PurchaseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient()],
    });
    service = TestBed.inject(PurchaseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
