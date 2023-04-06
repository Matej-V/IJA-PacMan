Projekt Pac-Man IJA 2022/2023

Navrhni rozhranie pre hru Pac-Man v JavaFX. Toto rozhranie by malo obsahovať herné pole, skóre, počet zostávajúcich životov a tlačidlá pre ovládanie Pac-Man postavy.

Navrhnite triedy pre Pac-Man, duchov a cestu. Trieda Pac-Man by mala obsahovať metódy pre pohyb, zberanie kľúčov a správu životov. Triedy duchov by mali obsahovať metódy pre pohyb a stíhanie Pac-Man postavy. Trieda cesty by mala obsahovať informácie o tom, kde sa Pac-Man môže pohybovať.

Implementujte čítanie a spracovanie mapy z textového súboru. Súbor by mal obsahovať informácie o stenách, ceste, duchoch, kľúčoch a štartovacom a koncovom políčku. Na základe týchto informácií by sa mala inicializovať herná plocha.

Implementujte logiku hry Pac-Man. Pac-Man by sa mal môcť pohybovať po ceste, zberať kľúče, vyhýbať sa duchom a snažiť sa dostať k cieľu. Duchovia by mali stíhať Pac-Mana a snažiť sa ho chytiť. Počet zostávajúcich životov by sa mal aktualizovať pri každej kolízii Pac-Mana s duchom. Taktiež by sa mal zaznamenávať pohyb a históriu krokov Pac-Mana a poskytnúť možnosť vrátiť sa v čase.

Implementácia ovládania Pac-Man postavy pomocou kláves WSAD alebo šípky.

Testujte aplikáciu a opravujte prípadné chyby.

Návrhový model:
Trieda Field:
-metóda priradenia políčka bludisku
    Podtrieda Wall
    Podtrieda Path
    - typ(start, end, path)


Trieda MazeObject:
- poloha
- metóda pre pohyb
    Podtrieda Ghost:
    - metóda pre stíhanie Pac-Mana(algoritmus Blink)
    Podtrieda PacMan:
    - počet zostávajúcich životov
    - počet zozbieraných kľúčov/zozbieraný kľúč
    - metóda pre zberanie kľúčov(možno predch. metódy)
    - metóda pre aktualizáciu počtu zostávajúcich životov
    Podtrieda Key

Trieda Maze:
- matica políčok reprezentujúca herné pole
- metóda pre inicializáciu herného poľa z textového súboru
- metóda pre získanie stavu herného poľa

Trieda Game:
- inštancie tried PacMan, Ghost a Maze,...