# Yleiskuvaus
Toteutan ohjelman, joka pystyy salaamaan ja purkamaan dataa käyttäen eri salausalgoritmeja.
Algoritmeista toteutan ensiksi DES:n ja tämän jälkeen, jos aikaa jää, jonkin seuraavista:
AES, Twofish, Speck. Valitsin ensimmäiseksi DES:n, koska se on suhteellisen yksinkertainen toteuttaa.
Todelliseen salaukseen se ei ole enää käyttökelpoinen pienen avainpituutensa (56 bittiä) takia.

Käytän toteutuksessa Javaa ja Mavenia.

# Ohjelman toiminta
Ohjelmaa käytetään tekstikäyttöliittymän kautta. Se lukee käsiteltävän datan
joko näppäimistöltä tai tiedostosta, salaa tai purkaa sen riippuen käyttäjän ohjeista 
ja lopuksi tallentaa tuloksen tiedostoon tai tulostaa sen näytölle. Syötteenä annettu 
data voi olla mikä tahansa jono bittejä. 

# Tavoitteelliset aika- ja tilavaativuudet
DES ja useat muut salausalgoritmit toimivat aika- ja tilavaativuudella O(n), missä n 
on käsiteltävän datan suuruus. O(n) on tavoitteena myös tässä toteutuksessa. 

