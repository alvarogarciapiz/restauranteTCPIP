import java.net.*;
import java.io.*;

public class TCPServer {
	
	public static String ficheroIngredientes;
	public static String ficheroPlatos;
	public static int clave=0;
	//Ingredientes (al leer el fichero se actualizan sus cantidades).
    public static int pan=0, carne=0, lechuga=0, tomate=0, salsa=0, tortilla=0, garbanzos=0, cebolla=0, ajo=0, pimiento=0, basePizza=0;
    
    // True si el ingrediente esta bloqueado y false si no esta bloqueados
    public static boolean panB=false, carneB=false, lechugaB=false, tomateB=false, salsaB=false, tortillaB=false, garbanzosB=false, cebollaB=false, ajoB=false, pimientoB=false, basePizzaB=false;
    
	public static void main (String args[]) {
		
		// Se asocian los nombres de los ficheros pasados por argumento a dos variables
		ficheroIngredientes = args[0];
		ficheroPlatos = args[1];
		
		//Se lee el stock de ingredientes y se actualizan sus valores
		Ficheros.leerIngredientes(ficheroIngredientes);
		
	    Ficheros.leerPlatos(ficheroPlatos); //En cada clase se actualizan los valores correspondientes a los ingredientes y cantidades 
		
		try{ // Creo el socket y espero a que se conecte
			int serverPort = 7896; 
			ServerSocket listenSocket = new ServerSocket(serverPort);
			while(true) {
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket);
				/* Al finalziar el programa se vuelcan de nuevo los datos al fichero
			    para mantener actualizadas sus cantidades */
			    Ficheros.actualizarIngredientesFichero(ficheroIngredientes);
			}
		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
		
			
	} //Fin main
	
	// -------------------------------- Metodos complementarios ----------------------------------
	
