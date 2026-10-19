package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.Arrays;
import java.util.List;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

public class PruebasBiblioteca {
    private static int total = 0;
    private static int correctas = 0;

    public static void main(String[] args) {
        ArbolBiblioteca soloRaiz = DatosPrueba.crearBibliotecaVacia();
        verificar("Solo raíz: altura 0", soloRaiz.altura() == 0);
        verificar("Solo raíz: 1 hoja", soloRaiz.contarHojas() == 1);

        ArbolBiblioteca inicial = DatosPrueba.crearBibliotecaInicial();
        verificar("Inicial: 7 unidades", inicial.contarUnidades() == 7);
        verificar("Inicial: altura 2", inicial.altura() == 2);
        verificar("Inicial: 4 hojas", inicial.contarHojas() == 4);
        verificar("Inicial: 13 libros", inicial.contarLibrosTotal() == 13);

        verificar("Nivel de BIB es 0", inicial.nivel("BIB") == 0);
        verificar("Nivel de sw es 1", inicial.nivel("sw") == 1);
        verificar("Nivel de SW-E2 es 2", inicial.nivel("SW-E2") == 2);
        verificar("Nivel de XYZ es -1", inicial.nivel("XYZ") == -1);

        verificar("Agregar ESTANTE bajo BIBLIOTECA es false",
                !inicial.agregar("BIB", new UnidadBiblioteca("E-X", "Estante suelto", TipoUnidad.ESTANTE)));
        verificar("Agregar código repetido es false",
                !inicial.agregar("BIB", new UnidadBiblioteca("sw", "Otra área", TipoUnidad.AREA)));
        verificar("Agregar a padre inexistente es false",
                !inicial.agregar("XYZ", new UnidadBiblioteca("AREA-X", "Área X", TipoUnidad.AREA)));
        verificar("Agregar AREA bajo BIBLIOTECA es true",
                inicial.agregar("BIB", new UnidadBiblioteca("ADM", "Administración", TipoUnidad.AREA)));

        CatalogoABB estante = DatosPrueba.crearBibliotecaInicial().buscar("SW-E1").getCatalogo();
        verificar("Preorden SW-E1", codigos(estante.recorrer(TipoRecorrido.PREORDEN)).equals("60 35 20 45 40 80 70 75 90"));
        verificar("Inorden SW-E1", codigos(estante.recorrer(TipoRecorrido.INORDEN)).equals("20 35 40 45 60 70 75 80 90"));
        verificar("Posorden SW-E1", codigos(estante.recorrer(TipoRecorrido.POSORDEN)).equals("20 40 45 35 75 70 90 80 60"));
        verificar("Traza preorden termina con la secuencia",
                estante.trazaRecorrido(TipoRecorrido.PREORDEN).endsWith("Secuencia PREORDEN: 60 35 20 45 40 80 70 75 90"));

        verificar("Insertar código repetido es false", !estante.insertar(DatosPrueba.crearLibro(60)));
        verificar("caminoBusqueda(42) = [60, 35, 45, 40]", estante.caminoBusqueda(42).equals(Arrays.asList(60, 35, 45, 40)));
        verificar("contarEnRango(40, 75) = 5", estante.contarEnRango(40, 75) == 5);
        verificar("ABB inicial es válido", estante.esABBValido());

        estante.insertar(DatosPrueba.crearLibro(42));
        verificar("Eliminar 20 (hoja)", estante.eliminar(20));
        verificar("Eliminar 70 (un hijo)", estante.eliminar(70));
        verificar("Eliminar 60 (dos hijos)", estante.eliminar(60));
        verificar("Preorden final 75 35 45 40 42 80 90",
                codigos(estante.recorrer(TipoRecorrido.PREORDEN)).equals("75 35 45 40 42 80 90"));
        verificar("ABB sigue válido tras eliminar", estante.esABBValido());
        verificar("Contar tras eliminar es 7", estante.contar() == 7);

        verificar("Buscar inexistente es null", estante.buscar(999) == null);
        verificar("Modificar inexistente es false", !estante.modificar(999, "Titulo", "Autor", 2000));
        verificar("Eliminar inexistente es false", !estante.eliminar(999));
        verificar("Modificar existente es true", estante.modificar(75, "Nuevo título", "Nuevo autor", 2020));
        verificar("Modificación aplicada", estante.buscar(75).getTitulo().equals("Nuevo título"));

        verificar("Libro con código 0 lanza excepción", lanzaExcepcion(0, "T", "A", 2000));
        verificar("Libro con año 1899 lanza excepción", lanzaExcepcion(5, "T", "A", 1899));
        verificar("Libro con año 2027 lanza excepción", lanzaExcepcion(5, "T", "A", 2027));
        verificar("Libro con título vacío lanza excepción", lanzaExcepcion(5, " ", "A", 2000));
        verificar("Libro con autor nulo lanza excepción", lanzaExcepcion(5, "T", null, 2000));

        CatalogoABB invalido = new CatalogoABB();
        NodoLibro raiz = new NodoLibro(DatosPrueba.crearLibro(50));
        NodoLibro hijo = new NodoLibro(DatosPrueba.crearLibro(30));
        NodoLibro nieto = new NodoLibro(DatosPrueba.crearLibro(70));
        hijo.setDerecho(nieto);
        raiz.setIzquierdo(hijo);
        invalido.setRaiz(raiz);
        verificar("esABBValido con nieto mal ubicado es false", !invalido.esABBValido());

        CatalogoABB reconstruido = CatalogoABB.desdePreorden(new int[]{60, 35, 20, 45, 40, 80, 70, 75, 90});
        verificar("desdePreorden reproduce el preorden",
                codigos(reconstruido.recorrer(TipoRecorrido.PREORDEN)).equals("60 35 20 45 40 80 70 75 90"));
        verificar("desdePreorden reproduce el inorden",
                codigos(reconstruido.recorrer(TipoRecorrido.INORDEN)).equals("20 35 40 45 60 70 75 80 90"));

        System.out.println();
        System.out.println(correctas + "/" + total + " pruebas OK");
    }

    private static void verificar(String nombre, boolean condicion) {
        total++;
        if (condicion) {
            correctas++;
        }
        System.out.println((condicion ? "OK     " : "FALLÓ  ") + nombre);
    }

    private static boolean lanzaExcepcion(int codigo, String titulo, String autor, int anio) {
        try {
            new Libro(codigo, titulo, autor, anio);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static String codigos(List<Libro> libros) {
        StringBuilder sb = new StringBuilder();
        for (Libro libro : libros) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(libro.getCodigo());
        }
        return sb.toString();
    }
}
