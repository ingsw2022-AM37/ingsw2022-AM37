# Peer-Review 1: UML

[Matteo Baggetto], [Matteo Brambilla], [Paolo Cerutti]

Gruppo [37]

Valutazione del diagramma UML delle classi del gruppo [10].

## Lati positivi
- Il package del **Controller** è ben strutturato e funziona bene, viene esposta all'esterno una sola classe e abbiamo apprezzato molto la gestione della Lobby e degli Users.

- Ci piace molto l'utilizzo dei **Manager** per la gestione delle funzionalità del gioco, pensiamo sia un'idea brillante per il loro scopo.

- Per quanto il nostro gruppo non si sia ancora concentrato su ciò, riteniamo che l'utilizzo di **Azioni** per poter notificare l'aggiornamento dello stato della partita sia un'ottima scelta.

## Lati negativi
- Il punto che abbiamo trovato più critico è sicuramente la gestione dei **Personaggi** in quanto ci sono circa due classi per ognuno di essi; ciò per quanto ci riguarda è segnale di un cattivo design in quanto nel caso si volessero aggiungere nuovi personaggi questo comporterebbe la creazione di 2 * n classi.

- Abbiamo anche qualche dubbio circa l'utilizzo di classi come "**Table**", "**StudentDisc**", "**ProfessorPawn**" in quanto sono perlopiù classi formate da soli dati ed i rispettivi getter. Tutti i metodi "di applicazione" invece sono isolati nel package Action e non hanno nessun tipo di dato al loro interno. Dal nostro punto di vista sembra sia stata effettuata una divisione evitabile tra dati e azioni e ciò ha incrementato di molto il numero di classi del model.

## Confronto tra le architetture
Rispetto a questa architettura la nostra non rappresenta con classi gli oggetti con poche caratteristiche (come i professori e i singoli studenti) e si pone come obiettivo di essere essenziale nelle sue funzionalità, evitando di suddividersi troppo e inutilmente.

La questione dei personaggi è stata approcciata in modo differente; la nostra architettura riesce a condensare in un database degli _effetti_ _base_ l'effetto di ogni personaggio che è _costruito_ sulla concatenazione di quest'ultimi. Consigliamo pertanto un metodo diverso che possa raggruppare gli effetti dei personaggi in meno classi oppure la creazione di personaggi attraverso la concatenazione di effetti base.

- L'uso delle **Azioni** per notificare il cambiamento di stato della partita lo terremo in considerazione e nel caso decidessimo di seguire questa strada la adatteremo al nostro design.

- Anche i **Manager** sono un punto forte del design però riteniamo che ognuno debba avere al suo interno il riferimento dell'oggetto che gestisce, così da evitare un'infinità di getter, abbiamo anche noi seguito questa strada ma implementata in modo tale da essere coerente con il nostro design.

 - La gestione della **Lobby** è molto snella e funzionale.