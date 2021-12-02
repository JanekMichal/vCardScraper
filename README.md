# vCardScraper

API służące do zapisywania listy wyszukanych biznesów na stronie panoramafirm.pl do plików w formacie .vcf

Przykład użycia: 

```
Request:

http://localhost:8080/vCard/hydraulik

Response:

[BEGIN:VCARD
VERSION:4.0
ORG:Hydraulika Montaż Instalacji Sanitarnych i Grzewczych Robert Rosłoniec
TEL:501083795
ADR:ul. Wierzbowa 12 05-503
EMAIL:rrrobert@vp.pl
END:VCARD
, BEGIN:VCARD
VERSION:4.0
ORG:Adam Kołota Udrażnianie rur
TEL:781266854
ADR:ul. Zaciszna 30A 05-230
EMAIL:joanna-kolota@wp.pl
END:VCARD
, BEGIN:VCARD
VERSION:4.0
ORG:Twój Hydraulik Piotr Pajdak
TEL:515511257
ADR:ul. Warzycka 13/1 32-020
EMAIL:kontakt@twojhydraulik.com
END:VCARD
, BEGIN:VCARD
VERSION:4.0
ORG:F.U. Hydro Seb – czyszczenie, udrażnianie, inspekcja kanalizacji, wuko
TEL:502220475
ADR:ul. Częstochowska 64 42-244
EMAIL:hydroseb@op.pl
END:VCARD]
```

Zwracana jest lista wyszukanych biznesów i każdy z biznesów jest zapisywany do folderu GeneratedCards w formie oddzielnego pliku .vcf

```
Przykładowa zawartość pliku: 

BEGIN:VCARD
VERSION:4.0
ORG:Twój Hydraulik Piotr Pajdak
TEL:515511257
ADR:ul. Warzycka 13/1 32-020
EMAIL:kontakt@twojhydraulik.com
END:VCARD
```



