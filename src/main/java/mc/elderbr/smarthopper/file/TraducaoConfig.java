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
import java.util.Objects;

public class TraducaoConfig {
    private static final File directoryFile = new File(VGlobal.FILE_LANG.getAbsolutePath());

    private String name;
    private Traducao traducao;

    private BufferedWriter escrever;
    private BufferedReader reader;
    private InputStream input;
    private String txtReader;

    private static YamlConfiguration yml;

    private static final File fileBR = new File(directoryFile, "pt_br.yml");
    private static final File fileGrupoBR = new File(directoryFile, "grupo.yml");
    private static final File filePT = new File(directoryFile, "pt_pt.yml");

    private static ItemController itemCtrl = new ItemController();
    private static GrupoController grupoCtrl = new GrupoController();

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

    public static void reload() {
        String lang = null;
        String type = null;
        for (File file : directoryFile.listFiles()) {
            // Lendo o arquivo de tradução
            yml = YamlConfiguration.loadConfiguration(file);
            // Nome do linguagem
            lang = yml.getString("lang");
            type = (Objects.isNull(yml.getString("type")) || yml.getString("type").isBlank() ? "all" : yml.getString("type"));

            if (lang == null || lang.isBlank()) {
                Msg.ServidorRed(String.format("No arquivo %s não está definido o lang!!!", file.getName()));
                continue;
            }

            for (Map.Entry<String, Object> map : yml.getValues(false).entrySet()) {

                String name = map.getKey();
                String translate = map.getValue().toString();

                if (name.equals("lang") || name.equals("type")) continue;

                switch (type) {
                    case "item":
                        try {
                            translateItem(name, lang, translate);// Procurando e salvando a tradução para o item
                        } catch (ItemException e) {
                            continue;
                        }
                        break;
                    case "grupo":
                        try {
                            translateGrupo(name, lang, translate);// Procurando e salvando a tradução para o grupo
                        } catch (GrupoException e) {
                        }
                        break;
                    default:
                        try {
                            translateItem(name, lang, translate);// Procurando e salvando a tradução para o item
                        } catch (ItemException e) {
                        }
                        try {
                            translateGrupo(name, lang, translate);// Procurando e salvando a tradução para o grupo
                        } catch (GrupoException e) {
                        }
                }
            }
            file.delete();
        }
    }

    public static void translateItem(String name, String lang, String translate) throws ItemException {
        Item item = itemCtrl.findByName(name);
        if (Objects.isNull(item)) return;
        item.addTranslation(lang, translate);
        itemCtrl.update(item);
        Msg.ServidorGreen("$2Adicionando tradução para o item $6" + item.getName());
    }

    public static void translateGrupo(String name, String lang, String translate) throws GrupoException {
        Grupo grupo = grupoCtrl.findByName(name);
        if (Objects.isNull(grupo)) return;
        grupo.addTranslation(lang, translate);
        grupoCtrl.update(grupo);
        Msg.ServidorBlue("$9Adicionando tradução para o grupo $6" + grupo.getName());
    }

}