package modelo;

public class Libro {
    private final int codigo;
    private String titulo;
    private String autor;
    private int anio;

    public Libro(int codigo, String titulo, String autor, int anio) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código debe ser mayor que 0.");
        }
        this.codigo = codigo;
        setTitulo(titulo);
        setAutor(autor);
        setAnio(anio);
    }

    public int getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        this.titulo = titulo.trim();
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        this.autor = autor.trim();
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        if (anio < 1900 || anio > 2026) {
            throw new IllegalArgumentException("El año debe estar entre 1900 y 2026.");
        }
        this.anio = anio;
    }

    @Override
    public String toString() {
        return "[" + codigo + "] " + titulo + " - " + autor + " (" + anio + ")";
    }
}
