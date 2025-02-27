package es.joseluisgs.dam.controllers;

import es.joseluisgs.dam.exceptions.PaisException;
import es.joseluisgs.dam.models.Pais;
import es.joseluisgs.dam.repositories.IPaisRepository;
import es.joseluisgs.dam.services.IStoragePaises;

import java.util.List;

/**
 * Controlador de Gestion de Paises
 * Dependencias: PaisRepository
 * Es importante que lanzo las excepciones de PaisException
 * Que ya las recogerá la vista
 */
public class PaisController {
    // private static PaisController instance;
    private final IPaisRepository paisRepository;
    private final IStoragePaises paisStorage;

    // Inyección de dependencias por controlador
    public PaisController(IPaisRepository paisRepository, IStoragePaises paisStorage) {
        this.paisRepository = paisRepository;
        this.paisStorage = paisStorage;
        loadData();
    }


    /**
     * Inicializa el repositorio con datos de prueba
     */
    private void loadData() {

        this.paisRepository.save(new Pais("España", "ES", "Castellano", "Euro", "Madrid"));
        this.paisRepository.save(new Pais("Francia", "FR", "Francés", "Euro", "Paris"));
        this.paisRepository.save(new Pais("Italia", "IT", "Italiano", "Euro", "Roma"));
        this.paisRepository.save(new Pais("Alemania", "DE", "Alemán", "Euro", "Berlín"));
        this.paisRepository.save(new Pais("Reino Unido", "UK", "Inglés", "Libra", "Londres"));
        this.paisRepository.save(new Pais("Japón", "JP", "Japonés", "Yen", "Tokio"));
        this.paisRepository.save(new Pais("China", "CN", "Chino", "Yuan", "Pekín"));
        this.paisRepository.save(new Pais("Australia", "AU", "Inglés", "Dólar", "Canberra"));
        this.paisRepository.save(new Pais("Argentina", "AR", "Español", "Peso", "Buenos Aires"));
        this.paisRepository.save(new Pais("Brasil", "BR", "Portugués", "Real", "Brasilia"));
        this.paisRepository.save(new Pais("Estados Unidos", "US", "Inglés", "Dólar", "Washington"));

    }

    public Pais crearPais(Pais pais) throws PaisException {
        // Comprobamos que no haya datos incorrectos, por si ha fallado la interfaz
        checkPaisData(pais);
        // comprobamos si existe
        var existe = paisRepository.findByNombre(pais.getNombre());
        if (existe == null) {
            paisRepository.save(pais);
            return pais;

        }
        throw new PaisException("Ya existe un pais con el nombre " + pais.getNombre());
    }

    /**
     * Comprueba que los datos del pais son correctos. No lo hacemos en el modelo para no contaminarlo.
     *
     * @param pais Pais a comprobar
     * @throws PaisException Si los datos son incorrectos
     */
    private void checkPaisData(Pais pais) throws PaisException {
        if (pais.getNombre() == null || pais.getNombre().trim().isEmpty()) {
            throw new PaisException("El nombre del pais no puede estar vacio");
        }
        if (pais.getCodigo() == null || pais.getCodigo().trim().isEmpty()) {
            throw new PaisException("El codigo del pais no puede estar vacio");
        }
        if (pais.getIdioma() == null || pais.getIdioma().trim().isEmpty()) {
            throw new PaisException("El idioma del pais no puede estar vacio");
        }
        if (pais.getMoneda() == null || pais.getMoneda().trim().isEmpty()) {
            throw new PaisException("La moneda del pais no puede estar vacia");
        }
        if (pais.getCapital() == null || pais.getCapital().trim().isEmpty()) {
            throw new PaisException("La capital del pais no puede estar vacia");
        }
    }

    /**
     * Devuelve el pais con el nombre indicado
     *
     * @param nombre nombre del pais
     * @return Pais con el nombre indicado
     * @throws PaisException si no existe el pais
     */
    public Pais getPaisByNombre(String nombre) throws PaisException {
        var pais = paisRepository.findByNombre(nombre);
        if (pais != null) {
            return pais;
        }
        throw new PaisException("No existe el pais con nombre " + nombre);
    }

    /**
     * Devuelve el pais con el id indicado
     *
     * @param id id del pais
     * @return Pais con el id indicado
     * @throws PaisException si no existe el pais
     */
    public Pais getPaisById(int id) throws PaisException {
        var pais = paisRepository.findById(id);
        if (pais != null) {
            return pais;
        }
        throw new PaisException("No existe el pais con id " + id);
    }

    /**
     * Devuelve todos los paises
     *
     * @return Lista de paises
     */
    public List<Pais> getAllPaises() {
        return paisRepository.findAll();
    }

    public Pais updatePais(int id, Pais pais) throws PaisException {
        // Comprobamos los datos
        checkPaisData(pais);
        // Vemos si con los datos nuevos existe un pais que se llame igual
        var otro = paisRepository.findByNombre(pais.getNombre());
        // si no existe otro pais con el mismo nombre, actualizamos
        // o si simplemente los id son iguales, actualizamos, pues somos el mismo objeto, por eso hay "otro" ya
        // si no lo entiendes dale la vuelta al if
        if ((otro == null) || (otro.getId() == pais.getId())) {
            // si no existe otro pais con el mismo nombre, lo actualizamos o somos nosotros por id
            paisRepository.update(id, pais);
            return pais;
        } else {
            throw new PaisException("Ya existe un pais con el nombre " + pais.getNombre());
        }
    }

    /**
     * Elimina el pais con el nombre indicado
     *
     * @param nombre nombre del pais
     * @return Pais eliminado
     * @throws PaisException si no existe el pais
     */
    public Pais deletePais(String nombre) throws PaisException {
        var pais = paisRepository.findByNombre(nombre);
        if (pais != null) {
            // paisRepository.deleteById(pais.getId());
            paisRepository.delete(pais.getId());
            return pais;
        } else {
            throw new PaisException("No existe el pais con nombre " + nombre);
        }
    }

    /**
     * Importa los datos desde un fichero de backup
     */
    public void importarDatos() {
        System.out.println("Importando datos de paises...");
        System.out.println("Importando desde: " + paisStorage.getStoragePath());
        var lista = paisStorage.load();
        if (lista.size() > 0) {
            paisRepository.clearAll();
            for (Pais pais : lista) {
                // pais
                paisRepository.save(pais);
            }
            System.out.println("Datos importados con éxito al repositorio: " + lista.size() + " paises");
        } else {
            System.out.println("Ha existido un problema al importar los datos");
        }
    }

    /**
     * Exporta los datos desde un fichero de Backup
     */
    public void exportarDatos() {
        System.out.println("Exportando datos de paises...");
        var lista = paisRepository.findAll();
        var res = paisStorage.save(lista);
        if (res) {
            System.out.println("Datos exportados con éxito en: " + paisStorage.getStoragePath() + "\nTotal paises: " + lista.size());
        } else {
            System.out.println("Ha existido un problema al exportar los datos");
        }
    }
}
