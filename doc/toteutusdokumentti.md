# Ohjelman rakenne
Ohjelmalla on yksinkertainen teksipohjainen käyttöliittymä (luokka TextInterface). 
Käyttäjä syöttää sen avulla tarvittavat tiedot salaukseen/purkamiseen, ja ne välitetään
Cryptographer-oliolle. Se hoitaa tiedostojen lukemisen, sekä algoritmin ja toimintatavan valinnan.
Luettu tieto annetaan seuraavaksi ModeOfOperation-luokan perivälle oliolle. Se lisää käsiteltävään
dataan täytettä niin, että se on käytettävän algoritmin lohkokoon monikerta. Sitten data annetaan 
lohko kerrallaan salausalgoritmille ja lopuksi se palautetaan Cryptographer:lle joka kirjoittaa
sen tiedostoon.

# Aika- ja tilavaativuudet
ECB-DES toimii aikavaativuudella O(n), missä n on salattavan/purettavan datan koko (ks. [testausdokumentti](testausdokumentti.md).
Tilavaativuus on O(n), koska purettava/salattava data luetaan muistiin kokonaisuudessaan kerralla.

# Ongelmia ja mahdollisia ratkaisuja
Tilavaativuuden voisi mahdollisesti laskea vakioksi lukemalla ja käsittelemällä dataa vain tietty määrä kerrallaan.

# Lähteet
## Data Encryption Standard (DES)
* https://en.wikipedia.org/wiki/Data_Encryption_Standard
* https://en.wikipedia.org/wiki/DES_supplementary_material
* [DES:n määritelmä](http://csrc.nist.gov/publications/fips/fips46-3/fips46-3.pdf)
* [The DES Algorithm illustrated](http://page.math.tu-berlin.de/~kant/teaching/hess/krypto-ws2006/des.htm)
