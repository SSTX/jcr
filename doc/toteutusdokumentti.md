# Ohjelman rakenne
Ohjelmalla on yksinkertainen teksipohjainen käyttöliittymä (luokka TextInterface). 
Käyttäjä syöttää sen avulla tarvittavat tiedot salaukseen/purkamiseen, ja ne välitetään
Cryptographer-oliolle. Se hoitaa tiedostojen lukemisen, sekä algoritmin ja toimintatavan valinnan.
Luettu tieto annetaan seuraavaksi ModeOfOperation-luokan perivälle oliolle. Se lisää käsiteltävään
dataan täytettä niin, että se on käytettävän algoritmin lohkokoon monikerta. Sitten data annetaan 
lohko kerrallaan salausalgoritmille ja lopuksi se palautetaan Cryptographer:lle joka kirjoittaa
sen tiedostoon.

# Aika- ja tilavaativuudet

## Kokeelliset tulokset

ECB-DES toimii aikavaativuudella O(n), missä n on salattavan/purettavan datan koko
(ks. [testausdokumentti](testausdokumentti.md)).

## Analyysi
Tarkastellaan lähemmin aikavaativuutta. Aloitetaan pisteestä, jossa kutsutaan ECB:n metodia
encrypt. Metodin ensimmäisellä rivillä kutsutaan kahta apumetodia: padBytes ja makeBlocks.

### Syötteen esikäsittely

__padBytes:__ olkoon n syötteen bytes[] pituus. Metodissa on kaksi silmukkaa ja taulukon
luonti, muuten operaatiot ovat vakioaikaisia. Huomataan, että muuttuja padNeed on vakio. 
Siten kumpikin silmukka toimii ajassa O(n), ja koko metodin aikavaativuus on O(n).
Uuden taulukon luomisen takia tilavaativuus on O(n).

__makeBlocks:__ Olkoon n syötteen bytes[] pituus. Nähdään, että blockNum * blockSize = n.
Siten uuden taulukon blocks[][] koko tavuina on O(n), ja kahden sisäkkäisen silmukan aikavaativuus
on O(n). Tässäkin siis metodin aika- ja tilavaativuus on O(n).

### Varsinainen salausalgoritmi

Seuraavaksi siirrytään silmukkaan, jossa kutsutaan joka kierroksella DES:n metodia
encrypt. Silmukka ajetaan O(n) kertaa, koska blocks[][] -taulukon pituus on
parametrin data pituus jaettuna vakiolla blockSize.

Tutkitaan seuraavaksi DES:n encrypt-metodia. Ensimmäisellä rivillä kutsutaan keySchedulen
metodia encryptionSubkeys. Se kutsuu apumetodeja rotateLeft, concatBits ja permutedChoice2. 
Helposti nähdään, että niiden aikavaativuudet ovat O(n), missä n on syötteinä annettujen
taulukoiden yhteispituus.Apumetodeja kutsutaan vakiomäärä kertoja vakiopituisilla syötteillä,
joten encryptionSubkeys toimiii ajassa O(1).

Seuraavaksi suoritus siirtyy metodiin process. Koska metodi initialPermutation palauttaa
korkeintaan 8 tavua pitkän taulukon, ovat kaikki muuttujat tässä metodissa vakiokokoisia.
Apumetodi copyBits toimii ajassa O(n), missä n on kahden ensimmäisen parametrin
pituusero, joka on tässä vakio. Enää täytyy tarkastella metodia round, muut toimivat
vakioajassa.

Round:n ensimmäisellä rivillä kutsutaan ensin metodia feistelFunction. Sille annetut
parametrit ovat vakiopituisia. FeistelFunction:n sisällä kaksi ensimmäistä riviä toimivat selvästi
vakioajassa. Tarkastellaan lähemmin substitution ja permutation -osia.

__substitution:__ Ensin kutsutaan metodia chBitsPerByte. Se toimii ajassa O(n),
missä n on syötteenä annetun taulukon pituus. Tässä se on vakio. Metodikutsua
seuraava silmukka ajetaan vakiomäärä kertoja, ja substitute-metodi toimii selvästi
vakioajassa. Tämän osan aikavaativuus on siis O(1).

__permutation:__ Taas kutsutaan chBitsPerByte:a vakiopituisella syötteellä. Lisäksi 
nähdään, että permutationP toimii ajassa O(n), missä n on syötteen pituus. Tässä
n on vakio. Tämänkin osan aikavaativuus on siis O(1).

Saatiin feistelFunction:lle aikavaativuudeksi O(1). Seuraavaksi round:ssa tehdään
vakiopituisten taulukoiden XOR, joka toimii ajassa O(1). ConcatBits toimii tässäkin
vakioajassa. Round toimii siis ajassa O(1), ja siten myös kutsuva metodi process ja
sitä kutsuva metodi (DES:n) encrypt toimivat ajassa O(1).

Palataan ECB:n encrypt-metodiin. Viimeisellä rivillä kutsutaan unmakeBlocks. Nähdään, että
sen aikavaativuus on O(h * j), missä h on syötteenä annetun 2-ulotteisen taulukon 
rivimäärä, ja j sarakemäärä. Kuitenkin tiedetään, että h * j = n, missä n on
encrypt-metodin parametrin data pituus. Siis tämänkin osan aikavaativuus on O(n).

### Yhteenveto
ECB:n metodissa encrypt ensin esikäsitellään syötteenä saatu data. Aikavaativuus on O(n),
missä n on data:n pituus tavuina. Seuraavaksi ajetaan silmukkaa O(n) kertaa, ja joka
kierroksella kutsutaan metodia, jonka aikavaativuus on O(1). Silmukan aikavaativuus on
siis O(n). Lopuksi loppukäsitellään salattu data, aikavaativuus O(n).

ECB:n encrypt-metodin aikavaativuus on siten O(n), missä n on metodiparametrin data
koko tavuina.

# Ongelmia ja mahdollisia ratkaisuja
Tilavaativuuden voisi mahdollisesti laskea vakioksi lukemalla ja käsittelemällä dataa vain tietty määrä kerrallaan.

# Lähteet
## Data Encryption Standard (DES)
* https://en.wikipedia.org/wiki/Data_Encryption_Standard
* https://en.wikipedia.org/wiki/DES_supplementary_material
* [DES:n määritelmä](http://csrc.nist.gov/publications/fips/fips46-3/fips46-3.pdf)
* [The DES Algorithm illustrated](http://page.math.tu-berlin.de/~kant/teaching/hess/krypto-ws2006/des.htm)
