# Catálogo de la Biblioteca FISEI

Aplicación de consola en Java para representar la organización de una biblioteca mediante un árbol general y gestionar los libros de cada estante con un árbol binario de búsqueda (ABB).

## Autor

Jampier Camacho.

Asignatura: Estructura de Datos.  
Nivel: Tercero de Ingeniería de Software.  
Universidad Técnica de Ambato.

## Descripción del problema

La biblioteca se organiza en áreas y cada área contiene estantes. Esta jerarquía se representa mediante un árbol general.

Cada estante dispone de un catálogo ABB independiente, ordenado por el código del libro. Los códigos son enteros positivos y no se admiten duplicados dentro del mismo catálogo.

Cada libro almacena código, título, autor y año de publicación. El código no se modifica y el año debe estar entre 1900 y 2026.

## Estructura inicial

| Unidad                             | Padre   | Libros insertados en orden         |
| ---------------------------------- | ------- | ---------------------------------- |
| BIB — Biblioteca FISEI             | Ninguno | No aplica                          |
| SW — Software                      | BIB     | No aplica                          |
| SW-E1 — Estante 1 de Software      | SW      | 60, 35, 80, 20, 45, 70, 90, 40, 75 |
| SW-E2 — Estante 2 de Software      | SW      | 15, 10, 25                         |
| TI — Tecnologías de la Información | BIB     | No aplica                          |
| TI-E1 — Estante 1 de TI            | TI      | 50                                 |
| TEL — Telecomunicaciones           | BIB     | No aplica                          |

Resultados iniciales:

- 7 unidades.
- 4 hojas: SW-E1, SW-E2, TI-E1 y TEL.
- Altura de 2 aristas.
- 13 libros entre todos los estantes.

TEL es una hoja porque no tiene hijos, aunque sea un área.

## Organización del código

| Paquete     | Archivos                                                                 |
| ----------- | ------------------------------------------------------------------------ |
| src/enums   | TipoUnidad.java y TipoRecorrido.java                                     |
| src/modelo  | Libro.java y UnidadBiblioteca.java                                       |
| src/negocio | NodoUnidad.java, NodoLibro.java, ArbolBiblioteca.java y CatalogoABB.java |
| src/app     | Main.java, DatosPrueba.java y PruebasBiblioteca.java                     |

Otros archivos:

- TRAZAS.md: construcción del ABB personal, recorridos, traza y eliminaciones.
- .gitignore: excluye out/ y los archivos .class.
- Ejecución del código/: capturas de las ejecuciones.

## Requisitos

- JDK 17 o posterior.
- Visual Studio Code o un editor compatible con Java.
- PowerShell para utilizar los comandos siguientes.

## Compilación

Abrir una terminal PowerShell en la raíz del repositorio y ejecutar:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

## Ejecutar el programa

```powershell
java -cp out app.Main
```

El programa comienza únicamente con la raíz BIB. Seleccionar la opción 1 para cargar las áreas, los estantes y los libros iniciales.

Volver a seleccionar la opción 1 restablece los datos iniciales y descarta los cambios realizados durante esa ejecución.

## Ejecutar las pruebas

```powershell
java -cp out app.PruebasBiblioteca
```

Resultado obtenido:

```text
41/41 pruebas OK
```

### Captura de las pruebas

![Resultado de PruebasBiblioteca](<Ejecución del código/01_Pruebas_Biblioteca_41_OK.png>)

## Operaciones disponibles

### Árbol general

- Agregar unidades respetando BIBLIOTECA → AREA → ESTANTE.
- Rechazar códigos de unidad repetidos.
- Buscar unidades sin distinguir mayúsculas y minúsculas.
- Mostrar la jerarquía con dos espacios por nivel.
- Calcular altura en aristas.
- Contar las unidades sin hijos.
- Consultar el nivel de una unidad.
- Contar los libros de todos los estantes.

### Catálogo ABB

