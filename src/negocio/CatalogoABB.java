package negocio;

import enums.TipoRecorrido;
import java.util.ArrayList;
import java.util.List;
import modelo.Libro;

public class CatalogoABB {
    private NodoLibro raiz;

    public NodoLibro getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoLibro raiz) {
        this.raiz = raiz;
    }

    public boolean insertar(Libro libro) {
        if (libro == null || buscar(libro.getCodigo()) != null) {
            return false;
        }
        raiz = insertar(raiz, libro);
        return true;
    }

    private NodoLibro insertar(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);
        }
        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), libro));
        } else {
            nodo.setDerecho(insertar(nodo.getDerecho(), libro));
        }
        return nodo;
    }

    public Libro buscar(int codigo) {
        return buscar(raiz, codigo);
    }

    private Libro buscar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo == actual) {
            return nodo.getDato();
        }
        return codigo < actual ? buscar(nodo.getIzquierdo(), codigo) : buscar(nodo.getDerecho(), codigo);
    }

    public int contar() {
        return contar(raiz);
    }

    private int contar(NodoLibro nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contar(nodo.getIzquierdo()) + contar(nodo.getDerecho());
    }

    public List<Libro> recorrer(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        List<Libro> resultado = new ArrayList<>();
        recorrer(raiz, tipo, resultado);
        return resultado;
    }

    private void recorrer(NodoLibro nodo, TipoRecorrido tipo, List<Libro> resultado) {
        if (nodo == null) {
            return;
        }
        if (tipo == TipoRecorrido.PREORDEN) {
            resultado.add(nodo.getDato());
        }
        recorrer(nodo.getIzquierdo(), tipo, resultado);
        if (tipo == TipoRecorrido.INORDEN) {
            resultado.add(nodo.getDato());
        }
        recorrer(nodo.getDerecho(), tipo, resultado);
        if (tipo == TipoRecorrido.POSORDEN) {
            resultado.add(nodo.getDato());
        }
    }

    public String trazaRecorrido(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de recorrido es obligatorio.");
        }
        StringBuilder traza = new StringBuilder();
        StringBuilder secuencia = new StringBuilder();
        traza(raiz, 0, tipo, traza, secuencia);
        traza.append("Secuencia ").append(tipo.name()).append(":").append(secuencia);
        return traza.toString();
    }

    private void traza(NodoLibro nodo, int nivel, TipoRecorrido tipo, StringBuilder traza, StringBuilder secuencia) {
        String sangria = "  ".repeat(nivel);
        if (nodo == null) {
            traza.append(sangria).append("null -> regresa\n");
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        traza.append(sangria).append("entra ").append(codigo).append("\n");
        if (tipo == TipoRecorrido.PREORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
        traza(nodo.getIzquierdo(), nivel + 1, tipo, traza, secuencia);
        if (tipo == TipoRecorrido.INORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
        traza(nodo.getDerecho(), nivel + 1, tipo, traza, secuencia);
        if (tipo == TipoRecorrido.POSORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
    }

    private void visitar(int codigo, String sangria, StringBuilder traza, StringBuilder secuencia) {
        traza.append(sangria).append("VISITA ").append(codigo).append("\n");
        secuencia.append(" ").append(codigo);
    }

    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        Libro libro = buscar(codigo);
        if (libro == null) {
            return false;
        }
        // valida todos los datos antes de cambiar el libro
        new Libro(codigo, titulo, autor, anio);
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setAnio(anio);
        return true;
    }

    public boolean eliminar(int codigo) {
        if (buscar(codigo) == null) {
            return false;
        }
        raiz = eliminar(raiz, codigo);
        return true;
    }

    private NodoLibro eliminar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo < actual) {
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), codigo));
        } else if (codigo > actual) {
            nodo.setDerecho(eliminar(nodo.getDerecho(), codigo));
        } else {
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }
            NodoLibro sucesor = minimo(nodo.getDerecho());
            nodo.setDato(sucesor.getDato());
            nodo.setDerecho(eliminar(nodo.getDerecho(), sucesor.getDato().getCodigo()));
        }
        return nodo;
    }

    private NodoLibro minimo(NodoLibro nodo) {
        return nodo.getIzquierdo() == null ? nodo : minimo(nodo.getIzquierdo());
    }

    public int contarEnRango(int a, int b) {
        return contarEnRango(raiz, a, b);
    }

    private int contarEnRango(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo < a) {
            return contarEnRango(nodo.getDerecho(), a, b);
        }
        if (codigo > b) {
            return contarEnRango(nodo.getIzquierdo(), a, b);
        }
        return 1 + contarEnRango(nodo.getIzquierdo(), a, b) + contarEnRango(nodo.getDerecho(), a, b);
    }

    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        camino(raiz, codigo, camino);
        return camino;
    }

    private void camino(NodoLibro nodo, int codigo, List<Integer> camino) {
        if (nodo == null) {
            return;
        }
        int actual = nodo.getDato().getCodigo();
        camino.add(actual);
        if (codigo < actual) {
            camino(nodo.getIzquierdo(), codigo, camino);
        } else if (codigo > actual) {
            camino(nodo.getDerecho(), codigo, camino);
        }
    }

    public boolean esABBValido() {
        return esValido(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean esValido(NodoLibro nodo, long minimo, long maximo) {
        if (nodo == null) {
            return true;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo <= minimo || codigo >= maximo) {
            return false;
        }
        return esValido(nodo.getIzquierdo(), minimo, codigo) && esValido(nodo.getDerecho(), codigo, maximo);
    }

    public static CatalogoABB desdePreorden(int[] preorden) {
        CatalogoABB catalogo = new CatalogoABB();
        if (preorden == null || preorden.length == 0) {
            return catalogo;
        }
        int[] indice = {0};
        catalogo.raiz = construir(preorden, indice, Long.MIN_VALUE, Long.MAX_VALUE);
        if (indice[0] != preorden.length) {
            throw new IllegalArgumentException("El arreglo no es el preorden de un ABB válido.");
        }
        return catalogo;
    }

    private static NodoLibro construir(int[] preorden, int[] indice, long minimo, long maximo) {
        if (indice[0] >= preorden.length) {
            return null;
        }
        int codigo = preorden[indice[0]];
        if (codigo <= minimo || codigo >= maximo) {
            return null;
        }
        indice[0]++;
        NodoLibro nodo = new NodoLibro(new Libro(codigo, "Libro " + codigo, "Autor desconocido", 2000));
        nodo.setIzquierdo(construir(preorden, indice, minimo, codigo));
        nodo.setDerecho(construir(preorden, indice, codigo, maximo));
        return nodo;
    }
}