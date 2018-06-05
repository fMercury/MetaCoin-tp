import { Component, HostListener, NgZone } from '@angular/core';
import { Web3Service, MetaCoinService } from '../services/services';
import { canBeNumber } from '../util/validation';

declare var window: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  account: any;

  balance: number;
  sendingAmount: number;
  recipientAddress: string;
  status: string;
  canBeNumber = canBeNumber;

  constructor(private _ngZone: NgZone, private web3Service: Web3Service, private metaCoinService: MetaCoinService) {
    this.onReady();
  }

  openAddress() {
    window.open('http://etherscan.io/address/' + this.account, '_blank');
  }

  sendCoin = () => {
    this.setStatus('Inicializando la transacción... Por favor, espere...');

    this.metaCoinService.sendCoin(this.account, this.recipientAddress, this.sendingAmount).subscribe(
      () => {
        this.setStatus('Transacción completada con éxito.');
        this.refreshBalance();
      },
      e => this.setStatus('Error enviando metacoin.')
    );
  };

  onReady = () => {
    this.web3Service.getAccounts().subscribe(
      accs => {
        this.account = accs[0];
        // console.log('accs: ', this.account);

        this._ngZone.run(() => this.refreshBalance());
      },
      err => alert(err)
    );
  };

  refreshBalance = () => {
    this.metaCoinService.getBalance(this.account).subscribe(
      value => {
        this.balance = value;
      },
      e => {
        this.setStatus('Error obteniendo el balance.');
      }
    );
  };

  setStatus = message => {
    this.status = message;
  };
}
