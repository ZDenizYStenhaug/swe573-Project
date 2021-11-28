import {Component, OnInit} from '@angular/core';
import {Offer} from "./offer";
import {OfferService} from "./offer.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{
  title = 'akita-frontend';

  public offers: Offer[] = [];

  constructor(private offerService: OfferService){}

  ngOnInit(): void {
    this.getOffers();
  }

  public getOffers(): void {
    this.offerService.getOffers().subscribe((response: Offer[]) => { this.offers = response;},
      (error: HttpErrorResponse) => { alert(error.message);}
    );
  }
}
