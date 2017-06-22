# Ohjelman rakenne
Ohjelmaa käytetään komentoriviltä. Argumentteina annetaan tiedostojen sijainnit 
ja muut tarvittavat tiedot, jotka välitetään Cryptographer-oliolle. Se hoitaa 
tiedostojen lukemisen, sekä algoritmin ja toimintatavan valinnan. Luettu tieto 
annetaan seuraavaksi ModeOfOperation-luokan perivälle oliolle. Se lisää käsiteltävään
dataan täytettä niin, että se on käytettävän algoritmin lohkokoon monikerta.
Sitten data annetaan lohko kerrallaan salausalgoritmille ja lopuksi se palautetaan
 Cryptographer:lle joka kirjoittaa sen tiedostoon.

# Aika- ja tilavaativuudet

## Kokeelliset tulokset

ECB-DES näyttää toimivan aikavaativuudella O(n), missä n on salattavan/purettavan datan koko
(ks. [testausdokumentti](testausdokumentti.md)).

## Analyysi
Tarkastellaan lähemmin ohjelman aika- ja tilavaativuutta.

### BitFunctions-luokan metodit
1. __nBitByteArray__
    * Aikavaativuus O(1). Metodissa on vain vakioiakaisia operaatioita.
    * Tilavaativuus O(n), missä n on parametrin bits suuruus. Metodista palautetaan
uusi taulukko, jonka pituus on O(n).

2. __getBitByOffset__ 
    * Aikavaativuus O(1). Metodissa on vain vakioaikaisia operaatioita.
    * Tilavaativuus O(1). Metodissa on vain vakiotilaisia operaatioita.

3. __insertBit__
    * Aikavaativuus O(1). Metodissa on vain vakioaikaisia operaatioita.
    * Tilavaativuus O(1). Metodissa on vain vakiotilaisia operaatioita.

4. __permuteBits__ 
    * Aikavaativuus O(n), missä n on permTable-taulukon pituus. Metodissa on yksi
silmukka, joka ajetaan kerran jokaista permTable:n alkiota kohti. Silmukassa on vain
vakioaikaisia operaatioita.
    * Tilavaativuus O(n), missä n on permTable-taulukon pituus. Metodissa kutsutaan
__nBitByteArray__:a, joka palauttaa n-pituisen taulukon. Muut operaatiot ovat 
vakiotilaisia.

5. __rotateLeft__ 
    * Aikavaativuus O(n), missä n on kokonaisluvun lengthInBits suuruus. Metodissa
käydään alkio alkiolta läpi taulukko, jonka pituus on lengthInBits. Tähän kuluu
aikaa O(n). Luotu taulukko annetaan parametrina __permuteBits__:lle, jonka aikavaativuus
on yllä esitetyn nojalla lineaarinen taulukon koon suhteen.
    * Tilavaativuus O(n), missä n on kokonaisluvun lengthInBits suuruus. Metodissa luodaan
taulukko, jonka pituus on n.

6. __concatBits__
    * Aikavaativuus O(n + m), missä n, m ovat kokonaisluvut nBitsLeft, nBitsRight.
 Metodissa on kaksi silmukkaa, toinen ajetaan n kertaa, toinen m kertaa.
Silmukoiden sisällä kutsutaan __getBitByOffset__:a ja __insertBit__:a, joiden
aikavaativuudet ovat O(1). Siis aikavaativuus on n * 2 * O(1) + m * 2 * O(1) = O(n + m).
    * Tilavaativuus O(n + m), missä n, m kuten edellä.
Kutsutaan __nBitByteArray__:a parametrilla n+m, mikä palauttaa (n+m) -pituisen taulukon.

7. __copyBits__
    * Aikavaativuus O(m - n), missä n on kokonaisluvun startInclusive suuruus, ja m on
kokonaisluvun endExclusive suuruus. Metodissa on yksi silmukka, joka ajetaan 
m - n kertaa, jos m - n > 0. Silmukan sisällä on kutsu __getBitByOffset__:n, jonka
aikavaativuus on O(1), ja kaksi selvästi vakioaikaista operaatiota. Siis metodin
aikavaativuus on O(n - m) * O(1) = O(n - m).
    * Tilavaativuus O(m - n), missä n ja m kuten edellä. Kutsutaan __nBitByteArray__:a
parametrilla m - n, mikä palauttaa (m-n) -pituisen taulukon.

8. __bitwiseXOR__
    * Aikavaativuus O(n), missä n on taulukoiden arr1, arr2 pituuksista lyhyempi.
