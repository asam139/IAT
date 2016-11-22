; Miramos si coger el paraguas o no


;REGLAS-------------------------------------------------------------
(defrule inicial
     (initial-fact)
=>
     (printout t "Quiere salir y no sabe si llevar paraguas. " crlf)
     (printout t "Antes debe informarse de la previsión meterológica." crlf)
     (printout t "Dígame la previsión del tiempo: soleado o nublado o lluvioso" crlf)
     (assert (prevision (read)))
)


(defrule con-nubes
     (prevision nublado)
=>
     (printout t "Coge el paraguas" crlf)
)

(defrule no-llevas-sol
     (prevision soleado)
     (humedad normal)	
=>
     (printout t "No es necesario el paraguas" crlf)
)

(defrule sol
     (prevision soleado)
=>
     (printout t "Dígame si la humedad es alta o normal" crlf)
     (assert (humedad (read)))
)

(defrule si-llevas-sol
     (prevision soleado)
     (humedad alta)	
=>
     (printout t "Coge el paraguas" crlf)
)

(defrule llueve
     (prevision lluvioso)
=>
     (printout t "Dígame si el viento es fuerte o suave" crlf)
     (assert (viento (read)))
)


(defrule no-llevas-lluvia
     (prevision lluvioso)
     (viento fuerte)	
=>
     (printout t "No es necesario el paraguas" crlf)
)

(defrule si-llevas-lluvia
     (prevision lluvioso)
     (viento suave)	
=>
     (printout t "Coge el paraguas" crlf)
)