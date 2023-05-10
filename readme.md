Implementačná dokumentácia k projektu PacMan v jazyku Java 2022/2023
----------------------------
----------------------------
### Autori
- **Matej Vadovič** - xvadov01 - vedúci tímu
- **Alina Vinogradova** - xvinog00
## Použité technológie

Projekt bol implementovaný pomocou `JavaFX` a využíva zostavovací systém `Maven`.
## Preloženie projektu
Skompiluje zdrojové súbory, vygeneruje programovú dokumentáciu javadoc do `/doc/javadoc` a vytvorí `jar` v priečinku `target`.
```
mvn compile
```
## Spustenie projektu
Pre spustenie projektu je potrebné mať nainštalované JavaFX. Projekt sa spúšťa z priečinku projektu pomocou príkazu:
```
mvn javafx:run
```
## Vygenerovanie doxygen dokumentácie
Pre vygenerovanie stačí spustiť v prienčinku `doc` príkaz:
```
doxygen Doxyfile
```


## Úvod

Projekt PacMan je hra pre jedného hráča implementovaná v jazyku Java s použitím `JavaFX`. Hráč ovláda postavu Pacmana pomocou šípiek, kláves W,S,A,D a myšou(kliknutím na políčko) a jeho cieľom je zozbierať všetky kľúče v bludisku a získať čo najviac bodov. Duchovia sa pohybujú po bludisku a Pacman sa im snaži vyhýbať. Duchovia sa čas od času sa stanú "eatable", čo znamená, že ich Pacman môže zjesť a dostane 100 bodov navyše. Ak hráča trikrát chytia, prehráva hru. Po dokončení hry sa zobrazí konečné skóre a hráč má možnosť začať novú hru. Implemetovali sme tiež možnosť prehrania poslednej hry pomocou klávesy R a spätného prehrania pomocou klávesy B. Hra umožňuje prerušenie pomocou klávesy P.

## Použité návrhové vzory
Pri implementácii projektu sme použili návrhový vzor MVC a návrhový vzor Observer. Návrhový vzor MVC sme použili na oddelenie grafického rozhrania od logiky hry a modelu. Návrhový vzor Observer sme použili na zabezpečenie komunikácie medzi modelom a grafickým rozhraním.

## Balíky a triedy

Projekt PacMan pozostáva z niekoľkých balíkov a tried:

### Balík common

Balík `common` obsahuje rozhrania, ktoré sa používajú v celom projekte. Obsahuje rozhranie `MazeObject`, ktoré definuje metódy pre objekty v bludisku, rozhranie `Maze`, ktoré definuje metódy pre bludisko, rozhranie `Field`, ktoré definuje metódy pre jednotlivé polia v bludisku, a rozhranie `Observable`, ktoré definuje metódy pre pozorovateľov a pozorovaných.

### Balík game

Balík `game` obsahuje triedy, ktoré implementujú funkcionality modelu hry. Obsahuje triedy `GhostObject`, `KeyObject`, `MazeClass`, `MazeConfigure`, `PacmanObject`, `PathField`, `TargetField` a `WallField`. Trieda MazeClass predstavuje samotné bludisko a implementuje rozhranie Maze. Triedy PacmanObject, GhostObject a KeyObject predstavujú objekty bludiska, implementujú rozhranie MazeObject. Triedy PathField, TargetField a WallField predstavujú rôzne typy polí v bludisku a implementujú rozhranie Field. Tento balík obsahuje aj triedu `GameException`, ktorá slúži ako flag, že hráč vyhral alebo prehral hru.

### Balík pacman_project

Balík `pacman_project` obsahuje hlavnú triedu `PacManApp`, ktorá je rozšírením triedy `javafx.application.Application` a inicializuje grafické rozhranie pre hru. Trieda `PacManController` riadi pohyby zmeny objektov bludiska, načítanie hry a riadenie zonbrazovanie prostredia aplikácie. Trieda `PacManView` riadi, čo je zobrazené v aplikácii, a trieda `LogWriter` slúži na zapisovanie logov do súboru.

### Balík view
Balík `view` obsahuje triedy, ktoré reprezentujú grafické prvky hry, teda `FieldView`, `GhostObjectView`, `KeyObjectView`, `PacmanObjectView`. Tieto triedy rozširujú triedu `javafx.scene.layout.Pane` a implementujú rozhranie `Observer`. Pri zmene stavu modelu tieto triedy reagujú a aktualizujú zobrazenie na obrazovke. Obsahuje tiež triedu `UIBarView`, ktorá reprezentuje časť UI zobrazjúcu skóre a životy pacmana.

## Plány do budúcnosti

V budúcnosti plánujeme vylepšiť grafiku a pridať možnosť ukladania tabuľky skóre hráčov.