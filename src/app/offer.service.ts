import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient} from '@angular/common/http';
import { Offer } from './offer';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root'})
export class OfferService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getOffers(): Observable<Offer[]> {
    return this.http.get<Offer[]>(`${this.apiServerUrl}/offer/all`);
  }
}
