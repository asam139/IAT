package practica1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase implementa el problema del n-Puzzle, normalmente n=8 o n=15.
 * 
 * @author fjgc@decsai.ugr.es
 */

public class NPuzzle {
    
    /** TamaÃ±o del problema, el nÃºmero de casillas es n+1*/
    int n; 
    
    /** Tablero de juego */
    ArrayList<Integer> tablero;
    
    /** Movimiento para llegar al nodo padre, si es ! de 1,2,3,4 no lo sabemos*/
    int padre;
    
    /** Valor de la funciÃ³n costo para el nodo (profundidad)*/
    int g;
    
    /** Valor de la funciÃ³n heurÃ­stica para el nodo*/
    int h;

    /**
     * CTes. para indicar los segundos mÃ¡ximos de espera
     */
    public static final int TMAX = 300;
    
     /**
     * CTes. para las direcciones del hueco
     */
    public static final int ARRIBA = 1;
    public static final int ABAJO = 2;
    public static final int DERECHA = 3;
    public static final int IZQUIERDA = 4;
    
    
    
   /*---------------------------------------------------------------------------*/
    /**
     * Constructor para crear un n-puzzle a partir de un fichero.
     * @param fichero nombre del fichero con el n-puzzle
     * @param n indica el tamaÃ±o del tablero. Lo rormal 8 Ã³ 15 (8-puzzle/15puzzle)
     */
    public NPuzzle(String fichero, int n) {
        
        //Guardamos el n si es correcto
        if ( Math.sqrt(n+1)!= Math.round(Math.sqrt(n+1)) ) {
            System.out.println("ERROR: Imposible usar n="+n+" Usando n=8");
            this.n=8;
        } else
            this.n=n;
        
        //Ahora obtenemos el tablero
        tablero=new ArrayList<>();
        
        //Leemos el tablero de un fichero y si no podemos lo generamos 
        //aleatoriamente
        try {  
            Scanner scanner = new Scanner(new File(fichero));
            while(scanner.hasNextInt())
                tablero.add(scanner.nextInt());            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NPuzzle.class.getName()).log(Level.SEVERE, null, ex);
            //Si ha falado la lectura del fichero,
            //creamos un tablero con posiciones aleatorias
            tablero=generaPermutacion(n+1);                    
        }
        
