# POC CQRS #

Acest POC demonstreaza utilizarea librariei cqrs-java.

## Reguli de business ##

Aplicatia emuleaza o banca care contine conturi bancare in care se pot depune bani si din care se pot retrage bani.
Regula 1. Nu se pot retrage mai multi bani decat au fost depusi.
Regula 2. Daca banca ramane fara bani atunci se inchide (si nu mai sunt permise transferuri).

## Modele Write ##

Exista un singur Model Write: BankAccount. El protejeaza **Regula 1**.

## Modele Read ##

Exista doua modele:
1. lista de conturi bancare + balantele lor
2. totalul de bani din banca

## Saga/Process manager ##

Saga este un fel de Read Model daca care primeste o singura data un eveniment.
Este nevoie de acest tip de model pentru ca la procesarea evenimentelor se produc efecte in afara sistemului
(de exemplu se trimit emailuri) care nu trebuie repetate cand se reconstruieste modelul.

Exista un singur Saga in POC, CloseBankOnBankruptSaga, care inchide banca cand ramane fara capital 
(protejeaza **Regula 2**).