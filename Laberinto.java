/* Georgina González Enríquez A01632886
 * Clase Laberinto
 * 4/06/2018*/
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Laberinto {
	
	private Point inicio,
				  fin;
	private boolean laberinto[][];//Representa el mapa del laberinto false: camino libre, true: pared.
	private boolean pasado[][][]; /*Representa los lugares por los que se ha pasado en el laberinto. 
	                                Ese arreglo es tridimensional. En la tercer dimensión la capa 0 es para indicar si ya se visitó ese camino o no. 
	                                false: no se ha visitado, true: ya se visitó. 
	                                La capa uno sirve para guardar si esa posición pertenece al camino correcto para llegar al final del laberinto.*/
	/*Entrada: 
	 * Archivo txt donde los 1’s indicarán que ahí hay pared y los 0’s significan que es espacio libre. 
	 * Las primeras dos filas del archivo indicarán la entrada y salida al laberinto respectivamente. 
	 * La tercer fila indicará las dimensiones del laberinto.*/
	/* Salida:
	 * I: inicio, F: final, P: Pared, X: Visitado pero no pertenece al camino correcto, Y: Es parte del camino correcto, 
	 * Espacio en blanco, simplemente es una casilla del laberinto que no es pared pero tampoco fue necesario visitarla. */
	
	public Laberinto(String ruta) {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(ruta));
			String linea = bf.readLine();
			StringTokenizer st = new StringTokenizer(linea, " ");
			this.inicio = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
			linea = bf.readLine();
			st = new StringTokenizer(linea," ");
			this.fin = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
			linea = bf.readLine();
			st = new StringTokenizer(linea, " ");
			int filas = Integer.parseInt(st.nextToken()),
				columnas = Integer.parseInt(st.nextToken());
			this.laberinto = new boolean[filas][columnas];
			this.pasado = new boolean[filas][columnas][2];
			for(int i = 0; i < this.laberinto.length; i++) {
				linea = bf.readLine();
				for(int j = 0; j < this.laberinto[i].length; j++) {
					if(linea.charAt(j) == '1') {
						this.laberinto[i][j] = true;//hay pared
					}
					else {
						this.laberinto[i][j] = false;//camino libre
					}
				}//recorro columnas
			}//recorro filas
			bf.close();
			for(int i = 0; i < this.pasado.length;i++) {
				for(int j = 0; j < this.pasado[i].length; j++) {
					this.pasado[i][j][1] = true;
				}
			}
			System.out.println("Entrada:");
			imprimirLaberinto();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public void buscar() {
		buscar((int)this.inicio.getX(),(int)this.inicio.getY());
	}
	
	public boolean buscar(int f, int c) {
		pasado[f][c][0] = true;//marca celda como visitada
		if(f == (int)this.fin.getX() && c == (int)this.fin.getY()) {
			System.out.println("Salida:");
			imprimirPasado();
			guardarPasado();
			return true;
		}//ya llegó al final
		else {
			if(validar(f,c+1)) {
				buscar(f,c+1);
			}//camino disponible a la derecha
			if(validar(f,c-1)) {
				buscar(f,c-1);
			}//camino disponible a la izquierda
			if(validar(f+1,c)) {
				buscar(f+1,c);
			}//camino disponible abajo
			if(validar(f-1,c)) {
				buscar(f-1,c);
			}//camino disponible arriba
			return this.pasado[f][c][1] = false;//no es el camino
		}//busca camino disponible
	} 
	
	private boolean validar(int f, int c) {
		if(f < 0 || f >= this.laberinto.length) {
			return false;
		}//si no es una fila válida
		if(c < 0 || c >= this.laberinto[f].length) {
			return false;
		}//si no es una columna válida
		if(this.laberinto[f][c] == true) {
			return false;
		}//si es una pared
		if(this.pasado[f][c][0] == true) {
			return false;
		}//si ya se visitó
		return true;
	}
	
	public void guardarPasado() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("Salida.txt"));
			for(int i = 0; i < this.pasado.length;i++) {
				for(int j = 0; j < this.pasado[i].length; j++) {
					if(i == (int)this.inicio.getX() && j == (int)this.inicio.getY()) {
						pw.print("I" + ",");
					}
					else if(i == (int)this.fin.getX() && j == (int)this.fin.getY()) {
						pw.print("F" + ",");
					}
					else if(this.laberinto[i][j] == true) {
						pw.print("P" + ",");
					}
					else if(this.pasado[i][j][0] == true && this.pasado[i][j][1] == false) {
						pw.print("X" + ",");
					}
					else if(this.pasado[i][j][0] == true && this.pasado[i][j][1] == true) {
						pw.print("Y" + ",");
					}
					else {
						pw.print(" " + ",");
					}
				}
				pw.println();
			}
			pw.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public void imprimirPasado() {
		for(int i = 0; i < this.pasado.length;i++) {
			for(int j = 0; j < this.pasado[i].length; j++) {
				if(i == (int)this.inicio.getX() && j == (int)this.inicio.getY()) {
					System.out.print("I" + ",");
				}
				else if(i == (int)this.fin.getX() && j == (int)this.fin.getY()) {
					System.out.print("F" + ",");
				}
				else if(this.laberinto[i][j] == true) {
					System.out.print("P" + ",");
				}
				else if(this.pasado[i][j][0] == true && this.pasado[i][j][1] == false) {
					System.out.print("X" + ",");
				}
				else if(this.pasado[i][j][0] == true && this.pasado[i][j][1] == true) {
					System.out.print("Y" + ",");
				}
				else {
					System.out.print(" " + ",");
				}
			}
			System.out.println();
		}
	}
	
	public void imprimirLaberinto() {
		System.out.print("Inicio(" + (int)this.inicio.getX() + "," + (int)this.inicio.getY() + ") ");
		System.out.println("Fin(" + (int)this.fin.getX() + "," + (int)this.fin.getY() + ")");
		for(int i = 0; i < this.laberinto.length; i++) {
			for(int j = 0; j < this.laberinto[i].length ; j++) {
				if(i == (int)this.inicio.getX() && j == (int)this.inicio.getY()) {
					System.out.print("I");
				}
				else if(i == (int)this.fin.getX() && j == (int)this.fin.getY()) {
					System.out.print("F");
				}
				else if(this.laberinto[i][j] == true) {
					System.out.print("1");
				}
				else {
					System.out.print("0");
				}
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Laberinto laberinto = new Laberinto("EjemploEntrada.txt");
		laberinto.buscar();
	}

}
