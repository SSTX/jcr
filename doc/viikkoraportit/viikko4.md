# Viikkoraportti 4

## Tuntikirjanpito
* Maanantai: 1
* Tiistai: 4
* Keskiviikko: 2
* Torstai: 3
* Perjantai: 5
* Yhteensä: 15

Maanantaina aloitin kirjoittamaan metodia, joka pidentää tai lyhentää byte[] -tyyppistä
taulukkoa käyttämällä (vain) tietyn määrän bittejä joka tavusta. Tarvitsin sitä 
selventämään feistel-funktion toimintaa.

Tiistaina tein maanantaina aloittamani toteutuksen loppuun ja testasin. Nyt 
Feistel-funktio toimii oikein. Algoritmissa on kuitenkin jotain muutakin vikaa.

Keskiviikkona jatkoin debuggausta. Kävi ilmi, että vika oli bitwiseXOR-metodissa.
Olin unohtanut, että taulukot ovat viiteparametreja ja käytin parametria
arr1 tuloksen laskemiseen. Tämä aiheutti epätoivotun sivuvaikutuksen kutsuvassa 
metodissa. DES alkoi toimimaan oikein korjauksen jälkeen.

Torstaina työskentelin ECB:n toteutuksen kanssa.

Perjantaina kirjoitin vielä muutaman testin Cryptographer-luokalle. Aloitin testaus-
ja toteutusdokumentaation kirjoittamisen. Kirjoitin vertaisarvion.