Metodissa on vakioaikaisten operaatioiden lisäksi kutsu __copyBits__:n ja silmukka,
joka ajetaan n kertaa. CopyBits:lle annetaan parametreina 0, 8*n ja taulukko arr1, 
joten sen aikavaativuus on tässä O(8n - 0) = O(n). Silmukan sisällä on yksi vakioaikainen
operaatio, joten sen aikavaativuus on O(n). Siis metodin aikavaativuus on O(n).
    * Tilavaativuus O(n), missä n kuten edellä. Kutsutaan __copyBits__:a parametrein
0, 8n ja arr1, mikä palauttaa (8n - 0) -pituisen taulukon. O(8n - 0) = 0(n).

9. __inPlaceBitwiseXOR__
    * Aikavaativuus O(n), missä n on taulukoiden arr1, arr2 pituuksista lyhyempi. Metodissa
olevaa silmukkaa ajetaan n kertaa, ja sen sisällä olevat operaatiot ovat vakioaikaisia.
    * Tilavaativuus O(1). Metodissa on vain vakiotilaisia operaatioita.

10. __chBitsPerByte__ 
    * Aikavaativuus O(n), missä n on taulukon source pituus. Ennen silmukkaa 
suoritettavat operaatiot ovat vakioaikaisia. Havaitaan, että silmukassa kasvatetaan
sourceIdx:a ja targetIdx:a joka kierroksella vähintään yhdellä. Lisäksi havaitaan,
että sourceIdx on alussa vähintään 0. Siten silmukka ajetaan korkeintaan 8n - 0 = O(n)
kertaa. Silmukan sisällä on vakioaikaisia operaatioita, mukaanlukien kutsut __getBitByOffset__:n
ja __insertBit__:n. Siis silmukan ja koko metodin aikavaativuus on O(n).
    * Tilavaativuus O(n), missä n on taulukon source pituus. Huomataan, että currentBitCount
ja targetBitCount ovat vakioita. Siten metodissa luotu taulukko arr on pituudeltaan
O(n). Muut operaatiot ovat vakiotilaisia.


### ModeOfOperation-luokan metodit

1. __padBytes__ 
    * Aikavaativuus O(n), missä n on taulukon bytes pituus. Huomataan, että muuttuja padNeed on vakio. 
Ensimmäinen silmukka toimii selvästi ajassa O(n). Toinen silmukka ajetaan korkeintaan
n + padNeed kertaa. Koska padNeed on vakio, on tämänkin silmukan aikavaativuus O(n).
Metodin aikavaativuus on 2 * O(n) = O(n).
    * Tilavaativuus O(n), missä n kuten edellä. Koska muuttuja padNeed on vakio, metodissa luotu
taulukko paddedBytes on pituudeltaan O(n).

2. __unpadBytes__ 
    * Aikavaativuus O(n), missä n on taulukon bytes pituus. Huomataan, että muuttuja
pads on vakio (korkeintaan 255 = O(255) = O(1)). Silmukka ajetaan siis O(n) - O(1) = O(n)
kertaa, ja sen sisällä on yksi vakioaikainen operaatio. Siten silmukan ja koko metodin
aikavaativuus on O(n) * O(1) = O(n).
    * Tilavaativuus O(n), missä n kuten edellä. Koska muuttuja pads on vakio, metodissa
luotu taulukko unpad on pituudeltaan O(n).

3. __makeBlocks__ 
    * Aikavaativuus O(n), missä n on taulukon bytes pituus. Huomataan, että 
blockNum * blockSize = n. Metodissa on kaksi sisäkkäistä silmukkaa, ulompi ajetaan
blockNum kertaa, sisempi blockSize kertaa. Silmukoiden sisällä on vain vakioaikaisia
operaatioita. Silmukoiden ja koko metodin aikavaativuus on siis 
O(blockNum * blockSize) * O(1) = O(n).
    * Tilavaativuus O(n), missä n kuten edellä. Nähdään, että 2-ulotteisen taulukon
blocks koko on n.

4. __unmakeBlocks__
    * Aikavaativuus O(n*m), missä n on blocks-taulukon rivimäärä ja m sarakemäärä.
Metodissa on kaksi sisäkkäistä silmukkaa. Ulompi ajetaan n kertaa, sisempi m kertaa.
Silmukoiden ja koko metodin aikavaativuus on siten O(n*m).
    * Tilavaativuus O(n*m), missä n ja m kuten edellä. Metodissa luodaan taulukko, jossa
rivejä n ja sarakkeita m.

5. __konstruktori__ 
    * Aikavaativuus O(1). Vain vakioaikaisia operaatioita.
    * Tilavaativuus O(1). Vain vakiotilaisia operaatioita.

### DESKeySchedule-luokan metodit
1. __init__ 
    * Aikavaativuus O(1). Metodissa on kaksi kutsua __copyBits__:n. Sen aikavaativuus on
