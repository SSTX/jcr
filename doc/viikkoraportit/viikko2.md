# Viikkoraportti 2

## Tunteja käytetty
* Maanantai: 6
* Tiistai: 6
* Keskiviikko: 7
* Yhteensä: 19

Maanantaina ohjelmoin alkeellisen tekstikäyttöliittymän ja aloitin toteuttamaan 
mode of operation -luokkaa.

Tiistaina aloitin DES:n toteutuksen. Kirjoitin testejä DES:lle ja ModeOfOperation:lle.
Törmäsin ongelmiin javan bittitason operaatioiden kanssa. Bit shift muuttaa byte-tyypin
int-tyypiksi ja takaisin, mikä aiheutti ylimääräisiä 1-bittejä vasemmalle.

Keskiviikkona jatkoin DES:n toteutusta ja testausta. Feistel-funktion testaaminen jätetään
myöhemmäksi, koska se on niin monimutkainen ja virheen mahdollisuus on suuri. Testaan muut 
osat, ja jos algoritmi tuottaa väärän tuloksen on vika todennäköisesti feistel-funktiossa.
Metodeita, jotka käyttävät permuteBits:a (expand, permutationP jne.) ei testata erikseen
koska niiden oikeellisuus perustuu testattuun permuteBits:n ja bittien paikat määrittäviin 
taulukoihin, jotka on kopioitu DES:n määritelmästä. 

Seuraavalla viikolla toteutan DES:n key schedulen. Tämän jälkeen DES:n toteutus on lähes valmis.
