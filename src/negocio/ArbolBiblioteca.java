package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

public class ArbolBiblioteca {
    private final NodoUnidad raiz;

    public ArbolBiblioteca(UnidadBiblioteca raiz) {
        if (raiz == null || raiz.getTipo() != TipoUnidad.BIBLIOTECA) {
            throw new IllegalArgumentException("La raíz debe ser una unidad de tipo BIBLIOTECA.");
        }
        this.raiz = new NodoUnidad(raiz);
    }

    public NodoUnidad getRaiz() {
        return raiz;
    }

    public boolean agregar(String codigoPadre, UnidadBiblioteca nueva) {
        if (codigoPadre == null || nueva == null) {
            return false;
        }
        NodoUnidad padre = buscar(codigoPadre);
        if (padre == null || buscar(nueva.getCodigo()) != null) {
            return false;
        }
        if (!padre.getDato().getTipo().puedeTenerHijo(nueva.getTipo())) {
            return false;
        }
        padre.getHijos().add(new NodoUnidad(nueva));
        return true;
    }

    public NodoUnidad buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        return buscar(raiz, codigo.trim());
    }

    private NodoUnidad buscar(NodoUnidad nodo, String codigo) {
        if (nodo.getDato().getCodigo().equalsIgnoreCase(codigo)) {
            return nodo;
        }
        for (NodoUnidad hijo : nodo.getHijos()) {
            NodoUnidad encontrado = buscar(hijo, codigo);
            if (encontrado != null) {
                return encontrado;
            }
        }
        return null;
    }

    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoUnidad nodo) {
        int max = 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            max = Math.max(max, 1 + altura(hijo));
        }
        return max;
    }

    public int contarHojas() {
        return contarHojas(raiz);
    }

    private int contarHojas(NodoUnidad nodo) {
        if (nodo.getHijos().isEmpty()) {
            return 1;
        }
        int total = 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarHojas(hijo);
        }
        return total;
    }

    public int contarUnidades() {
        return contarUnidades(raiz);
    }

    private int contarUnidades(NodoUnidad nodo) {
        int total = 1;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarUnidades(hijo);
        }
        return total;
    }

    public int nivel(String codigo) {
        if (codigo == null) {
            return -1;
        }
        return nivel(raiz, codigo.trim(), 0);
    }

    private int nivel(NodoUnidad nodo, String codigo, int nivelActual) {
        if (nodo.getDato().getCodigo().equalsIgnoreCase(codigo)) {
            return nivelActual;
        }
        for (NodoUnidad hijo : nodo.getHijos()) {
            int resultado = nivel(hijo, codigo, nivelActual + 1);
            if (resultado != -1) {
                return resultado;
            }
        }
        return -1;
    }

    public String mostrarJerarquia() {
        StringBuilder sb = new StringBuilder();
        mostrarJerarquia(raiz, 0, sb);
        return sb.toString();
    }

    private void mostrarJerarquia(NodoUnidad nodo, int nivelActual, StringBuilder sb) {
        sb.append("  ".repeat(nivelActual)).append(nodo.getDato()).append("\n");
        for (NodoUnidad hijo : nodo.getHijos()) {
            mostrarJerarquia(hijo, nivelActual + 1, sb);
        }
    }

    public int contarLibrosTotal() {
        return contarLibrosTotal(raiz);
    }

    private int contarLibrosTotal(NodoUnidad nodo) {
        int total = nodo.getCatalogo() != null ? nodo.getCatalogo().contar() : 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarLibrosTotal(hijo);
        }
        return total;
    }
}