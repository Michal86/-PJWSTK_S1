# TPO


### **FILE_CHANNELS**

> Zadanie 1: kanały plikowe
	Katalog {user.home}/TPO1dir  zawiera pliki tekstowe umieszczone w tym katalogu i jego różnych podkatalogach. Kodowanie plików to Cp1250.

	Przeglądając rekursywnie drzewo katalogowe, zaczynające się od {user.home}/TPO1dir,  wczytywać te pliki i dopisywać ich zawartości do pliku o nazwie TPO1res.txt, znadującym się w katalogu projektu. Kodowanie pliku TPO1res.txt winno być UTF-8, a po każdym uruchomieniu programu plik ten powinien zawierać tylko aktualnie przeczytane dane z  plików katalogu/podkatalogow.

---

### **WEBCLIENTS**

> Zadanie 2: klienci usług sieciowych
	Napisać aplikację, udostępniającą GUI, w którym po podanu miasta i nazwy kraju pokazywane są:
	Informacje o aktualnej pogodzie w tym mieście.
	Informacje o kursie wymiany walutu kraju wobec podanej przez uzytkownika waluty.
	Informacje o kursie NBP złotego wobec tej waluty podanego kraju.
	Strona wiki z opisem miasta.
	W p. 1 użyć serwisu api.openweathermap.org; 
	W p. 2 - serwisu exchangeratesapi.io;
	W p. 3 - informacji ze stron NBP: http://www.nbp.pl/kursy/kursya.html i http://www.nbp.pl/kursy/kursyb.html;
	W p. 4 użyć klasy WebEngine z JavaFX dla wbudowania przeglądarki w aplikację Swingową.