	/* Terminar el servicio ------------------------------------------------------------------------ */
	public static int termina_servicio() {
		int errorExit;
		System.exit(0);
		return 0;
	}
	
	
	/* Metodo que devuelve informacion sobre el plato ------------------------------------------------*/
    public static String consulta_plato(int id){
    	int i=0, result=0;
    	String plato="", s="";
    	if(id==00) { //Kebab
    		plato = String.format("%s 10 %s 11 %s 12 %s 13 %s 14 %s", Kebab.numIngredientes, Kebab.pan, Kebab.carne, Kebab.lechuga, Kebab.tomate, Kebab.salsa);
        	i++;
        }
        else if(id==01){ //Durum
        	plato = String.format("%s 20 %s 11 %s 12 %s 13 %s 14 %s", Durum.numIngredientes, Durum.tortilla, Durum.carne, Durum.lechuga, Durum.tomate, Durum.salsa);
        	i++;
        }
        else if(id==02){ //Falafel
        	plato = String.format("%s 15 %s 16 %s 18 %s 19 %s", Falafel.numIngredientes, Falafel.cebolla, Falafel.garbanzos, Falafel.pimiento, Falafel.ajo);
        	i++;
        }
        else if(id==03){ //Lahmacun
        	plato = String.format("%s 13 %s 15 %s 17 %s 18 %s", Lahmacun.numIngredientes, Lahmacun.tomate, Lahmacun.cebolla, Lahmacun.basePizza, Lahmacun.pimiento);
        	i++;
        }
    	
    	s = plato + "-" + i; //Se obtiene el numero de ingredientes + identificadores + cantidade + error
    	return s;
    }
    
	
	/* Metodo que devuelve la cantidad de ingredientes disponible ----------------------------------------------------*/
    public static String lee_ingrediente(int id){
    	int cantidad = 0, i=0;
    	String cantidadResult="";
    	
    	if(id==10) {
        	cantidad=pan;
        	i++;
        }
        else if(id==11){
        	cantidad=carne;
        	i++;
        }
        else if(id==12){
        	cantidad=lechuga;
        	i++;
        }
        else if(id==13){
        	cantidad=tomate;
        	i++;
        }
        else if(id==14){
        	cantidad=salsa;
        	i++;
        }
        else if(id==15){
        	cantidad=cebolla;
        	i++;
        }
        else if(id==16){
        	cantidad=garbanzos;
        	i++;
        }
        else if(id==17){
        	cantidad=basePizza;
        	i++;
        }
        else if(id==18){
        	cantidad=pimiento;
        	i++;
        }
        else if(id==19){
        	cantidad=ajo;
        	i++;
        }
        else if(id==20){
        	cantidad=tortilla;
        	i++;
        }
    	
    	cantidadResult = String.valueOf(cantidad) + " " + String.valueOf(i); //cantidad result
    	return cantidadResult;
    }
	
	
	/* Metodo que cambia la cantidad de ingredientes disponible, para cada ingrediente se comprueba que el ingrediente no esta bloqueado */
    public static String escribe_ingrediente(int id, int cantidad){
    	String dev="";
    	int i=0;
    	
    	if(id==10 && panB==false) {
        	pan-=cantidad;
        	i++;
        }
        else if(id==11 && carneB==false){
        	carne-=cantidad;
        	i++;
        }
        else if(id==12 && lechugaB==false){
        	lechuga-=cantidad;
        	i++;
        }
        else if(id==13 && tomateB==false){
        	tomate-=cantidad;
        	i++;
        }
        else if(id==14 && salsaB==false){
        	salsa-=cantidad;
        	i++;
        }
        else if(id==15 && cebollaB==false){
        	cebolla-=cantidad;
        	i++;
        }
        else if(id==16 && garbanzosB==false){
        	garbanzos-=cantidad;
        	i++;
        }
        else if(id==17 && basePizzaB==false){
        	basePizza-=cantidad;
        	i++;
        }
        else if(id==18 && pimientoB==false){
        	pimiento-=cantidad;
        	i++;
        }
        else if(id==19 && ajoB==false){
        	ajo-=cantidad;
        	i++;
        }
        else if(id==20 && tortillaB==false){
        	tortilla-=cantidad;
        	i++;
        }
    	
    	dev = String.valueOf(i); // Si dev==0 es que hay un error, si por el contrario es 1 todo bien
    	Ficheros.actualizarIngredientesFichero(ficheroIngredientes);
    	return dev;
    }	
	
	
    /* Metodo que bloquea el ingrediente seleccionado  --------------------------------------- */
    public static int bloquea_ingrediente(int id){
    	int result=0, i=0;
    	
    	
    	if(id==10 && panB==false) {
        	panB=true;
        	i++;
        }
        else if(id==11 && carneB==false){
        	carneB=true;
        	i++;
        }
        else if(id==12 && lechugaB==false){
        	lechugaB=true;
        	i++;
        }
        else if(id==13 && tomateB==false){
        	tomateB=true;
        	i++;
        }
        else if(id==14 && salsaB==false){
        	salsaB=true;
        	i++;
        }
        else if(id==15 && cebollaB==false){
        	cebollaB=true;
        	i++;
        }
        else if(id==16 && garbanzosB==false){
        	garbanzosB=true;
        	i++;
        }
        else if(id==17 && basePizzaB==false){
        	basePizzaB=true;
        	i++;
        }
        else if(id==18 && pimientoB==false){
        	pimientoB=true;
        	i++;
        }
        else if(id==19 && ajoB==false){
        	ajoB=true;
        	i++;
        }
        else if(id==20 && tortillaB==false){
        	tortillaB=true;
        	i++;
        }
    	
    	return i; //retorna '1' si va todo bien
    	
    	
    }
    
    
    /* Metodo que desbloquea el ingrediente seleccionado -------------------------------------------------*/
    public static int desbloquea_ingrediente(int id){
    	int result=0, i=0;
    	
    	
    	if(id==10 && panB==true) {
        	panB=false;
        	i++;
        }
        else if(id==11 && carneB==true){
        	carneB=false;
        	i++;
        }
        else if(id==12 && lechugaB==true){
        	lechugaB=false;
        	i++;
        }
        else if(id==13 && tomateB==true){
        	tomateB=false;
        	i++;
        }
        else if(id==14 && salsaB==true){
        	salsaB=false;
        	i++;
        }
        else if(id==15 && cebollaB==true){
        	cebollaB=false;
        	i++;
        }
        else if(id==16 && garbanzosB==true){
        	garbanzosB=false;
        	i++;
        }
        else if(id==17 && basePizzaB==true){
        	basePizzaB=false;
        	i++;
        }
        else if(id==18 && pimientoB==true){
        	pimientoB=false;
        	i++;
        }
        else if(id==19 && ajoB==true){
        	ajoB=false;
        	i++;
        }
        else if(id==20 && tortillaB==true){
        	tortillaB=false;
        	i++;
        }
    	
    	return i; //retorna '1' si va todo bien
    	
    	
    }
	
	
}

