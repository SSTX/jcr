# Yksikkötestaus
Yksikkötestaukseen käytän jUnitia. DES:n testidata on lähtöisin 
[tästä](http://page.math.tu-berlin.de/~kant/teaching/hess/krypto-ws2006/des.htm)
 materiaalista.

# Suorituskykytestaus
Suorituskykytestaukseen on main-paketissa oma luokka, Benchmarking. Sen sisältämä
main-metodi mittaa ECB-DES:n suorittamiseen kuluvaa aikaa eri pituisilla syötteillä.
Argumentteja on kolme, pienin testattavan syötteen koko, suurimman testattavan syötteen
koko ja koon lisäys. Testisyötteen koko kasvaa lineaarisesti pienimmästä suurimpaan
annetuin askelin. Esim "100 1000 100" tuottaa syötteiden pituuksiksi 100, 200, 300,
400, 500, 600, 700, 800, 900, 1000.

Testaustulokset tulostuvat stdout:n. Testaaja sai seuraavat tulokset:

| Syötteen koko tavuina | Kulunut aika sekunteina|
| ---- | ----------- |
| 10E0 | 8.08675E-4 |
| 10E1 | 0.001044103 |
| 10E2 | 0.003404757 |
| 10E3 | 0.014225404 |
| 10E4 | 0.073739221 |
| 10E5 | 0.519957689 |
| 10E6 | 4.943009466 |
| 10E7 | 49.13388077 |

Algoritmi vaikuttaa toimivan ajassa O(n).
