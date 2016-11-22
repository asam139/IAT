; Miramos si coger el paraguas o no

;HECHOS-------------------------------------------------------------
(deffacts previsiones "Previsión del tiempo"

  (prevision soleado)
  (humedad normal)

)

;REGLAS-------------------------------------------------------------
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

(defrule si-llevas-sol
     (prevision soleado)
     (humedad alta)	
=>
     (printout t "Coge el paraguas" crlf)
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