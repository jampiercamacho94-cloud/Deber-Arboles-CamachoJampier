package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static ArbolBiblioteca biblioteca = DatosPrueba.crearBibliotecaVacia();

    public static void main(String[] args) {
        int opcion = -1;
        try {
            do {
                mostrarMenu();
                opcion = leerEntero("Opción: ");
                ejecutar(opcion);
            } while (opcion != 0);
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada finalizada.");
        }
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== CATÁLOGO BIBLIOTECA FISEI =====");
        System.out.println(" 1. Cargar estructura inicial");
        System.out.println(" 2. Mostrar jerarquía");
        System.out.println(" 3. Agregar unidad");
        System.out.println(" 4. Buscar unidad");
        System.out.println(" 5. Consultas: altura, hojas, nivel y total de libros");
        System.out.println(" 6. Insertar libro en un estante");
        System.out.println(" 7. Buscar libro en un estante");
        System.out.println(" 8. Modificar libro de un estante");
        System.out.println(" 9. Eliminar libro de un estante");
        System.out.println("10. Mostrar recorrido con traza");
        System.out.println("11. Ejecutar retos");
        System.out.println(" 0. Salir");
    }

    private static void ejecutar(int opcion) {
        switch (opcion) {
            case 1 -> cargarInicial();
            case 2 -> System.out.print(biblioteca.mostrarJerarquia());
            case 3 -> agregarUnidad();
            case 4 -> buscarUnidad();
            case 5 -> consultas();
            case 6 -> insertarLibro();
            case 7 -> buscarLibro();
            case 8 -> modificarLibro();
            case 9 -> eliminarLibro();
            case 10 -> mostrarRecorrido();
            case 11 -> ejecutarRetos();
            case 0 -> System.out.println("Hasta pronto.");
            default -> System.out.println("Opción no válida.");
        }
    }

    private static void cargarInicial() {
        biblioteca = DatosPrueba.crearBibliotecaInicial();
        System.out.println("Estructura inicial cargada: "
                + biblioteca.contarUnidades() + " unidades y "
                + biblioteca.contarLibrosTotal() + " libros.");
    }

    private static void agregarUnidad() {
        String padre = leerTexto("Código de la unidad padre: ");
        String codigo = leerTexto("Código de la nueva unidad: ");
        String nombre = leerTexto("Nombre: ");
        int tipo = leerEntero("Tipo (1 = BIBLIOTECA, 2 = AREA, 3 = ESTANTE): ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo no válido.");
            return;
        }
        try {
            UnidadBiblioteca nueva = new UnidadBiblioteca(codigo, nombre, TipoUnidad.values()[tipo - 1]);
            if (biblioteca.agregar(padre, nueva)) {
                System.out.println("Unidad agregada: " + nueva);
            } else {
                System.out.println("No se pudo agregar: padre inexistente, código repetido o jerarquía no permitida.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());
        }
    }

    private static void buscarUnidad() {
        NodoUnidad nodo = biblioteca.buscar(leerTexto("Código de la unidad: "));
        if (nodo == null) {
            System.out.println("La unidad no existe.");
            return;
        }
        System.out.println("Unidad: " + nodo.getDato());
        System.out.println("Tipo: " + nodo.getDato().getTipo());
        System.out.println("Hijos directos: " + nodo.getHijos().size());
        if (nodo.getCatalogo() != null) {
            System.out.println("Libros en el estante: " + nodo.getCatalogo().contar());
        }
    }

    private static void consultas() {
        System.out.println("Altura del árbol: " + biblioteca.altura());
        System.out.println("Número de hojas: " + biblioteca.contarHojas());
        System.out.println("Total de libros: " + biblioteca.contarLibrosTotal());
        String codigo = leerTexto("Código para consultar su nivel (Enter para omitir): ");
        if (!codigo.isEmpty()) {
            System.out.println("Nivel de " + codigo.toUpperCase() + ": " + biblioteca.nivel(codigo));
        }
    }

    private static void insertarLibro() {
        CatalogoABB catalogo = pedirEstante();
        if (catalogo == null) {
            return;
        }
        int codigo = leerEntero("Código del libro: ");
        String titulo = leerTexto("Título: ");
        String autor = leerTexto("Autor: ");
        int anio = leerEntero("Año: ");
        try {
            Libro libro = new Libro(codigo, titulo, autor, anio);
            System.out.println(catalogo.insertar(libro) ? "Libro insertado." : "El código ya existe.");
        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());
        }
    }

    private static void buscarLibro() {
        CatalogoABB catalogo = pedirEstante();
        if (catalogo == null) {
            return;
        }
        Libro libro = catalogo.buscar(leerEntero("Código del libro: "));
        System.out.println(libro == null ? "El libro no existe." : libro.toString());
    }

    private static void modificarLibro() {
        CatalogoABB catalogo = pedirEstante();
        if (catalogo == null) {
            return;
        }
        int codigo = leerEntero("Código del libro a modificar: ");
        if (catalogo.buscar(codigo) == null) {
            System.out.println("El libro no existe.");
            return;
        }
        String titulo = leerTexto("Nuevo título: ");
        String autor = leerTexto("Nuevo autor: ");
        int anio = leerEntero("Nuevo año: ");
        try {
            System.out.println(catalogo.modificar(codigo, titulo, autor, anio) ? "Libro modificado." : "El libro no existe.");
        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());
        }
    }

    private static void eliminarLibro() {
        CatalogoABB catalogo = pedirEstante();
        if (catalogo == null) {
            return;
        }
        System.out.println(catalogo.eliminar(leerEntero("Código del libro a eliminar: "))
                ? "Libro eliminado." : "El libro no existe.");
    }

    private static void mostrarRecorrido() {
        CatalogoABB catalogo = pedirEstante();
        if (catalogo == null) {
            return;
        }
        int tipo = leerEntero("Recorrido (1 = PREORDEN, 2 = INORDEN, 3 = POSORDEN): ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo no válido.");
            return;
        }
        TipoRecorrido recorrido = TipoRecorrido.values()[tipo - 1];
        System.out.println(catalogo.trazaRecorrido(recorrido));
        imprimirLibros(catalogo.recorrer(recorrido));
    }

    private static void ejecutarRetos() {
        CatalogoABB catalogo = pedirEstante();
        if (catalogo == null) {
            return;
        }
        int a = leerEntero("Límite inferior del rango: ");
        int b = leerEntero("Límite superior del rango: ");
        System.out.println("Libros con código entre " + a + " y " + b + ": " + catalogo.contarEnRango(a, b));
        int codigo = leerEntero("Código para el camino de búsqueda: ");
        System.out.println("Camino hacia " + codigo + ": " + catalogo.caminoBusqueda(codigo));
        System.out.println("¿Es un ABB válido? " + (catalogo.esABBValido() ? "Sí" : "No"));
        String texto = leerTexto("Preorden para reconstruir un ABB (códigos separados por espacios, Enter para omitir): ");
        if (!texto.isEmpty()) {
            reconstruir(texto);
        }
    }

    private static void reconstruir(String texto) {
        String[] partes = texto.split("\\s+");
        int[] preorden = new int[partes.length];
        try {
            for (int i = 0; i < partes.length; i++) {
                preorden[i] = Integer.parseInt(partes[i]);
            }
            CatalogoABB reconstruido = CatalogoABB.desdePreorden(preorden);
            System.out.println(reconstruido.trazaRecorrido(TipoRecorrido.INORDEN));
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo reconstruir: " + e.getMessage());
        }
    }

    private static CatalogoABB pedirEstante() {
        NodoUnidad nodo = biblioteca.buscar(leerTexto("Código del estante: "));
        if (nodo == null) {
            System.out.println("La unidad no existe.");
            return null;
        }
        if (nodo.getCatalogo() == null) {
            System.out.println("La unidad no es un estante.");
            return null;
        }
        return nodo.getCatalogo();
    }

    private static void imprimirLibros(List<Libro> libros) {
        for (Libro libro : libros) {
            System.out.println("  " + libro);
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }
}
