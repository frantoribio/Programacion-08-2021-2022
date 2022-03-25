package es.joseluisgs.dam.services;

import es.joseluisgs.dam.models.Pais;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StoragePaisesSerializedFile implements IStoragePaises {
    private final Path currentRelativePath = Paths.get("");
    private final String ruta = currentRelativePath.toAbsolutePath().toString();
    private final String dir = ruta + File.separator + "data";
    private final String paisesFile = dir + File.separator + "paises.dat";

    public StoragePaisesSerializedFile() {
        init();
    }

    private void init() {
        Path path = Paths.get(dir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Debe estar el modelo serializado!!!
    @Override
    public boolean save(List<Pais> lista) {
        File fichero;
        ObjectOutputStream f = null;
        boolean result = false;
        try {
            fichero = new File(paisesFile);
            f = new ObjectOutputStream(new FileOutputStream(fichero));

            f.writeObject(lista);

            result = true;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            result = false;
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    System.out.println("Error Close Write: " + e.getMessage());
                    result = false;
                }
            }
        }
        return result;
    }

    @Override
    public List<Pais> load() {
        File fichero;
        ObjectInputStream f = null;
        List<Pais> lista = new ArrayList<>();
        try {
            fichero = new File(paisesFile);
            f = new ObjectInputStream(new FileInputStream(fichero));

            lista = (List<Pais>) f.readObject();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    @Override
    public String getStoragePath() {
        return paisesFile;
    }
}
