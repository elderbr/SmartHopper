package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.controllers.GrupoController;
import mc.elderbr.smarthopper.controllers.ItemController;
import mc.elderbr.smarthopper.exceptions.GrupoException;
import mc.elderbr.smarthopper.exceptions.ItemException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.model.Traducao;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class TraducaoConfig {
    private final File directoryFile = new File(VGlobal.FILE_LANG.getAbsolutePath());

    private String name;
    private Traducao traducao;

    private BufferedWriter escrever;
    private BufferedReader reader;
    private InputStream input;
    private String txtReader;

    private static YamlConfiguration yml;

    private File fileBR = new File(directoryFile, "pt_br.yml");
    private File fileGrupoBR = new File(directoryFile, "grupo.yml");
    private File filePT = new File(directoryFile, "pt_pt.yml");

    private ItemController itemCtrl = new ItemController();
    private GrupoController grupoCtrl = new GrupoController();

    public TraducaoConfig() {
        // Criando o pasta lang
        if (!directoryFile.exists()) {
            directoryFile.mkdir();

            // Se o arquivo pt_br não existir cria
            if (!fileBR.exists()) {
                createBR();
            }
            // Se o arquivo pt_pt não existir cria
            if (!filePT.exists()) {
                createTP();
            }
        }
        reload();// Lendo todas as traduções
    }

    private void createBR() {

        try {

            escrever = Files.newBufferedWriter(fileBR.toPath(), StandardCharsets.UTF_8);

            input = getClass().getResourceAsStream("/pt_br.yml");
            reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

            escrever.write("# Tradução dos item em português Brasil");
            escrever.newLine();
            escrever.flush();
            while ((txtReader = reader.readLine()) != null) {
                escrever.write(txtReader);
                escrever.newLine();
                escrever.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (escrever != null)
                    escrever.close();
                if (reader != null)
                    reader.close();
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
    }

    private void createTP() {
        try {

            escrever = Files.newBufferedWriter(filePT.toPath(), StandardCharsets.UTF_8);

            input = getClass().getResourceAsStream("/pt_pt.yml");
            reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

            escrever.write("# Tradução dos item em português Portugal");
            escrever.newLine();
            escrever.flush();
            while ((txtReader = reader.readLine()) != null) {
                escrever.write(txtReader);
                escrever.newLine();
                escrever.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (escrever != null)
                    escrever.close();
                if (reader != null)
                    reader.close();
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
    }

    public void reload() {
        String lang = null;
        for (File file : directoryFile.listFiles()) {
            // Lendo o arquivo de tradução
            yml = YamlConfiguration.loadConfiguration(file);
            // Nome do linguagem
            lang = yml.getString("lang");

            if (lang == null || lang.isBlank()) {
                Msg.ServidorRed(String.format("No arquivo %s não está definido o lang!!!", file.getName()));
                continue;
            }

            for (Map.Entry<String, Object> map : yml.getValues(false).entrySet()) {
                // Procurando e salvando a tradução para o item
                try {
                    Item item = itemCtrl.findByName(map.getKey());
                    if (item == null) continue;
                    item.addTranslation(lang, map.getValue().toString());
                    itemCtrl.update(item);
                    Msg.ServidorGreen("$2Adicionando tradução para o item $6" + item.getName());
                } catch (ItemException e) {
                    continue;
                }

                // Procurando e salvando a tradução para o grupo
                try {
                    Grupo grupo = grupoCtrl.findByName(map.getKey());
                    if (grupo == null) continue;
                    grupo.addTranslation(lang, map.getValue().toString());
                    grupoCtrl.update(grupo);
                    Msg.ServidorBlue("$9Adicionando tradução para o grupo $6" + grupo.getName());
                } catch (GrupoException e) {

                }
            }
            file.delete();
        }
    }

}