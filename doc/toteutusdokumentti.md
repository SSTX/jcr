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
1. __nBitByteArray__
    * Aikavaativuus O(1). Metodissa on vain vakioiakaisia operaatioita.
    * Tilavaativuus O(n), missä n on parametrin bits suuruus. Metodista palautetaan
    uusi taulukko, jonka pituus on n.
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
7. __konstruktori: O(1).__ Kaksi vakioaikaista metodikutsua. Selvästi O(1).

### DES-luokan metodit
1. __expand: O(1).__ Metodissa luodaan vakiokokoinen taulukko, joka annetaan
aikavaativuuden kannalta merkitsevänä parametrian __permuteBits__:lle. Siis metodin
aikavaativuus on O(1).
2. __initialPermutation: O(1).__ Kuten edellinen.
3. __finalPermutation: O(1).__ Kuten edellinen.
4. __permutationP: O(1).__ Kuten edellinen.
5. __substitute: O(1).__ Vain vakioaikaisia operatioita.
6. __feistelFunction: O(n), missä n on pienin taulukoiden data, subkey pituuksista.__
Taulukon block laskemisen aikavaativuus tunnetaan, se on O(n). Tämä taulukko on 
pituudeltaan n. Se annetaan parametrina __chBitsPerByte__:lle, joten tämän operaation
aikavaativuus on O(n). Taulukko subs on vakiopituinen, joten muut operaatiot ovat
aikavaativuudeltaan O(1). Koko metodin aikavaativuus on siis O(n) + O(n) + O(1) = O(n).
7. __process: O(1).__ Havaitaan, että taulukot permutedInput, left, right ovat 
vakiopituisia. Silmukkaa ajetaan 16 kertaa, sen sisällä on kutsu __feistelFunction__:n
ja __bitwiseXOR__:n. Koska taulukko right on vakiokokoinen, __feistelFunction__:n 
aikavaativuus on tässä O(1). Tiedetään, että sen palauttama taulukko on vakiopituinen.
Siten myös __bitwiseXOR__:n aikavaativuus on tässä O(1). Näin ollen metodin aikavaativuus
on O(1).
8. __encrypt: O(1).__ Kumpikin kutsutuista metodeista on aikavaativuudeltaan O(1).
9. __decrypt: O(1).__ Kuten edellinen.
10. __getKeySchedule: O(1).__ Selvästi vakioaikainen.
11. __konstruktori: O(1).__ Selvästi vakioaikainen.

### ECB-luokan metodit
1. __encrypt: O(n), missä n on taulukon data pituus.__ Edellä esitetyn perusteella
__padBytes__:n aikavaativuus on tässä O(n). Sen paluuarvo on taulukko, jonka pituus
on vakiomäärän suurempi kuin syötteenä annetun taulukon. Tämä paluuarvo syötetään
__makeBlocks__:lle, jonka aikavaativuus on siis myös O(n). Saatu paluuarvo on pituudeltaan
O(n), koska __makeBlocks__ jakaa parametrinaan saamansa taulukon vakiokokoisiin osiin,
O(n / d) = O(n), jos d on vakio. Silmukkaa ajetaan siis O(n) kertaa ja sen sisällä
kutsutaan DES:n __encrypt__-metodia, jonka aikavaativuudeksi tiedetään O(1). Lopuksi
kutsutaan metodia __unmakeBlocks__. Sen aikavaativuus on O(n), koska blocks-taulukon
(rivien määrä) * (sarakkeiden määrä) = n. Siis koko metodin aikavaativuus on O(n).
2. __decrypt: O(n), missä n on taulukon data pituus.__ Kuten edellä, tässäkin
__makeBlocks__:lle annetaan O(n) kokoinen taulukko. DES:n __decrypt__-metodin 
aikavaativuus on O(1). Tiedetään, että __unmakeBlocks__ palautaa taulukon, jonka
pituus on O(n). Siten __unpadBytes__:n aikavaativuus on tässä O(n). Koko metodin 
aikavaativuus on siis O(n).


### Yhteenveto
Nähtiin, että ECB:n metodien __encrypt__ ja __decrypt__ aikavaativuudet ovat O(n),
missä n parametrina annetun taulukon pituus. Tämä taulukko on salattava/purettava data,
joten salausalgoritmin aikavaativuus on O(m), missä m on salattavien/purettavien 
tavujen määrä.

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
