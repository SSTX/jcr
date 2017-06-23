# Yleiskuvaus
Toteutus Data Encryption Standardille (DES), ja algoritmin käyttämiseen tarvittavat komponentit
ja työkalut: Electronic Code Book (ECB), tiedostojen lukeminen/kirjoittaminen, komentorivikäyttöliittymä,
taulukon täyttäminen oikean pituiseksi (PKCS#7 padding).
Käytän toteutuksessa Javaa ja Mavenia.

# Ohjelman toiminta
Ohjelmaa käytetään komentoriviltä. Syötteenä annetaan luettavien ja kirjoitettavien tiedostojen sijainnit
ja valitsin salaamiselle/purkamiselle. Ohjelma lukee syötteen ja salausavaimen tiedostosta, prosessoi sen
ja tallentaa tuloksen toiseen tiedostoon.

# Aika- ja tilavaativuus
## Tavoitteellinen vaativuus
DES ja useat muut salausalgoritmit toimivat aika- ja tilavaativuudella O(n), missä n 
on käsiteltävän datan suuruus. O(n) on tavoitteena myös tässä toteutuksessa. 

## Vaativuuksien testaus ja analysointi
Vertaan oman toteutukseni suorituskykyä javan valmiiden kirjastojen toteutuksiin.
