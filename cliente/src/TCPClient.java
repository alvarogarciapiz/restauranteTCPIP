import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.util.Random;

public class TCPClient {
	public static void main (String args[]) {
		Socket s = null;
		int opcion; //Para leer la opcion del menu
        Scanner sc = new Scanner(System.in);
        
        
        //COMUNICACION
		try{
			
			String pedidosActivos="";
			int serverPort = 7896;
			s = new Socket("localhost", serverPort);    //local host es la direccion IP
			DataInputStream in = new DataInputStream( s.getInputStream());
			DataOutputStream out =new DataOutputStream( s.getOutputStream());
			//out.writeUTF(args[0]); //Escribe en su buffer de salida lo que recibe como argumentos     	
		
		do {
            //Menu
            System.out.println(" __________________________________________________");
            System.out.println("|                                                  |");
            System.out.println("| Introduce el numero de la opcion para ejecutar:  |");
            System.out.println("| 1. Consultar disponibilidad del plato.           |");
            System.out.println("| 2. Solicitud de plato.                           |");
            System.out.println("| 3. Anulacion de plato.                           |");
            System.out.println("| 4. Actualizacion cantidad ingredientes.          |");
            System.out.println("| 5. Consultar el menu.                            |");
            System.out.println("|__________________________________________________|");
            System.out.println("| 0. Salir del restaurante.                        |");
            System.out.println("|__________________________________________________|");
            opcion = sc.nextInt();
            
                switch (opcion) {
                    
                    case 1: //1. Consultar disponibilidad del plato
                    	int i,l=0;
	                    String cas1= consultarDisponibilidad();
	                    while(cas1.equals("04")) {
	                    	cas1= consultarDisponibilidad();
	                    }
	                    
	                    // Primero se llama al servidor para saber la composicion del plato
	                    cas1 = "1/" + cas1;
	                    out.writeUTF(cas1);
	                    
	                    String infoPlatos = in.readUTF();
	                    String[] ingredientesYError = infoPlatos.split("-");
	                    
	                    /* En ingredientesYError tenemos los ingredientes + cantidades + el c—digo de error
	                     * mientras que en ingredientes tenemos las cantidades y no el error */
	                    
	                    if(ingredientesYError[1].equals("0")) {
	                    	System.out.println("Error al consultar la disponibilidad del plato");
	                    } else {
	                    	String[] ingredientes = ingredientesYError[0].split(" ");
	                    	int numeroIngredientes = Integer.parseInt(ingredientes[0]); 
	                    	
	                    	for (i=1; i<numeroIngredientes*2; i+=2) {
	                    		/* En la posici—n i tenemos el ID del ingrediente y en la posici—n i+1 la cantidad necesaria
	                    		 Hago una llamada al servidor por cada ingrediente para comprobar su disponibilidad */
	                    		
	                    		
	                    		String checkIngrediente = "7/" + ingredientes[i];
	                    		out.writeUTF(checkIngrediente);
	                    		
	                    		String disponibilidadIngrediente = in.readUTF();
	                    		
	                    		String[] disponibilidadYError = disponibilidadIngrediente.split(" ");
	                    		String error = disponibilidadYError[1];
	                    		int qIngredienteNecesaria = Integer.parseInt(ingredientes[i+1]);
	                    		int qIngrediente = Integer.parseInt(disponibilidadYError[0]); // la cantidad que tengo del ingrediente
	                    		
	                    		if(!error.equals("0") && qIngrediente>=qIngredienteNecesaria) {
	                    			l++; // El contador aumenta si no hay error al leer el ingrediente y la cantidad en stock es superior a la necesaria para elaborar el plato
	                    		}
	                    		
	                    	}
	                    	//Si el contador 'l' es igual al nœmero de ingredientes significa que el plato se encuentra disponible y por tanto se avisa al usuario
	                    	if(l==numeroIngredientes) {
                    			System.out.println("El plato se encuentra disponible \n");
                    		} else {
                    			System.out.println("El plato no se encuentra disponible \n");
                    		}
	                    }    
                    break;
                    
                    case 2: //2. Solicitud de plato
                    	l=0;
                    	int b=0;
	                    String cas2 = hacerPedido();
	                    while(cas2.equals("04")) {
	                    	cas2= hacerPedido(); //En cas2 se tiene el id del plato que se desea solicitar
	                    }
	                    
	                    String pedidoCopia = cas2;
	                    
	                 // Primero se llama al servidor para saber la composici—n del plato
	                    cas2 = "1/" + cas2;
	                    out.writeUTF(cas2);
	                    
	                    infoPlatos = in.readUTF();
	                    ingredientesYError = infoPlatos.split("-");
	                    
	                    /* En ingredientesYError tenemos los ingredientes + cantidades + el c—digo de error
	                     * mientras que en ingredientes tenemos las cantidades y no el error */
	                    
	                    if(ingredientesYError[1].equals("0")) {
	                    	System.out.println("Error al consultar la disponibilidad del plato");
	                    } else {
	                    	String[] ingredientes = ingredientesYError[0].split(" ");
	                    	int numeroIngredientes = Integer.parseInt(ingredientes[0]); 
	                    	
	                    	for (i=1; i<numeroIngredientes*2; i+=2) {
	                    		/* En la posici—n i tenemos el ID del ingrediente y en la posici—n i+1 la cantidad necesaria
	                    		 Hago una llamada al servidor por cada ingrediente para comprobar su disponibilidad */
	                    		
	                    		
	                    		String checkIngrediente = "7/" + ingredientes[i];
	                    		out.writeUTF(checkIngrediente);
	                    		
	                    		String disponibilidadIngrediente = in.readUTF();
	                    		
	                    		String[] disponibilidadYError = disponibilidadIngrediente.split(" ");
	                    		String error = disponibilidadYError[1];
	                    		int qIngredienteNecesaria = Integer.parseInt(ingredientes[i+1]);
	                    		int qIngrediente = Integer.parseInt(disponibilidadYError[0]); // la cantidad que tengo del ingrediente                  		
	                    		
	                    		if(!error.equals("0") && qIngrediente>=qIngredienteNecesaria) {
	                    			l++; // El contador aumenta si no hay error al leer el ingrediente y la cantidad en stock es superior a la necesaria para elaborar el plato
	                    			
	                    			
	                    			out.writeUTF("4/" + ingredientes[i] + " " + qIngredienteNecesaria); //    4/ + ID del Ingrediente + Cantidad del ingrediente
	                    			String errorEscritura = in.readUTF();
	                    			if(!errorEscritura.equals("0")) {
	                    				b++;
	                    			}
	                    		}
	                    		
	                    	}
	                    	//Si el contador 'l' es igual al nœmero de ingredientes significa que el plato se encuentra disponible y se ha pedido correctamente
	                    	if(l==numeroIngredientes && b ==numeroIngredientes) {
                    			System.out.println("El plato fue pedido correctamente, disfrute de su pedido. \n");
                    		} else if(b!=numeroIngredientes){
                    			System.out.println("El plato se encuentra disponible pero hubo un error al solicitarlo, pruebe de nuevo.\n");
                    		} else if(l!=numeroIngredientes) {
                    			System.out.println("No hay ingredientes suficientes para confeccionar el plato\n");
                    		}
	                    }
	                    pedidosActivos = pedidosActivos + pedidoCopia + " ";
                    break;
                    
                    case 3: //3. Anulacion de plato
                    	
                    	String anulacion = anularPedido(pedidosActivos);
                    	if(anulacion.equals("0")) {
                    		System.out.println("No hay ningun pedido activo. ");
                    	} else {        	
                    	l=0;
                    	b=0;
	                    cas2 = anulacion;
	                    
	                    
	                 // Primero se llama al servidor para saber la composici—n del plato
	                    cas2 = "1/" + cas2;
	                    out.writeUTF(cas2);
	                    
	                    infoPlatos = in.readUTF();
	                    ingredientesYError = infoPlatos.split("-");
	                    
	                    /* En ingredientesYError tenemos los ingredientes + cantidades + el c—digo de error
	                     * mientras que en ingredientes tenemos las cantidades y no el error */
	                    
	                    if(ingredientesYError[1].equals("0")) {
	                    	System.out.println("Error al consultar la disponibilidad del plato");
	                    } else {
	                    	String[] ingredientes = ingredientesYError[0].split(" ");
	                    	int numeroIngredientes = Integer.parseInt(ingredientes[0]); 
	                    	
	                    	for (i=1; i<numeroIngredientes*2; i+=2) {
	                    		/* En la posici—n i tenemos el ID del ingrediente y en la posici—n i+1 la cantidad necesaria
	                    		 Hago una llamada al servidor por cada ingrediente para comprobar su disponibilidad */
	                    		
	                    		
	                    		String checkIngrediente = "7/" + ingredientes[i];
	                    		out.writeUTF(checkIngrediente);
	                    		
	                    		String disponibilidadIngrediente = in.readUTF();
	                    		
	                    		String[] disponibilidadYError = disponibilidadIngrediente.split(" ");
	                    		String error = disponibilidadYError[1];
	                    		int qIngredienteNecesaria = Integer.parseInt(ingredientes[i+1]);
	                    		int qIngrediente = Integer.parseInt(disponibilidadYError[0]); // la cantidad que tengo del ingrediente                  		
	                    		
	                    		if(!error.equals("0") && qIngrediente>=qIngredienteNecesaria) {
	                    			l++; // El contador aumenta si no hay error al leer el ingrediente y la cantidad en stock es superior a la necesaria para elaborar el plato
	                    			
	                    			
	                    			out.writeUTF("4/" + ingredientes[i] + " " + "-" + qIngredienteNecesaria); //    4/ + ID del Ingrediente + Cantidad del ingrediente
	                    			String errorEscritura = in.readUTF();
	                    			if(!errorEscritura.equals("0")) {
	                    				b++;
	                    			}
	                    		}
	                    		
	                    	}
	                    	//Si el contador 'l' es igual al nœmero de ingredientes significa que el plato se encuentra disponible y se ha pedido correctamente
	                    	if(l==numeroIngredientes && b ==numeroIngredientes) {
                    			System.out.println("El plato fue anulado correctamente, vuelva pronto. \n");
                    		} else if(b!=numeroIngredientes){
                    			System.out.println("Error al cancelar su pedido.\n");
                    		} else if(l!=numeroIngredientes) {
                    			System.out.println("Error al cancelar su pedido.\n");
                    		}
	                    }
                    	}
                    break;
                    
                    case 4: //4. Actualizacion cantidad ingredientes.
                    	
                    	//0. Se obtiene una clave aleatoria para gestionar los bloqueos
                    	int clave = generadorClave();
                    	System.out.println("La clave es: " + clave);
                    	
                    	
                    	String cas4 = actualizarIngredientes();
                    	String[] parts = cas4.split(" ");
                    	
                    	String idIngrediente = parts[0];
                    	String cantidadIngrediente = parts[1];
                    	
                    	//1. Se bloquea el ingrediente a actualizar
                    	out.writeUTF("5/" + idIngrediente + "/" + clave);
                    	String errorID = in.readUTF();
                    	if(!errorID.equals("1")) {
                    		System.out.println("Error al bloquear el ingrediente");
                    	}
                    	
                    	
                    	//2. Se actualiza la cantidad del ingrediente
                    	out.writeUTF("2/" + cas4 + "/" + clave);
                    	String errorAct = in.readUTF();
                    	if(errorAct.equals("0")) {
                    		System.out.println("Error al escribir el ingrediente");
                    	}
                    	
                    	
                    	//3. Se desbloquea el ingrediente
                    	out.writeUTF("6/" + idIngrediente);
                    	String errorIDdes = in.readUTF();
                    	if(!errorIDdes.equals("1")) {
                    		System.out.println("Error al desbloquear el ingrediente");
                    	}
                    	
                    	if(errorIDdes.equals("1") && !errorAct.equals("0") && errorID.equals("1")) {
                    		System.out.println("La cantidad del ingrediente " + idIngrediente + " fue actualizada correctamente.");
                    	}
	                    
                    break;
                    
                    case 5: //5. Consultar el menu
                    verPlatos();
                    break;
                    
                    case 0: //0. Salir del programa
                    	out.writeUTF("0");
                    	
                    	String errorExit = in.readUTF();
                    	if(errorExit.equals("0")) {
                    		System.exit(0);
                    		opcion = 0;
                    	} else {
                    		System.out.println("Error al cerrar el servidor. ");
                    	}
                    	
                    break;
                    
                    default:
                    System.out.println("[ERROR] Introduce una opcion valida.");
                }
            } while (opcion!=0);
		
		
	}catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
	}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
	}catch (IOException e){System.out.println("El restaurante se encuentra cerrado.\nAbre el restaurante para poder utilizarlo.");
	}finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
		
     }
	
	/* Metodo para comprobar la disponibilidad de los platos */
    public static String consultarDisponibilidad(){
    	String func="";
    	System.out.println("Desea ver primero los platos que ofrecemos? \n Introduce un '0' para viualizarlos, sino cualquier otro numero. ");
    	Scanner ss = new Scanner(System.in);
    	Scanner sp = new Scanner(System.in);
    	int pl = ss.nextInt();
    	if(pl==0) {
    		verPlatos();
    	}
        System.out.println("Introduce el ID del plato del que desea conocer su disponibilidad. [00-01-02-03]");
        String platoElegido = sp.nextLine();
        
        if(platoElegido.equals("00")){ //Si es 00 es Kebab
        	System.out.println("Has seleccionado: Kebab");
        	func="00";
        }
        else if(platoElegido.equals("01")){ //Si es 01 es Durum
        	System.out.println("Has seleccionado: Durum");
        	func="01";
        }
        else if(platoElegido.equals("02")){ // Si es 02 es falafel
        	System.out.println("Has seleccionado: Falafel");
        	func="02";
        }
        else if(platoElegido.equals("03")){ // Si es 03 es Lahmacun
        	System.out.println("Has seleccionado: Lahmacun");
        	func="03";
        }
        else {
        	System.out.println("[ERROR] Introduce una opcion valida.");
        	func="04";
        }
        
        return func;
    }
    
    
    /* Metodo para solicitar un plato (hacer pedido) */
    public static String hacerPedido(){
    	String func="";
    	Scanner ss = new Scanner(System.in);
    	Scanner sp = new Scanner(System.in);
    	
    	System.out.println("Desea ver primero los platos que ofrecemos? \n Introduce un '0' para viualizarlos, sino cualquier otro numero. ");
    	int pl = ss.nextInt();
    	if(pl==0) {
    		verPlatos();
    	}
        System.out.println("Introduce el ID del plato que desea solicitar. [00-01-02-03]");
        String platoElegido = sp.nextLine();
        
        if(platoElegido.equals("00")){ //Si es 00 es Kebab
        	func="00";
        }
        else if(platoElegido.equals("01")){ //Si es 01 es Durum
        	func="01";
        }
        else if(platoElegido.equals("02")){ // Si es 02 es falafel
        	func="02";
        }
        else if(platoElegido.equals("03")){ // Si es 03 es Lahmacun
        	func="03";
        }
        else {
        	System.out.println("[ERROR] Introduce una opcion valida.");
        	func="04";
        }
        System.out.println("has seleccionado el plato " + func + ". Su pedido se precesar‡ en breve.");
        return func;
    }
    
    /* Metodo para anular un pedido */
    public static String anularPedido(String pedidosActivos) {
    	int g=0, gg=0;
    	String pedidos[] = pedidosActivos.split(" ");
    	Scanner sq = new Scanner(System.in);
    	String pedidoEliminar = "";
    	
    	int s=0;
    	
    	if(pedidosActivos.equals("")) {
    		System.out.println("No se ha realizado ningun pedido. No se puede anular nada. haz un pedido para poder anularlo.");
    		pedidoEliminar = "0";
    	}
    	else {
    	System.out.println("Los pedidos que se encuentran en proceso son los siguientes: ");
    	for (s=0; s<pedidos.length; s++) {
    		System.out.println(pedidos[s]);
    	}
	    	while (g==0) {
	    		System.out.println("Introduce el ID de un pedido de la lista: ");
	        	pedidoEliminar = sq.nextLine();
	        	
	        	for (gg=0; gg<pedidos.length; gg++) {
		        	if(pedidoEliminar.equals(pedidos[gg])) {
		        		g++;
		        	}
	        	}
	    	}
    	
    	}
    	
    	return pedidoEliminar; //Si devuelve un 0 es que no hay pedidos activos, sino devuelve el ID del pedido
    }
    
    
    
    /* Metodo para actualizar ingredientes */
    public static String actualizarIngredientes(){
    	Scanner sp = new Scanner(System.in);
    	Scanner p = new Scanner(System.in);
    	String ingrediente="";
    	
        System.out.println("Introduce el ID del ingrediente que deseas actualizar.");
        System.out.println("[10 -> Pan]  [11 -> Carne]  [12 -> lechuga]  [13 -> Tomate]  [14 -> Salsa]  [15 -> Cebolla]  [16 -> Garbanzos]  [17 -> Base para Pizza]  [18 -> Pimiento]  [19 -> Ajo]  [20 -> Pan en tortilla]");
        String IDIngrediente = sp.nextLine();
        
        System.out.println("Introduce la cantidad que vas a agregar del ingrediente: ");
        String QIngrediente = sp.nextLine();
        
        ingrediente = IDIngrediente + " " + QIngrediente;
        return ingrediente;
    }
    
    
    /* Metodo para ver los platos y su descripcion */
    public static void verPlatos() {
    	System.out.println("A continuacion se muestra el menu: ");
        System.out.println(" [KEBAB -> 00] Pan con carne, lechuga tomate y salsa.");
        System.out.println(" [DURUM -> 01] Carne, lechuga, tomate y salsa en una tortilla de pan.");
        System.out.println(" [FALAFEL -> 02] Croquetas de garbanzos, cebolla, ajo y pimiento.");
        System.out.println(" [LAHMACUN -> 03] Pizza turca con pimiento tomate y cebolla sobre una base de pan.");
		try {
			Thread.sleep(500); //Se visualiza medio segundo al menos antes de continuar
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    //Metodo que genera un clave aleatoria
    public static int generadorClave() {
    	Random rand = new Random();
        int limiteSuperior = 2000;
          //genera valores aleatorios entre 0 y 2000
        int clave = rand.nextInt(limiteSuperior); 

        return clave;
        
    }
	
	
}