tässä O(1), koska merkitsevät parametrit ovat vakioita.
    * Tilavaativuus O(1). Kutsu __copyBits__:n palauttaa tässä vakiopituisen taulukon.

2. __permutedChoice1__ 
    * Aikavaativuus O(1). Metodissa luodaan vakiokokoinen taulukko, joka annetaan
parametrina __permuteBits__:lle. Siis aikavaativuus on O(1).
    * Tilavaativuus O(1). Kutsu __permuteBits__:n palauttaa tässä vakiokokoisen taulukon.

3. __permutedChoice2__
    * Aikavaativuus O(1). Kuten __permutedChoice1__.
    * Tilavaativuus O(1). Kuten __permutedChoice1__.

4. __nLeftShift__ 
    * Aikavaativuus O(1). Selvästi vakioaikainen.
    * Tilavaativuus O(1). Selvästi vakiotilainen.

5. __encryptionSubKeys__ 
    * Aikavaativuus O(1). Metodissa luodaan vakiokokoinen taulukko keys.
Seuraavaksi ajetaan silmukkaa 16 kertaa. Silmukan sisällä on kutsuja __rotateLeft__:n
ja __concatBits__:n. Huomataan, että aikavaativuuden kannalta merkitsevät parametrit
ovat vakioita, siis näiden aikavaativuus on O(1). Lisäksi joitain vakioaikaisia
operaatioita. Aikavaativuus on siis O(1).
    * Tilavaativuus O(1). Luodaan 2-ulotteinen taulukko keys, jossa vakiomäärä rivejä. 
Tässä __rotateLeft__ palauttaa vakiopituisen taulukon, koska sen toinen parametri 
(lengthInBits) on vakio. Siis keys:n sarakemäärä on vakio ja metodin tilavaativuus
O(1).

6. __decryptionSubKeys__ 
    * Aikavaativuus O(1). Metodissa kutsutaan __encryptionSubKeys__:a, jonka
aikavaativuus on O(1). Saadun taulukon alkiot järjestetään vastakkaiseen järjestykseen
aikavaativuudella O(1). Siis metodin aikavaativuus on O(1).
    * Tilavaativuus O(1). Metodissa luodaan kaksi vakiokokoista taulukkoa, toinen suoraan
ja toinen __encryptionSubKeys__:n kautta. Tilavaativuus on vakio.

7. __konstruktori__ 
    * Aikavaativuus O(1). Kaksi vakioaikaista metodikutsua. Selvästi O(1).
    * Tilavaativuus O(1). Kaksi vakiotilaista metodikutsua. Selvästi O(1).

### DES-luokan metodit
1. __expand__
    * Aikavaativuus O(1). Metodissa luodaan vakiokokoinen taulukko, joka annetaan
aikavaativuuden kannalta merkitsevänä parametrian __permuteBits__:lle. Siis metodin
aikavaativuus on O(1).
    * Tilavaativuus O(1). Luodaan vakiokokoinen taulukko, ja annetaan se parametrina
__permuteBits__:lle, jonka tilavaativuus on tässä O(1).

2. __initialPermutation__
    * Aikavaativuus O(1). Kuten edellinen.
    * Tilavaativuus O(1). Kuten edellinen.

3. __finalPermutation__
    * Aikavaativuus O(1). Kuten edellinen.
    * Tilavaativuus O(1). Kuten edellinen.

4. __permutationP__
    * Aikavaativuus O(1). Kuten edellinen.
    * Tilavaativuus O(1). Kuten edellinen.

5. __substitute__
    * Aikavaativuus O(1). Vain vakioaikaisia operaatioita.
    * Tilavaativuus O(1). Vain vakiotilaisia operaatioita.

6. __feistelFunction:__
    * Aikavaativuus  O(n), missä n on pienin taulukoiden data, subkey pituuksista.
Taulukon block laskemisen aikavaativuus tunnetaan, se on O(n). Tämä taulukko on 
pituudeltaan n. Se annetaan parametrina __chBitsPerByte__:lle, joten tämän operaation
aikavaativuus on O(n). Taulukko subs on vakiopituinen, joten muut operaatiot ovat
aikavaativuudeltaan O(1). Koko metodin aikavaativuus on siis O(n) + O(n) + O(1) = O(n).
    * Tilavaativuus O(n), missä n on taulukon data pituus. Metodissa on kaksi kutsua
__chBitsPerByte__:n, jonka tilavaativuudeksi tiedetään O(n).

7. __process__
    * Aikavaativuus O(1). Havaitaan, että taulukot permutedInput, left, right ovat 
