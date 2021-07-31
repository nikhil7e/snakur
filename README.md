# Snákur

Snákur er einfaldur leikur sem forritaður er í Java, með hjálp JavaFX (og örlitlu CSS). Í eins leikmanna útgáfunni reynir leikmaður að borða fæðu sem birtist á leiksvæði, án þess að rekast á eitursnáka. Snákur hefur að auki tvær aðrar útgáfur og stillingar fyrir hvert þeirra.


## Virkni

Inngangsglugginn sem birtist þegar leikurinn er opnaður hefur 3 hnappa: 1 leikmaður, 2 leikmenn og Tank. Hér fyrir neðan lýsir teiknimyndasaga verkefninu "Spila eins leikmanna leik".
![Imgur](https://i.imgur.com/ZrjQpSq.jpg)

### 1 leikmaður:
Stillingar opnast þegar ýtt er á hnappinn. Notandi getur fengið upplýsingar í gegnum
tool-tip um hverja stillingu með því að færa músina yfir viðeigandi hnapp. Þar er hægt að velja
erfiðleikastig, annaðhvort létt, miðlungs eða erfitt. Stigin hafa áhrif á hraða snáks, líkur þess að eitursnákur birtist og líkur þess að eitursnákur skipti um stefnu. Erfiðleikastigin eru valin með því að ýta á viðeigandi
hnapp. Einnig er hægt að velja Classic Snake útgáfu, þar sem veggir eru eitraðir og engir eitursnákar
birtast. Ef ýtt er á áfram birtist leikborðið og leikur er spilaður. Hægt er að ýta á til baka hnapp í öllum
stillingargluggum til að fara aftur í inngangsglugga.

### 2 leikmenn:
Stillingar opnast líkt og áður. Hægt er að velja með textfield hversu mikinn mat
leikmaður 1 þarf að borða til þess að vinna. Einnig er hægt að ýta á hnapp til þess að hafa eitraða
veggi. Leikborð birtist ef ýtt er á áfram eins og venjulega. Leikmaður 1 birtist vinstra megin en
leikmaður 2 hægra megin. Til þess að koma í veg fyrir að eitursnákur geti haldið sig á/mjög nálægt
fæðu og þannig teft leikinn eru tvær fæður á leikborði á hverjum tíma.

![Imgur](https://i.imgur.com/a0rDhaD.png)
*Stillinga gluggi fyrir tveggja leikmanna útgáfu*

![Imgur](https://i.imgur.com/bXOP68c.png)
*Tveggja leikmanna útgáfa spiluð*

### Tank:
Stillingar opnast líkt og áður með tool-tips. Hér er hægt að velja hversu mörg líf leikmenn hafa
með textfield, þ.e. hversu oft leikmaður skal vera skotinn áður en hann tapar. Einnig er hægt að velja
hvort veggir séu eitraðir. Leikmaður 1 birtist í vinstra efra horn leikborðsins og leikmaður 2 í hægra
neðra horninu. Leikmaður getur skotið tvisvar í röð með stuttu millibili, en skot leikmanns mega í
mesta lagi vera tvö á leikborði á hverjum tíma. Skot leikmanns meiða aðeins mótherja. Skotin skoppa
einu sinni af vegg og hverfa þegar þau snerta næst vegg. Þegar leikmaður er skotinn verður snákur
hans dökkrauður í hálfa sekúndu. Viðmótshlutum sem tengjast stigum er sleppt í þessari útgáfu.

![Imgur](https://i.imgur.com/lUuJq6V.png)
*Stillinga gluggi fyrir Tank útgáfu*

![Imgur](https://i.imgur.com/E5HDvB1.png)
*Tank leikmanna útgáfa spiluð*