//Por cada cliente que me haga una solicitud preparo un hilo
class Connection extends Thread {
	DataInputStream in; //Entrada
	DataOutputStream out; //Salida
	Socket clientSocket;
	public Connection (Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream( clientSocket.getInputStream());
			out =new DataOutputStream( clientSocket.getOutputStream());
			this.start(); //Llama a run
		} catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}
	public void run(){
		try {			                 
			//----------------------------------------
			while (true) {
			String data = in.readUTF();
			
			 String[] parts = data.split("/");
			
			while(!parts[0].equals("0")) {
				
				switch (parts[0]) {
		            case "1": // Consulta plato
		            	String plato = TCPServer.consulta_plato(Integer.parseInt(parts[1]));
		            	out.writeUTF(plato);
		            	parts[0]="0";	
		            break;
		            
		            
		            case "2": //Escribe ingrediente en el caso de actualizaci—n de ingredientes
		            	
		            	String[] partsK = parts[1].split(" "); //parts2[0] es el ID del ingrediente y parts2[1] es la cantidad
		            	String errorK = "";
		            	int idK = Integer.parseInt(partsK[0]);
		            	int qK = Integer.parseInt(partsK[1]);
		            	qK = -qK; //Para que a–ade y no reste al pasarle a la funci—n escribe_ingrediente
		            	
		            	/* Si la clave del cliente coincide con la que nos pas— significa que es el mismo cliente por lo que
		            	 desbloqueamos primero el ingrediente, lo escribimos y lo volvemos a bloquear */
		            	if (parts[2]!=null) {
		            	if(TCPServer.clave==Integer.parseInt(parts[2])) {
		            		
		            		int errorDesbloq = TCPServer.desbloquea_ingrediente(idK); //Primero desbloqueamos moment‡namente
		            		
		            		errorK = TCPServer.escribe_ingrediente(idK, qK);
		            		
		            		int errorBloq = TCPServer.bloquea_ingrediente(idK); //Volvemos a bloquear
		            		
		            	} else {
		            		errorK = "0";
		            	}
						} else {
							errorK = TCPServer.escribe_ingrediente(idK, qK);
						}
		            	out.writeUTF(errorK);
		            	parts[0]="0";
		            break;
		            
		            
		            case "4": //Escribe ingrediente
		            	
		            	String[] parts2 = parts[1].split(" "); //parts2[0] es el ID del ingrediente y parts2[1] es la cantidad
		            	String error = "";
		            	int id = Integer.parseInt(parts2[0]);
		            	int q = Integer.parseInt(parts2[1]);
		            	
							error = TCPServer.escribe_ingrediente(id, q);
						
		            	out.writeUTF(error);
		            	parts[0]="0";
		            break;
		            
		            
		            case "5": //Bloquea un ingrediente
		            	
		            	TCPServer.clave = Integer.parseInt(parts[2]); //Almacenamos la clave que nos pasa el cliente
		            	
		            	
		            	int ID = Integer.parseInt(parts[1]);
		            	int errorID = TCPServer.bloquea_ingrediente(ID);
		            	
		            	out.writeUTF(String.valueOf(errorID)); // Si es '1' se bloquea correctamente
		            	
		            	parts[0]="0";
		            break;
		            
		            case "6": //Desbloquea un ingrediente
		            	
		            	int IDdes = Integer.parseInt(parts[1]);
		            	int errorIDdes = TCPServer.desbloquea_ingrediente(IDdes);
		            	
		            	out.writeUTF(String.valueOf(errorIDdes)); // Si es '1' se bloquea correctamente
		            	
		            	parts[0]="0";
		            break;
		            
		            case "7": //Lee ingredente
		            	String backIngrediente = TCPServer.lee_ingrediente(Integer.parseInt(parts[1]));
		            	out.writeUTF(backIngrediente); // cantidad + " " + result    -> si result es 0 es que hay fallo
		            	parts[0]="0";
		            break;
		            
		            case "0": //Termina servicio
		            	int errorExit = TCPServer.termina_servicio();
		            	if(errorExit == 0) {
		            		out.writeUTF("0"); //Ejecuci—n correcta
		            	} else {
		            		out.writeUTF("1"); //Error al finalizar el Servidor
		            	}
		            	
		            break;
		            
		            default:
		            	System.out.println("numero incorrecto");
		            break;
		        }
				
			}
			} //Fin while(true)
			//--------------------------------------------------------
				                  
		}catch (EOFException e){System.out.println("EOF:"+e.getMessage());
		} catch(IOException e) {System.out.println("readline:"+e.getMessage());
		} finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
		

	}
}