vakiopituisia. Silmukkaa ajetaan 16 kertaa, sen sisällä on kutsu __feistelFunction__:n
ja __bitwiseXOR__:n. Koska taulukko right on vakiokokoinen, __feistelFunction__:n 
aikavaativuus on tässä O(1). Tiedetään, että sen palauttama taulukko on vakiopituinen.
Siten myös __bitwiseXOR__:n aikavaativuus on tässä O(1). Näin ollen metodin aikavaativuus
on O(1).
    * Tilavaativuus O(1). Koska permutedInput, left, right ovat vakiopituisia, on
__feistelFunction__:n palauttama taulukko myös vakiopituinen. Lopussa kutsutaan
__concatBits__:a vakioparametrein, joten tämäkin operaatio on vakiotilainen.

8. __encrypt__
    * Aikavaativuus O(1). Kumpikin kutsutuista metodeista on aikavaativuudeltaan O(1).
    * Tilavaativuus O(1). Tiedetään, että __encryptionSubKeys__ palauttaa vakiopituisen
taulukon, ja että __process__ on vakiotilainen.

9. __decrypt__
    * Aikavaativuus O(1). Kuten edellinen.
    * Tilavaativuus O(1). Kuten edellinen.

10. __getKeySchedule__
    * Aikavaativuus O(1). Selvästi vakioaikainen.
    * Tilavaativuus O(1). Selvästi vakiotilainen.

11. __konstruktori__
    * Aikavaativuus O(1). Selvästi vakioaikainen.
    * Tilavaativuus O(1). Selvästi vakiotilainen.

### ECB-luokan metodit
1. __encrypt__
    * Aikavaativuus O(n), missä n on taulukon data pituus. Edellä esitetyn perusteella
__padBytes__:n aikavaativuus on tässä O(n). Sen paluuarvo on taulukko, jonka pituus
on vakiomäärän suurempi kuin syötteenä annetun taulukon. Tämä paluuarvo syötetään
__makeBlocks__:lle, jonka aikavaativuus on siis myös O(n). Saatu paluuarvo on pituudeltaan
O(n), koska __makeBlocks__ jakaa parametrinaan saamansa taulukon vakiokokoisiin osiin,
O(n / d) = O(n), jos d on vakio. Silmukkaa ajetaan siis O(n) kertaa ja sen sisällä
kutsutaan DES:n __encrypt__-metodia, jonka aikavaativuudeksi tiedetään O(1). Lopuksi
kutsutaan metodia __unmakeBlocks__. Sen aikavaativuus on O(n), koska blocks-taulukon
(rivien määrä) * (sarakkeiden määrä) = n. Siis koko metodin aikavaativuus on O(n).
    * Tilavaativuus O(n), missä n on taulukon data pituus. Luodaan uusi taulukko blocks, 
jonka pituus on O(n).

2. __decrypt__
    * Aikavaativuus O(n), missä n on taulukon data pituus. Kuten edellä, tässäkin
__makeBlocks__:lle annetaan O(n) kokoinen taulukko. DES:n __decrypt__-metodin 
aikavaativuus on O(1). Tiedetään, että __unmakeBlocks__ palautaa taulukon, jonka
pituus on O(n). Siten __unpadBytes__:n aikavaativuus on tässä O(n). Koko metodin 
aikavaativuus on siis O(n).
    * Tilavaativuus O(n), missä n on taulukon data pituus. Kuten edellä.


### Yhteenveto
Nähtiin, että ECB:n metodien __encrypt__ ja __decrypt__ aika- ja tilavaativuudet ovat O(n),
missä n parametrina annetun taulukon pituus. Nämä metodit saavat syötteenään
salattavan/purettavan datan kokonaisuudessaan, joten ohjelma toimii aika- ja tilavaativuudella
O(m), missä m on salattavan/purettavan tiedoston koko tavuina.

# Ongelmia ja mahdollisia ratkaisuja
Tilavaativuuden voisi mahdollisesti laskea vakioksi lukemalla ja käsittelemällä dataa vain tietty määrä kerrallaan.
ECB-DES toimii todella hitaasti verrattuna javan valmiisiin kirjastoihin (ks. [testausdokumentti](testausdokumentti.md)).
Suorituskykyä voisi ehkä parantaa muuttamalla taulukonkäsittelymetodeita siten, että mahdollisimman paljon operaatioita
tehdään jo olemassaolevaan, parametrina annettuun taulukkoon. Tällä hetkellä kaikki metodit luovat ja palauttavat uuden taulukon.

# Lähteet
## Data Encryption Standard (DES)
* https://en.wikipedia.org/wiki/Data_Encryption_Standard
* https://en.wikipedia.org/wiki/DES_supplementary_material
* [DES:n määritelmä](http://csrc.nist.gov/publications/fips/fips46-3/fips46-3.pdf)
* [The DES Algorithm illustrated](http://page.math.tu-berlin.de/~kant/teaching/hess/krypto-ws2006/des.htm)
