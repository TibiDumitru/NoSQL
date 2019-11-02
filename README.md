# NoSQL

  Program that simulates the implementation of a NoSQL database.
  
  -----------------------------------------------------------------
				      Lost in NoSQL
-----------------------------------------------------------------


*Clase utilizate

 - Tema2 - clasa principala
 - DataBase
 - Node
 - Instance
 - Entity
 - Attribute 
 - DBOperations
 - TimestampComparator

*Implementare

 - In clasa principala, Tema2, in main, se realizeaza citirea comenzilor din fisierul de input si parsarea;

! Pentru toate structurile a fost utilizata clasa ArrayList din Collections.

 - Clasa DataBase - contine doua valori intregi: numarul de noduri si capacitatea maxima a unui nod si o lista de noduri;
			  - constructor-ul genereaza nodurile bazei de date si le seteaza numele (prin constructor-ul clasei Node);

 - Clasa Node - contine o lista de instante;

 - Instance - o lista de tip Object care contine valori de tip Integer/Float/String in functie de tipul fiecarui atribut
		 - o lista de String-uri in care se salveaza numele atributelor pentru a fi accesate mai rapid
		 - variabila de tip long timestamp utilizata ulterior la sortarea instantelor in noduri in functie de momentul de timp la care au fost create;

 - Entity - nume + factor de replicare + lista de atribute;

 - Attribute - nume si tip (Integer/Float/String);

 - DBOperations - clasa care contine metode utile pentru modularizarea codului, de exemplu metoda delete, utilizata atat pentru comanda DELETE, cat si pentru comanda CLEANUP;

 - TimestampComparator - clasa comparator utilizata pentru a compara instantele in functie de variabila de tip long timestamp.


*Mod de functionare 

 - CREATEDB - comanda care creeaza baza de date cu numele, capacitatea maxima a unui nod si numarul de noduri setate din constructor;

 - CREATE  - creeaza o entitate
	      - creeaza pe rand atributele, fiecare cu nume si tip date si se adauga pe rand in lista de atribute a entitatii
		- entitatea a fost construita => se adauga in lista de entitati aflata in DBOperaions;

 - INSERT - se creeaza o instanta a entitatii a carei nume se da ( se gaseste entitatea respectiva in lista de enitati )
	     - se salveaza factorul de replicare al entitatii in replicationFactor
		- se ia pe rand fiecare atribut al entitatii gasite si adauga numele acestuia in lista de nume a instantei, iar valoarea o converteste in functie de campul tip al atributului si apoi o adauga in lista cu valori a instantei
		- instanta a fost construita, deci trebuie adaugata in baza de date de un numarul de ori egal cu factorul de replicare
		- mai intai se afla numarul de noduri libere si se adauga cate noduri mai sunt necesare pentru realizarea operatiei de inserare (bonus2)
		- se realizeaza inserarea, cate o instanta in fiecare nod, de un numar de ori egal cu factorul de replicare
		- se sorteaza instantele din noduri descrescator dupa timestamp (momentul la care au fost create);

 - DELETE - se creaza o lista cu instantele care trebuie sterse
	     - se parcurg nodurile si instantele, iar atunci cand numele si cheia primara corespund, se adauga instanta in lista cu instantele de sters
		- se parcurge lista cu instantele de sters si se sterg pe rand din noduri elementele care se afla in aceasta lista

 - GET - se parcurg nodurile si se afiseaza numele acelora care contin instanta cu numele de enitate si cheia primara date
	 - se parcurg nodurile si se afiseaza numele si valoarea fiecarui camp care corespunde cu numele de enitate si cheia primara date

 - UPDATE - se cauta tipul campului care trebuie modificat in lista de atribute si se memoreaza intr-o variabila type
	     - se cauta campul care trebuie modificat si i se schimba valoarea cu cea noua convertita la tipul indicat de variabila type
		- se sorteaza din nou instantele dupa timestamp (timestamp-ul s-a modificat doar la instantele care au primit update)

 - CLEANUP - parcurge instantele din fiecare nod si verifica daca timestamp-ul acestora este mai mic decat cel dat; daca este, se adauga instantele intr-o lista cu instante de sters
		- ca la comanda DELETE, se parcurge acea lista si se sterg pe rand instantele