- Insertar libros sin códigos repetidos.
- Buscar libros por código.
- Contar libros.
- Obtener preorden, inorden y posorden.
- Mostrar la traza recursiva de cada recorrido.
- Modificar título, autor y año sin cambiar el código.
- Eliminar nodos hoja, con un hijo y con dos hijos.
- Utilizar el sucesor inorden al eliminar un nodo con dos hijos.

### Retos

- contarEnRango(a, b): cuenta los códigos incluidos en el rango y poda los subárboles que no pueden aportar resultados.
- caminoBusqueda(codigo): devuelve los códigos visitados, incluso si el libro buscado no existe.
- esABBValido(): verifica el orden de todos los nodos mediante límites mínimo y máximo.
- desdePreorden(preorden): reconstruye el ABB recursivamente sin insertar los códigos uno por uno. Rechaza secuencias que no pueden consumirse completamente como un preorden válido.

La reconstrucción recibe únicamente códigos, por lo que utiliza títulos y autores genéricos para los libros creados.

## Menú

| Opción | Función                                          |
| ------ | ------------------------------------------------ |
| 1      | Cargar estructura inicial                        |
| 2      | Mostrar jerarquía                                |
| 3      | Agregar unidad                                   |
| 4      | Buscar unidad                                    |
| 5      | Consultar altura, hojas, nivel y total de libros |
| 6      | Insertar libro                                   |
| 7      | Buscar libro                                     |
| 8      | Modificar libro                                  |
| 9      | Eliminar libro                                   |
| 10     | Mostrar recorrido y traza                        |
| 11     | Ejecutar retos                                   |
| 0      | Salir                                            |

## Resultados verificados

### Recorridos iniciales de SW-E1

| Recorrido | Resultado                  |
| --------- | -------------------------- |
| Preorden  | 60 35 20 45 40 80 70 75 90 |
| Inorden   | 20 35 40 45 60 70 75 80 90 |
| Posorden  | 20 40 45 35 75 70 90 80 60 |

### Consultas y validaciones

| Caso                                             | Resultado                |
| ------------------------------------------------ | ------------------------ |
| Biblioteca solo con raíz                         | Altura 0 y una hoja      |
| Nivel de BIB                                     | 0                        |
| Nivel de sw                                      | 1                        |
| Nivel de SW-E2                                   | 2                        |
| Nivel de XYZ                                     | -1                       |
| Agregar ESTANTE directamente bajo BIBLIOTECA     | false                    |
| Insertar un código repetido                      | false                    |
| caminoBusqueda(42), antes de insertarlo          | [60, 35, 45, 40]         |
| contarEnRango(40, 75), sobre el catálogo inicial | 5                        |
| Buscar un código inexistente                     | null                     |
| Modificar o eliminar un código inexistente       | false                    |
| Crear un libro con datos inválidos               | IllegalArgumentException |
| Validar un ABB con un nieto mal ubicado          | false                    |

Al insertar 42 en SW-E1 y eliminar 20, 70 y 60, en ese orden, el preorden final es:

```text
75 35 45 40 42 80 90
```

## Ejercicio personal

Los códigos personales son 91, 11 y 13. Se insertan después de los nueve códigos iniciales de SW-E1.

Preorden obtenido:

```text
60 35 20 11 13 45 40 80 70 75 90 91
```

Después de eliminar 91, 11 y 60, en ese orden:

```text
70 35 20 13 45 40 80 75 90
```

El desarrollo y los diagramas se encuentran en [TRAZAS.md](TRAZAS.md).

## Restricciones

Las operaciones sobre los árboles se resuelven mediante recursividad. En el árbol general se utiliza un ciclo para recorrer la lista de hijos y se aplica recursión sobre cada subárbol.

No se utilizan TreeSet, TreeMap, PriorityQueue, NavigableSet, NavigableMap, Collections.sort ni List.sort.

Los recorridos se obtienen visitando los nodos en el orden correspondiente, sin ordenar listas.

## Desarrollo con Git

El trabajo se realiza en la rama desarrollo mediante commits que describen cada avance. La entrega requiere integrar esa rama en main mediante un pull request.

Los archivos compilados se excluyen del repositorio mediante .gitignore.
