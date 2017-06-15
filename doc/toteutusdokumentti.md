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

ECB-DES näyttää toimivan aikavaativuudella O(n), missä n on salattavan/purettavan datan koko
(ks. [testausdokumentti](testausdokumentti.md)).

## Analyysi
Tarkastellaan lähemmin algoritmin aikavaativuutta.

### BitFunctions-luokan metodit
1. __nBitByteArray: O(1).__ Metodissa on vain vakioiakaisia operaatioita.
2. __getBitByOffset: O(1).__ Metodissa on vain vakioaikaisia operaatioita.
3. __insertBit: O(1).__ Metodissa on vain vakioaikaisia operaatioita.
4. __permuteBits: O(n), missä n on permTable-taulukon pituus.__ Metodissa on yksi
silmukka, joka ajetaan kerran jokaista permTable:n alkiota kohti. Silmukassa on vain
vakioaikaisia operaatioita.
5. __rotateLeft: O(n), missä n on kokonaisluvun lengthInBits suuruus.__ Metodissa
käydään alkio alkiolta läpi taulukko, jonka pituus on lengthInBits. Tähän kuluu
aikaa O(n). Luotu taulukko annetaan parametrina __permuteBits__:lle, jonka aikavaativuus
on yllä esitetyn nojalla lineaarinen taulukon koon suhteen.
6. __concatBits: O(n + m), missä n, m ovat kokonaislukujen nBitsLeft, nBitsRight
suuruudet.__ Metodissa on kaksi silmukkaa, toinen ajetaan n kertaa, toinen m kertaa.
Silmukoiden sisällä kutsutaan __getBitByOffset__:a ja __insertBit__:a, joiden
aikavaativuudet ovat O(1). Siis aikavaativuus on n * 2 * O(1) + m * 2 * O(1) = O(n + m).
7. __copyBits: O(m - n), missä n on kokonaisluvun startInclusive suuruus, ja m on
kokonaisluvun endExclusive suuruus.__ Metodissa on yksi silmukka, joka ajetaan 
m - n kertaa, jos m - n > 0. Silmukan sisällä on kutsu __getBitByOffset__:n, jonka
aikavaativuus on O(1), ja kaksi selvästi vakioaikaista operaatiota. Siis metodin
aikavaativuus on O(n - m) * O(1) = O(n - m).
8. __bitwiseXOR: O(n), missä n on taulukoiden arr1, arr2 pituuksista lyhyempi.__
Metodissa on vakioaikaisten operaatioiden lisäksi kutsu __copyBits__:n ja silmukka,
joka ajetaan n kertaa. CopyBits:lle annetaan parametreina 0, 8*n ja taulukko arr1, 
joten sen aikavaativuus on tässä O(8n - 0) = O(n). Silmukan sisällä on yksi vakioaikainen
operaatio, joten sen aikavaativuus on O(n). Siis metodin aikavaativuus on O(n).
9. __bitRepresentation: O(n), missä n on taulukon data pituus.__ Analyysi ohitetaan,
koska metodia ei käytetä salausalgoritmissa.
10. __chBitsPerByte: O(n), missä n on taulukon source pituus.__ Ennen silmukkaa 
suoritettavat operaatiot ovat vakioaikaisia. Havaitaan, että silmukassa kasvatetaan
sourceIdx:a ja targetIdx:a joka kierroksella vähintään yhdellä. Lisäksi havaitaan,
että sourceIdx on alussa vähintään 0. Siten silmukka ajetaan korkeintaan 8n - 0 = O(n)
kertaa. Silmukan sisällä on vakioaikaisia operaatioita, mukaanlukien kutsut __getBitByOffset__:n
ja __insertBit__:n. Siis silmukan ja koko metodin aikavaativuus on O(n).


### ModeOfOperation-luokan metodit