        //Iniciamos el padre del nodo a 0
        this.padre=0;
        //Iniciamos el costo del nodo a 0
        this.g=0;
        //Iniciamos la heuristica del nodo
        this.h=heuristica();
        //this.h=heuristica2();
        
    }
    /*---------------------------------------------------------------------------*/
    /**
     * Constructor para un tablero aleatorio de un n-puzzle.
     * @param n indica el tamaÃ±o del tablero. Lo rormal 8 Ã³ 15 (8-puzzle/15puzzle)
     */
    public NPuzzle(int n) {
        //Guardamos el n si es correcto
        if ( Math.sqrt(n+1)!= Math.round(Math.sqrt(n+1)) ) {
            System.out.println("ERROR: Imposible usar n="+n+" Usando n=8");
            this.n=8;
        } else
            this.n=n;
        //Creamos un tablero con posiciones aleatorias
        tablero = generaPermutacion(n+1);
        //tablero = new ArrayList<>(Arrays.asList(9, 10, 15, 14, 2, 4, 6, 0, 11, 5, 7, 3, 12, 13, 1, 8));
        //Iniciamos el padre del nodo a 0
        this.padre=0;
        //Iniciamos el costo del nodo a 0
        this.g=0;
        //Iniciamos la heuristica del nodo
        this.h=heuristica();
        //this.h=heuristica2();
    }
 /*---------------------------------------------------------------------------*/
    /**
     * Constructor copiar para un tablero n-puzzle.
     * @param puzzle a copiar.
     */
    public NPuzzle(NPuzzle puzzle) {
	this.n=puzzle.n;
	this.tablero=new ArrayList<>(this.n+1);
	for (int i=0;i<this.n+1;i++)
	    this.tablero.add(puzzle.tablero.get(i));
        
        //Iniciamos el padre del nodo 
        this.padre=puzzle.padre;
        //Iniciamos el costo del nodo 
        this.g=puzzle.g;
        //Iniciamos la heuristica del nodo
        this.h=puzzle.h;
    }
    /*---------------------------------------------------------------------------*/
    /**Este procedimiento genera una permutaciÃ³n aleatorio de tamaÃ±o n y lo 
     * devuelve en un ArrayList.
     * @param n tamaÃ±o de la permutaciÃ³n
     * @return ArrayList donde se alamacena la permutaciÃ³n
    */
    final ArrayList<Integer> generaPermutacion(int n){
        ArrayList<Integer> permutacion=new ArrayList<>(n);
        //Generamos los nÃºmeros de la permutaciÃ³n
        for (int i=0;i<n;i++)
            permutacion.add(i);
        //Los mezclamos y los devolvemos
        java.util.Collections.shuffle(permutacion);    
        return permutacion;
    }
    /*---------------------------------------------------------------------------*/
    /**
     * Esta funciÃ³n calcula el valor f del nodo como la suma del coste (g) y la
     * herutistica (h)
     * @return f(node)=g(node)+h(node)
     */
    public int f() {
        return this.g+this.h;
    }
    /*---------------------------------------------------------------------------*/
    /**
     * Esta funciÃ³n devuelve la profundida nodo 
     * @return la profundidad enla qeu estÃ¡ el nodo en el Ã¡rbol de bÃºsqueda
     */
    public int profundidad() {
        return this.g;
    }
   
    /*---------------------------------------------------------------------------*/
    /**
       Esta funciÃ³n devuelve un valor heurÃ­tico para el tablero dado basado en la 
       distancia de Hamming, que nos da el nÃºmero de casillas mal colocadas. Si todas
       estÃ¡n mal colocadas devolverÃ¡ n y si estÃ¡n todas bien colocadas devolverÃ¡ 0.
       @param puzzle del cual queremos calcular la heurÃ­stica.
       @return distancia de Hamming del puzzle
     */
    public int heuristica2() {
      int i,distancia;

      for (i=0,distancia=0;i<this.n+1;i++)
                //si encontramos una ficha mal aumentamos la distancia.
                if (this.tablero.get(i)!=i)
                    distancia++;
       return distancia;
    }
    /*---------------------------------------------------------------------------*/
    /**
       Esta funciÃ³n devuelve un valor heurÃ­stico para el tablero dado basado en la 
       distancia Manhattan, que nos da la suma de las distancias desde la posiciÃ³n
       actual de cada ficha hasta su posiciÃ³n original. 
       @param puzzle del cual queremos calcular la heurÃ­stica.
       @return distancia de Hamming del puzzle
     */
    public int heuristica() {
        int i,distancia;
        int fila_actual=1,columna_actual=1; /*PosiciÃ³n de la casilla que estamos viendo*/
        int fila=1,columna=1; /*PosiciÃ³n donde deberÃ­a estar la casilla*/
        int c; /*Casilla actual*/
        int ancho=(int)Math.sqrt(this.n+1);/*Ancho del tablero*/
  
        for (i=0,distancia=0;i<this.n+1;i++) {
            c=this.tablero.get(i);

            if (c!=0) {
                /*Buscamos cual es la casilla que deberÃ­a ocupar*/
                fila=1;
                while (!( c>=ancho*(fila-1) && c<ancho*fila )) fila++;
                columna=(c%ancho)+1;

                /*AÃ±adimos la distancia*/
                distancia+=Math.abs(fila-fila_actual)+Math.abs(columna-columna_actual);
            }

            /*Nos vamos a la siguiente casilla*/
            columna_actual++;
            if (columna_actual>ancho) {
                columna_actual=1;
                fila_actual++;
            } 
        }
        
        return distancia;
    }

    /*---------------------------------------------------------------------------*/
    /** Convierte el objeto NPuzzle a cadena para porder imprimirlo*/
    @Override
    public String toString() {
        int filas=(int)Math.sqrt(n+1);
        String out= n+"-Puzzle g="+this.g+" h="+this.h+"{\n" ; //Mostramos el n
        //Mostramos el tablero
        for (int i=0;i<=n;i++) {
            int x=tablero.get(i);
            if (x!=0) out+=(x+" ");
            else out+=("_ ");
            if ((i+1)%filas==0) out+=("\n");
        }
        out+="}\n";
        return out;
    }
    /*---------------------------------------------------------------------------*/
    /**Miramos si este n-puzzle estÃ¡ en su estado objetivo
     @return si estÃ¡ o no en su estado objetivo*/
    public boolean objetivo() {
        for (int i=0;i<n;i++)
            //si encontramos una ficha mal colocada no estÃ¡ terminado
	    if (this.tablero.get(i)!=i) {
                return false;
            }
        return true;
    }
    /*---------------------------------------------------------------------------*/
    /**Esta funciÃ³n nos dice si el n-puzzle tiene soluciÃ³n o no. Un tablero serÃ¡ 
     * resoluble si :
     *     -Si el ancho del tablero es impar (p.e. 8-puzzle) si el nÃºmero de 
     *      inversiones es par el tablero es resoluble.
     *     -Si el ancho del tablero es par (p.e. 15-puzzle) si el nÃºmero de 
     *      inversiones + la fila donde estÃ¡ el hueco tiene la misma paridad 
     *      que el tablero soluciÃ³n (que tiene 0 inversiones + fila donde estÃ¡ 
     *      el hueco), entonces el tablero es resoluble.
     * Nota: Se cuenta el nÃºmero de fila desde arriba. Una inversiÃ³n es cuando 
     * a estÃ¡ antes que b y a>b. Para calcular las inversiones se descargta el 
     * hueco y se disponen todas las piezas consecutivamente (como en un vector).
     * @param puzzle a comprobar
     * @return si tiene soluciÃ³n o no.
     */
    boolean resoluble(){
        int i,j;
        int inversiones=0;
        int pos0=0,fila=1;
        int ancho=(int)Math.sqrt(this.n+1);/*Ancho del tablero*/

 
	/*Calculamos el nÃºmero de inversiones del puzzle, una inversiÃ³n es
	  cuando a estÃ¡ antes que b y a>b (descartamos el hueco)*/
	for(i=0;i<this.n;i++)
	    if (tablero.get(i)!=0 )
		for(j=i+1;j<this.n+1;j++) {
		    if(tablero.get(i)>tablero.get(j) && tablero.get(j)!=0)
			inversiones++;
		}

        /*Si el ancho es impar, entonces el nÃºmero de inversiones en un tablero
	  resoluble es par*/
        if (ancho%2!=0) {
            return inversiones%2==0;
        } else {
            /*Caso de tablero impar*/

            /*Buscamos la posiciÃ³n del hueco*/
            for (pos0=0; tablero.get(pos0)!=0 && pos0<n+1; pos0++);
	    /*Buscamos la fila en la que estÃ¡ el hueco*/
	    fila=1;
	    while (!( pos0>=ancho*(fila-1) && pos0<ancho*fila )) fila++;
            /*Nuestro tablero soluciÃ³n tiene paridad impar, pues el 0 lo tiene
            en la primera fila, por tanto si las inversiones+ la fila del hueco es
            impar tiene soluciÃ³n*/
	    return (inversiones+fila)%2 != 0;
        }
    }
    /*---------------------------------------------------------------------------*/
    /*MÃ©todo para comparar dos tableros del n-Puzzle*/
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NPuzzle other = (NPuzzle) obj;
        
        //Distinto n, distintos seguro
        if (this.n != other.n) {
            return false;
        }
        
        //Distinta heurÃ­stica, distintos seguro
        if (this.h != other.h) {
            return false;
        }
        
        //Finalemente, comparamos tablero
        for (int i=0;i<n;i++)
            //si encontramos una ficha distinta, son distintos
            if (!Objects.equals(this.tablero.get(i), other.tablero.get(i)))
                return false;
        
        return true;
    }
    /*---------------------------------------------------------------------------*/
    public void swap(int i, int j) {
        Integer aux=tablero.get(i);
        tablero.set(i, tablero.get(j));
        tablero.set(j, aux);
    }
    /*---------------------------------------------------------------------------*/
    /**Obtenemos el movimiento inversodel que le pasamos como parÃ¡metro
    * @param movimiento direcciÃ³n del movimiento del hueco
    * @return movimiento inverso*/
    public int inverso(int movimiento) {
        if (movimiento==IZQUIERDA) return DERECHA;
        if (movimiento==DERECHA) return IZQUIERDA;
        if (movimiento==ARRIBA) return ABAJO;
        if (movimiento==ABAJO) return ARRIBA;
        return -1;
    }
    /*---------------------------------------------------------------------------*/
    /**Movemos el espacio en blanco, si podemos.
     * @param movimiento direcciÃ³n del movimiento del hueco
     @return si estÃ¡ o no en su estado objetivo*/
    public boolean mueve(int movimiento) {
       
        //coger pos hueco
        int hueco=0;
        while (this.tablero.get(hueco)!=0) hueco++;
        
        //Cogemos la raiz de n+1 pues nos harÃ¡ falta para mirar movimientos vÃ¡lidos.
        int raiz=(int)Math.sqrt(n+1);
        if (movimiento==IZQUIERDA) {
            if (hueco==0 || hueco%raiz==0)
              return false;
            else {
                this.swap(hueco-1,hueco);  
                this.g++; //actualizamos funciÃ³n costo
                this.h=heuristica(); //actualizamos funciÃ³n heurÃ­stica   
                this.padre=inverso(movimiento);
                return true;
            }
        } else if (movimiento==DERECHA) {
            if (hueco==n || (hueco+1)%raiz==0)
              return false;
            else {
                this.swap(hueco+1,hueco);
                this.g++; //actualizamos funciÃ³n costo
                this.h=heuristica(); //actualizamos funciÃ³n heurÃ­stica 
                this.padre=inverso(movimiento);
                return true;
            }
            
        } else if (movimiento==ARRIBA) {
            if (hueco>=0 && hueco<raiz)
              return false;
            else {
                this.swap(hueco-raiz,hueco);
                this.g++; //actualizamos funciÃ³n costo
                this.h=heuristica(); //actualizamos funciÃ³n heurÃ­stica 
                this.padre=inverso(movimiento);
                return true;
            }
            
        } else if (movimiento==ABAJO) {
            if (hueco<=n && hueco>=raiz*(raiz-1))
              return false;
            else {
                this.swap(hueco+raiz,hueco);
                this.g++; //actualizamos funciÃ³n costo
                this.h=heuristica(); //actualizamos funciÃ³n heurÃ­stica 
                this.padre=inverso(movimiento);
                return true;
            }
            
        }
        
        //Movimiento errÃ³neo
        return false;
                
    }
    /*---------------------------------------------------------------------------*/
    /**
       Este mÃ©todo realiza una bÃºsqueda aleatoria de la soluciÃ³n de un 
       n-puzzle, durante TMAX segundos.
       @return la lista de movimiento realizados para llegar al estado objetivo, si 
       estÃ¡ vacÃ­a es que no se ha encontrado ninguna soluciÃ³n o el tablero pasado era 
       el objetivo.
    */
    public ArrayList<NPuzzle> busquedaAleatoria() {
        
        int movimiento;
        int movPadre=-1;/*Movimiento para llegar al padre*/
        ArrayList<NPuzzle> vistos=new ArrayList<>(); /*Lista de nodos vistos*/
        
        long tiempo_inicial=System.currentTimeMillis();
        while (!this.objetivo()) {
            movimiento = (int)(Math.random()*5); 
            if (movimiento!=movPadre && this.mueve(movimiento)) {
		    vistos.add(0,new NPuzzle(this));
                    movPadre=inverso(movimiento);
            }

            /*Si llevamos mÃ¡s de TMAX segundos, no hemos encontrado soluciÃ³n*/
            double tiempo_total=(System.currentTimeMillis()-tiempo_inicial)/1000.;
            if (tiempo_total>60/*TMAX*/){System.out.println("Vistos="+vistos.size());return new ArrayList<>();}
        }
        return vistos;
    }
    /*---------------------------------------------------------------------------*/
    /**
     * Este mÃ©todo devuelve el plan de movimientos seguidos desde el origen hasta el
     * objetivo (que deberÃ­a ser el primero de la lista).
     * @param lista listado de nodos
     * @param origen nodo original
       @return la lista con los movimientos.
    */
    public ArrayList<Integer> plan(ArrayList<NPuzzle> lista, NPuzzle origen) {
        ArrayList<Integer> movimientos= new ArrayList<>(lista.size()); /*Lista de nodos vistos*/
        int tam=lista.size();
        NPuzzle actual=new NPuzzle(lista.get(0));
        while (!actual.equals(origen)) {
            movimientos.add(0,inverso(actual.padre));
            actual.mueve(actual.padre);
            int pos=lista.indexOf(actual);
            if (pos==-1) {
                System.out.println("ERROR en plan");
                System.exit(-1);
            } else if (movimientos.size()>tam+10){
                
                System.out.println("ERROR 2 en plan.");// Movs:"+movimientos);
                System.exit(-1);
            }        
            actual=new NPuzzle(lista.get(pos));
            lista.remove(pos);
        }
        return movimientos;
    }
    /*--------------------------------------------------------------------------*/
    /**Función para implementar BPP
     * @return Lista de movimientos vistos
     */
    public ArrayList<NPuzzle> busqueda_BPP() { 
        int[] movimientos =  {ARRIBA,ABAJO,IZQUIERDA,DERECHA};
        ArrayList<NPuzzle> abiertos=new ArrayList<>();
        ArrayList<NPuzzle> cerrados=new ArrayList<>();
        NPuzzle N;

        abiertos.add(0,new NPuzzle(this));
        long tiempo_inicial=System.currentTimeMillis();
        while (!abiertos.isEmpty()) {
            N = abiertos.remove(0);
            cerrados.add(0,new NPuzzle(N));
            if (N.objetivo()) {
                return cerrados; 
            }
            for (int movimiento:movimientos) {
                if (N.mueve(movimiento)) {
                    if (!cerrados.contains(N)) {
                        N.padre = inverso(movimiento);
                        abiertos.add(0,new NPuzzle(N));
                    }		    
                    N.mueve(inverso(movimiento));
                }
            }
            double tiempo_total=(System.currentTimeMillis()-tiempo_inicial)/1000.;
            /*Si llevamos más de TMAX segundos, no hemos encontrado solución*/
            if (tiempo_total>TMAX){System.out.println("Vistos="+cerrados.size());return new ArrayList<NPuzzle>();}
        }
        return cerrados;
    }
        /*--------------------------------------------------------------------------*/
    /**Función para implementar BPP
     * @return Lista de movimientos vistos
     */
    public ArrayList<NPuzzle> busqueda_BPA() { 
        int[] movimientos =  {ARRIBA,ABAJO,IZQUIERDA,DERECHA};
        ArrayList<NPuzzle> abiertos=new ArrayList<>();
        ArrayList<NPuzzle> cerrados=new ArrayList<>();
        NPuzzle N;

        abiertos.add(0,new NPuzzle(this));
        long tiempo_inicial=System.currentTimeMillis();
        while (!abiertos.isEmpty()) {
            N = abiertos.remove(0);
            cerrados.add(0,new NPuzzle(N));
            if (N.objetivo()) {
                return cerrados; 
            }
            for (int movimiento:movimientos) {
                if (N.mueve(movimiento)) {
                    if (!cerrados.contains(N)) {
                        N.padre = inverso(movimiento);
                        abiertos.add(new NPuzzle(N));
                    }		    
                    N.mueve(inverso(movimiento));
                }
            }
            double tiempo_total=(System.currentTimeMillis()-tiempo_inicial)/1000.;
            /*Si llevamos más de TMAX segundos, no hemos encontrado solución*/
            if (tiempo_total>TMAX){System.out.println("Vistos="+cerrados.size());return new ArrayList<NPuzzle>();}
        }
        return cerrados;
    }
        /*--------------------------------------------------------------------------*/
    /**Función para implementar BPP
     * @return Lista de movimientos vistos
     */
    public ArrayList<NPuzzle> busqueda_BPP_iterativa() { 
        int[] movimientos =  {ARRIBA,ABAJO,IZQUIERDA,DERECHA};
        ArrayList<NPuzzle> abiertos=new ArrayList<>();
        ArrayList<NPuzzle> cerrados=new ArrayList<>();
        NPuzzle N;
        int limite = 1;
        int contador = 0;
        int iteracion = 0;
        long tiempo_inicial=System.currentTimeMillis();
        abiertos.add(0, new NPuzzle(this));
        while (!abiertos.isEmpty()) {
            // No estoy seguro de que funcione bien
            abiertos=new ArrayList<>();
            cerrados=new ArrayList<>();
            while (contador < limite) {
                abiertos.add(0, new NPuzzle(this));
                while (iteracion < limite) {
                    if (iteracion >= limite - contador) {
                        N = abiertos.remove(1);
                    } else {
                        N = abiertos.remove(0);
                    }
                    cerrados.add(0,new NPuzzle(N));
                    if (N.objetivo()) {
                        return cerrados;  // Debo arreglar esto
                    }
                    for (int movimiento:movimientos) {
                        if (N.mueve(movimiento)) {
                            if (!cerrados.contains(N)) {
                                N.padre = inverso(movimiento);
                                abiertos.add(0,new NPuzzle(N));
                            }		    
                            N.mueve(inverso(movimiento));
                        }
                    }
                    iteracion++;
                }
                contador ++;
                iteracion = 0;
            }
            limite++;
            contador = 0;
            double tiempo_total=(System.currentTimeMillis()-tiempo_inicial)/1000.;
            /*Si llevamos más de TMAX segundos, no hemos encontrado solución*/
            if (tiempo_total>TMAX){System.out.println("Vistos="+cerrados.size());return new ArrayList<NPuzzle>();}
        }
        return cerrados;
    }
    /*--------------------------------------------------------------------------*/
    /**Función para implementar escalada simple
     * @return Lista de movimientos vistos
     */
    public ArrayList<NPuzzle> busqueda_escalada_simple() { 
        int[] movimientos =  {ARRIBA,ABAJO,IZQUIERDA,DERECHA};
        ArrayList<NPuzzle> abiertos=new ArrayList<>();
        ArrayList<NPuzzle> cerrados=new ArrayList<>();
        NPuzzle N;
        NPuzzle hijo;
        N = new NPuzzle(this);
        
        abiertos.add(0,new NPuzzle(this));
        long tiempo_inicial=System.currentTimeMillis();
        while (!abiertos.isEmpty()) {
            hijo = abiertos.remove(0);
            if (hijo.h <= N.h) {
                N = hijo;
                cerrados.add(0,new NPuzzle(N));
                if (N.objetivo()) {
                    return cerrados; 
                }
                for (int movimiento:movimientos) {
                    if (N.mueve(movimiento)) {
                        if (!cerrados.contains(N)) {
                            N.padre = inverso(movimiento);
                            abiertos.add(0,new NPuzzle(N));
                        }		    
                        N.mueve(inverso(movimiento));
                    }
                }
            }
            
            double tiempo_total=(System.currentTimeMillis()-tiempo_inicial)/1000.;
            /*Si llevamos más de TMAX segundos, no hemos encontrado solución*/
            if (tiempo_total>TMAX){System.out.println("Vistos="+cerrados.size());return new ArrayList<NPuzzle>();}
        }
        return cerrados;
    }
    /*--------------------------------------------------------------------------*/
    /**Función para implementar escalada máxima pendiente
     * @return Lista de movimientos vistos
     */
    public ArrayList<NPuzzle> busqueda_escalada_maxima() { 
        int[] movimientos =  {ARRIBA,ABAJO,IZQUIERDA,DERECHA};
        ArrayList<NPuzzle> abiertos=new ArrayList<>();
        ArrayList<NPuzzle> cerrados=new ArrayList<>();
        NPuzzle N;
        NPuzzle hijo;
        NPuzzle mejor;
        
        N = new NPuzzle(this);
        
        abiertos.add(0,new NPuzzle(this));
        long tiempo_inicial=System.currentTimeMillis();
        while (!abiertos.isEmpty()) {
            hijo = abiertos.remove(0);
            if (hijo.h <= N.h) {
                N = hijo;
                cerrados.add(0,new NPuzzle(N));
                if (N.objetivo()) {
                    return cerrados; 
                }
                mejor = new NPuzzle(N);
                for (int movimiento:movimientos) {
                    if (N.mueve(movimiento)) {
                        if (!cerrados.contains(N)) {
                            N.padre = inverso(movimiento);
                            if (N.h < mejor. h) {
                                mejor = new NPuzzle(N);
                                mejor.padre = N.padre;
                            }
                        }		    
                        N.mueve(inverso(movimiento));
                    }
                }
                if (!mejor.equals(N)) {
                    abiertos.add(0,new NPuzzle(mejor));
                }
            }
            
            double tiempo_total=(System.currentTimeMillis()-tiempo_inicial)/1000.;
            /*Si llevamos más de TMAX segundos, no hemos encontrado solución*/
            if (tiempo_total>TMAX){System.out.println("Vistos="+cerrados.size());return new ArrayList<NPuzzle>();}
        }
        return cerrados;
    }
    /*--------------------------------------------------------------------------*/
    /**Función para implementar Primero el mejor
     * @return Lista de movimientos vistos
     */
    public ArrayList<NPuzzle> busqueda_BPM() { 
        int[] movimientos =  {ARRIBA,ABAJO,IZQUIERDA,DERECHA};
        ArrayList<NPuzzle> abiertos=new ArrayList<>();
        ArrayList<NPuzzle> cerrados=new ArrayList<>();
        NPuzzle hijo;
        NPuzzle mejor;
        int min_value;
        int min_pos;
        
        abiertos.add(0,new NPuzzle(this));
        long tiempo_inicial=System.currentTimeMillis();
        while (!abiertos.isEmpty()) {
            // Extraemos el mejor nodo:
            min_value = abiertos.get(0).f();
            min_pos = 0;
            for (int i=1; i<abiertos.size();i++) {
                if (abiertos.get(i).f() < min_value) {
                    min_value = abiertos.get(i).f();
                    min_pos = i;
                }
            }
            mejor = abiertos.remove(min_pos);
            cerrados.add(0,new NPuzzle(mejor));
            if (mejor.objetivo()) {
                return cerrados; 
            }
            for (int movimiento:movimientos) {
                // Genero el nodo sucesor
                hijo = new NPuzzle(mejor);
                if (hijo.mueve(movimiento)) {
                    // Añadimos el nuevo camino
                    if (!hijo.esta_en(abiertos) && !hijo.esta_en(cerrados)) {
                        hijo.padre = inverso(movimiento);
                        abiertos.add(0,new NPuzzle(hijo));
                    }
                }		    
            }
            
            double tiempo_total=(System.currentTimeMillis()-tiempo_inicial)/1000.;
            /*Si llevamos más de TMAX segundos, no hemos encontrado solución*/
            if (tiempo_total>TMAX){System.out.println("Vistos="+cerrados.size());return new ArrayList<NPuzzle>();}
        }
        return cerrados;
    }
    /*--------------------------------------------------------------------------*/
    /**Función para ver si una lista contiene un puzzle
     * 
     */
        public boolean esta_en(ArrayList<NPuzzle> lista) {
        for (int i=0;i<lista.size();i++) {
            if (this.equals(lista.get(i))) {
                return true;
            }
        }
        return false;
    }
    /*--------------------------------------------------------------------------*/
    /**Función para lanzar la búsqueda aleatoria
     * 
     */
    public void launch_aleatoria(String salida) {
        //Lanzamos un algoritmo de bÃºsqueda aleatoria y miramos el tiempo que tarda
        System.out.println("\nComienza la búsqueda aleatoria ("+TMAX+"segundos max)");
        long tiempo_inicial=System.currentTimeMillis();
	NPuzzle copia=new NPuzzle(this);
        ArrayList<NPuzzle> nodos=copia.busquedaAleatoria();
        System.out.println("Tiempo ="+(System.currentTimeMillis()-tiempo_inicial)/1000.+"seg");
        salida(nodos, salida);
    }
    /*---------------------------------------------------------------------------*/
    /**Función para lanzar la búsqueda en profuncidad
     * 
     */
    public void launch_BPP(String salida) {
        //Lanzamos un algoritmo de búsqueda en profundidad y miramos el tiempo que tarda
        System.out.println("\nComienza la búsqueda BPP ("+TMAX+"segundos max)");
        long tiempo=System.currentTimeMillis();
	NPuzzle BPP=new NPuzzle(this);
        ArrayList<NPuzzle> nodos=BPP.busqueda_BPP();
        System.out.println("Tiempo ="+(System.currentTimeMillis()-tiempo)/1000.+"seg");        
        salida(nodos, salida);
    }
    /*---------------------------------------------------------------------------*/
    /**Función para lanzar la búsqueda en profuncidad
     * 
     */
    public void launch_BPA(String salida) {
        //Lanzamos un algoritmo de búsqueda en profundidad y miramos el tiempo que tarda
        System.out.println("\nComienza la búsqueda BPA ("+TMAX+"segundos max)");
        long tiempo=System.currentTimeMillis();
	NPuzzle BPA=new NPuzzle(this);
        ArrayList<NPuzzle> nodos=BPA.busqueda_BPA();
        System.out.println("Tiempo ="+(System.currentTimeMillis()-tiempo)/1000.+"seg");
        salida(nodos, salida);
    }
    /**Función para lanzar la búsqueda en profuncidad
     * 
     */
    public void launch_BPP_iterativa(String salida) {
        //Lanzamos un algoritmo de búsqueda en profundidad y miramos el tiempo que tarda
        System.out.println("\nComienza la búsqueda BPP iterativa("+TMAX+"segundos max)");
        long tiempo=System.currentTimeMillis();
	NPuzzle BPP=new NPuzzle(this);
        ArrayList<NPuzzle> nodos=BPP.busqueda_BPP_iterativa();
        System.out.println("Tiempo ="+(System.currentTimeMillis()-tiempo)/1000.+"seg");        
        salida(nodos, salida);
    }
    /*---------------------------------------------------------------------------*/
    /**Función para lanzar la búsqueda en escalada simple
     * 
     */
    public void launch_escalada_simple(String salida) {
        //Lanzamos un algoritmo de búsqueda en profundidad y miramos el tiempo que tarda
        System.out.println("\nComienza la búsqueda escalada simple ("+TMAX+"segundos max)");
        long tiempo=System.currentTimeMillis();
	NPuzzle escalada_simple=new NPuzzle(this);
        ArrayList<NPuzzle> nodos=escalada_simple.busqueda_escalada_simple();
        System.out.println("Tiempo ="+(System.currentTimeMillis()-tiempo)/1000.+"seg");
        salida_heuristica(nodos, salida);
    }
    /*---------------------------------------------------------------------------*/
    /**Función para lanzar la búsqueda en escalada máxima pendiente
     * 
     */
    public void launch_escalada_maxima(String salida) {
        //Lanzamos un algoritmo de búsqueda en profundidad y miramos el tiempo que tarda
        System.out.println("\nComienza la búsqueda escalada de máxima pendiente ("+TMAX+"segundos max)");
        long tiempo=System.currentTimeMillis();
	NPuzzle escalada_maxima=new NPuzzle(this);
        ArrayList<NPuzzle> nodos=escalada_maxima.busqueda_escalada_maxima();
        System.out.println("Tiempo ="+(System.currentTimeMillis()-tiempo)/1000.+"seg");
        salida_heuristica(nodos, salida);
    }
    /*---------------------------------------------------------------------------*/
    /**FunciÃ³n para probar la mostrar la salida.
     * @param args the command line arguments
     */
    /*---------------------------------------------------------------------------*/
    /**Función para lanzar la búsqueda en escalada máxima pendiente
     * 
     */
    public void launch_BPM(String salida) {
        //Lanzamos un algoritmo de búsqueda en profundidad y miramos el tiempo que tarda
        System.out.println("\nComienza la búsqueda primero el mejor ("+TMAX+"segundos max)");
        long tiempo=System.currentTimeMillis();
	NPuzzle BPM=new NPuzzle(this);
        ArrayList<NPuzzle> nodos=BPM.busqueda_BPM();
        System.out.println("Tiempo ="+(System.currentTimeMillis()-tiempo)/1000.+"seg");
        salida_heuristica(nodos, salida);
    }
    /*--------------------------------------------------------------------------*/
    public void salida(ArrayList<NPuzzle> nodos, String salida) {
        ArrayList<Integer> movs=null;
        
        if (nodos.size()<=0) 
            System.out.println("Solución NO encontrada.");
        else {
            System.out.println("Solución encontrada. Nodos vistos:"+nodos.size());//+"\n ->"+nodos)
            System.out.println(nodos.get(0).toString());
        }      
        
        movs = this.plan(nodos, this);
        if (movs != null) {
            System.out.println("Solución");
            for (Integer i: movs)
                System.out.println(i+" ");
            System.out.println("Movimientos: " + movs.size());
        }  
        /*Guardamos la salida, si nos han dado un fichero para guardarlo*/
        if (salida!=null && movs!=null) {
            try {
                Writer out = new FileWriter(salida);
                for (Integer i: movs)
                    out.write(i+" ");
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(NPuzzle.class.getName()).log(Level.SEVERE, "Fallo escribiendo fichero salida", ex);
            } 
        }
    }
        /*---------------------------------------------------------------------------*/
    /**FunciÃ³n para probar la mostrar la salida.
     * @param args the command line arguments
     */
    /*--------------------------------------------------------------------------*/
    public void salida_heuristica(ArrayList<NPuzzle> nodos, String salida) {
        ArrayList<Integer> movs=null;
        
        if (nodos.get(0).objetivo()) {
            System.out.println("Solución encontrada. Nodos vistos:"+nodos.size());//+"\n ->"+nodos)
            System.out.println(nodos.get(0).toString());
            
            movs = this.plan(nodos, this);
            if (movs != null) {
                System.out.println("Solución");
                for (Integer i: movs)
                    System.out.println(i+" ");
                System.out.println("Movimientos: " + movs.size());
            }  
            /*Guardamos la salida, si nos han dado un fichero para guardarlo*/
            if (salida!=null && movs!=null) {
                try {
                    Writer out = new FileWriter(salida);
                    for (Integer i: movs)
                        out.write(i+" ");
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(NPuzzle.class.getName()).log(Level.SEVERE, "Fallo escribiendo fichero salida", ex);
                } 
            }       
        } else if(nodos.get(0).h < nodos.get(nodos.size() - 1).h ) {
            System.out.println( "Nodos vistos:"+nodos.size());
            System.out.println("Solución NO encontrada. Pero mejoramos heurística de: " + nodos.get(nodos.size() - 1).h +  " a " + nodos.get(0).h );
            System.out.println(nodos.get(0).toString());
        } else {
            System.out.println("Solución NO encontrada. No posible mejorar heurística: " + nodos.get(nodos.size() - 1).h);
            System.out.println(nodos.get(0).toString());
        }       
    }
    public static void main(String[] args) {       
        int n;
        NPuzzle puzzle;
        String salida=null;
        String busqueda=null;
        //Si el nÃºmero de parÃ¡metros es incorrecto salimos
        if (args.length < 2){
            System.out.println("npuzzle "+"<fich_puzzle> 8/15 [<fich_salida>]\n");
            System.out.println(" Usando n=8. Genero puzzle aleatorio.  ");
            n=8;
            puzzle= new NPuzzle(n);
        } else {
            n=(Integer.valueOf(args[1])).intValue();
            puzzle= new NPuzzle(args[0],n);
            if (args.length>2) salida=args[2];
            if (args.length>3) busqueda=args[3];
        }

        System.out.println("Puzzle Inicial:\n"+puzzle);
        
        //Miramos si el puzzle tiene soluciÃ³n
        if (!puzzle.resoluble()){
	    System.out.println("EL puzzle NO tiene solución\n");
	    System.exit(0);
	} else  System.out.println("EL puzzle Sí tiene solución\n");
        
        if (busqueda == "Aleatoria")
            puzzle.launch_aleatoria(salida);
        else if (busqueda == "BPP")
            puzzle.launch_BPP(salida);
        else if (busqueda == "BPA")
            puzzle.launch_BPA(salida);
        else if (busqueda == "BPPI")
            puzzle.launch_BPP_iterativa(salida);
        else if (busqueda == "ES")
            puzzle.launch_escalada_simple(salida);
        else if (busqueda == "EMP")
            puzzle.launch_escalada_maxima(salida);
        else if (busqueda == "BPM")
            puzzle.launch_BPM(salida);
        else
            puzzle.launch_escalada_simple(salida);
    }//End funciÃ³n main
}
