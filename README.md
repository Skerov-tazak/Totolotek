## Totolotek - System symulacji loterii

Totolotek to jeden z pokazowych projektów zrealizowanych przeze mnie w ramach studiów. Implementuje on grę losową dla wielu graczy i jednocześnie posiada system realizowania płatności

## Najważniejsze Klasy

Ten projekt w pełni pokazuje moc programowania obiektowego. Dzięki sprytnemu wykorzystaniu dziedziczenia i polimorfizmu otrzymujemy bardzo elegancki kod realizujący wymagające zadania.

Przede wszystkim warto spojrzeć na klasy:

- `Centrala`: Realizuje główną funkcjonalność systemu losującego, licząc wydane kupony i implementując inteligentny system sprawdzania poprawności kuponów. 
- `Bank`: Procesuje płatności w oparciu o indywidualne kody weryfikacyjne graczy - pozawala to uniknąć oszustw i przekrętów używająć interfejsu gracza.
- ``:

## Testy

Folder `src/Test` zawiera serię prostych testów jednostkowych opartych na bibliotece `JUnit` - jest to standard w świecie Javy i pozwala na o wiele sprawniejsze debugowanie programu. 