1. __padBytes: O(n), missä n on taulukon bytes pituus.__ Huomataan, että muuttuja padNeed on vakio. 
Ensimmäinen silmukka toimii selvästi ajassa O(n). Toinen silmukka ajetaan korkeintaan
n + padNeed kertaa. Koska padNeed on vakio, on tämänkin silmukan aikavaativuus O(n).
Metodin aikavaativuus on 2 * O(n) = O(n).
2. __unpadBytes: O(n), missä n on taulukon bytes pituus.__ Huomataan, että muuttuja
pads on vakio (korkeintaan 255 = O(255) = O(1)). Silmukka ajetaan siis O(n) - O(1) = O(n)
kertaa, ja sen sisällä on yksi vakioaikainen operaatio. Siten silmukan ja koko metodin
aikavaativuus on O(n) * O(1) = O(n).
3. __makeBlocks: O(n), missä n on taulukon bytes pituus.__ Huomataan, että 
blockNum * blockSize = n. Metodissa on kaksi sisäkkäistä silmukkaa, ulompi ajetaan
blockNum kertaa, sisempi blockSize kertaa. Silmukoiden sisällä on vain vakioaikaisia
operaatioita. Silmukoiden ja koko metodin aikavaativuus on siis 
O(blockNum * blockSize) * O(1) = O(n).
4. __unmakeBlocks: O(n*m), missä n on blocks-taulukon rivimäärä ja m sarakemäärä.__
Metodissa on kaksi sisäkkäistä silmukkaa. Ulompi ajetaan n kertaa, sisempi m kertaa.
Silmukoiden ja koko metodin aikavaativuus on siten O(n*m).
5. __konstruktori: O(1).__ Vain vakioaikaisia operaatioita.

### DESKeySchedule-luokan metodit
1. __init: O(1).__ Metodissa on kaksi kutsua __copyBits__:n. Sen aikavaativuus on
tässä O(1), koska merkitsevät parametrit ovat vakioita.
2. __permutedChoice1: O(1).__ Metodissa luodaan vakiokokoinen taulukko, joka annetaan
parametrina __permuteBits__:lle. Siis aikavaativuus on O(1).
3. __permutedChoice2: O(1).__ Kuten edellinen.
4. __nLeftShift: O(1).__ Selvästi vakioaikainen.
5. __encryptionSubKeys: O(1).__ Metodissa luodaan vakiokokoinen taulukko keys.
Seuraavaksi ajetaan silmukkaa 16 kertaa. Silmukan sisällä on kutsuja __rotateLeft__:n
ja __concatBits__:n. Huomataan, että aikavaativuuden kannalta merkitsevät parametrit
ovat vakioita, siis näiden aikavaativuus on O(1). Lisäksi joitain vakioaikaisia
operaatioita. Aikavaativuus on siis O(1).
6. __decryptionSubKeys: O(1).__ Metodissa kutsutaan __encryptionSubKeys__:a, jonka
aikavaativuus on O(1). Saadun taulukon alkiot järjestetään vastakkaiseen järjestykseen
aikavaativuudella O(1). Siis metodin aikavaativuus on O(1)
7. __konstruktori: O(1).__ 

### DES-luokan metodit


### ECB-luokan metodit

Aloitetaan pisteestä, jossa kutsutaan ECB-luokan metodia encrypt. Olkoon n taulukon
data pituus. Yllä esitetyn perusteella nähdään, että blocks-taulukon laskemisen
aikavaativuus on O(n). Seuraavaksi metodissa on silmukka, joka ajetaan O(n) kertaa.
Tämä johtuu siitä, että makeBlocks-metodi jakaa data-taulukon vakiokokoisiin osiin
(riveihin). O(n / d) = O(n), missä d on vakio. Silmukan sisällä kutsutaan DES:n
metodia encrypt.

Ensimmäisellä rivillä kutsutaan keySchedulen metodia encryptionSubkeys. 
Se kutsuu apumetodeja rotateLeft, concatBits ja permutedChoice2. Helposti nähdään, 
että niiden aikavaativuudet ovat O(n), missä n on syötteinä annettujentaulukoiden 
yhteispituus.Apumetodeja kutsutaan vakiomäärä kertoja vakiopituisilla syötteillä,
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
