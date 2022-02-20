package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class GrupoFile {

    private final File FILE = new File(VGlobal.ARQUIVO, "grupo.yml");
    private static YamlConfiguration YML;

    public GrupoFile() {
    }

    public void escrever() {
        Grupo grupo = new Grupo();
        try (BufferedWriter w = Files.newBufferedWriter(FILE.toPath(), StandardCharsets.UTF_8)) {
            w.write("Grupos:");
            for (Grupo gp : VGlobal.LIST_GRUPO) {
                w.newLine();
                w.write("  -"+gp.getDsGrupo());
            }
            w.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
