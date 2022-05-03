# Peer-Review 2: Protocollo di comunicazione

[Matteo Baggetto], [Matteo Brambilla], [Paolo Cerutti]

Gruppo [37]

Valutazione del protocollo di comunicazione del gruppo [10].

## Lati positivi

- Il messaggio **LOBBY** è molto intelligente perchè permette ai giocatori di cambiare la propria scelta e risparmiare tempo in attesa. 


## Lati negativi

- Nel messaggio di tipo **USER**, che viene usato nel momento subito successivo alla connessione al server, vengono mandati due parametri, ovvero il nickname e il numero di giocatori con cui si vuole condividere una partita; questo però potrebbe portare all'invio di informazioni extra che il server ha già ricevuto in passato: si verifica ad esempio quando il client manda il suo nome ma questo è già in uso in una qualche lobby, a questo punto il giocatore oltre a un nuovo nome dovrà mandare anche il numero di giocatori che aveva già scelto.

- I messaggi di tipo **STILL_ALIVE** non sono strettamente necessari in quanto quando si manda un messaggio o lo si riceve Java stesso può lanciare una IOException nel caso in cui la connessione sia stata interrotta nel frattempo.  

- Non è presente un messaggio per notificare gli errori, ad esempio quello rigurdante la richiesta di inserire nuovamente il nickname.


## Confronto tra le architetture

- I lati negativi presentano al loro interno una possibile soluzione che è già stata implementata da noi.
- Il messaggio **ROUND** non è presente nella nostra architettura, è un buon metodo per affrontare la gestione del proprio round e separare le istruzioni possibili, potremmo implementarlo.


