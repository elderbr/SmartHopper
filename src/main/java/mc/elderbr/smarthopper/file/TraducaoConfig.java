package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TraducaoConfig {
    private final File directoryFile = new File(VGlobal.FILE_LANG.getAbsolutePath());

    private BufferedWriter escrever;
    private BufferedReader reader;
    private InputStream input;
    private String txtReader;

    private static YamlConfiguration yml;

    private File fileBR = new File(directoryFile, "pt_br.yml");
    private File filePT = new File(directoryFile, "pt_pt.yml");

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
            createGrupoBR();
        }

        reload();// Lendo todas as traduções
    }

    public YamlConfiguration configBR() {
        yml = YamlConfiguration.loadConfiguration(fileBR);
        return yml;
    }

    public YamlConfiguration configPT() {
        yml = YamlConfiguration.loadConfiguration(filePT);
        return yml;
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
                escrever.write(StringEscapeUtils.unescapeJava(txtReader));
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

    private void createGrupoBR() {
        try {

            input = getClass().getResourceAsStream("/grupo.yml");
            reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

            while ((txtReader = reader.readLine()) != null) {
                String grupoName = txtReader.split(":")[0].trim();
                String traducao = txtReader.split(":")[1].trim();
                VGlobal.TRADUCAO_GRUPO_NAME_MAP.put(grupoName, traducao);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
    }

    public void reload() {
        String lang = null;
        for (File files : directoryFile.listFiles()) {
            // Lendo o arquivo de tradução
            yml = YamlConfiguration.loadConfiguration(files);
            // Nome do linguagem
            lang = files.getName().substring(0, files.getName().indexOf(".")).trim().toLowerCase();

            int cod = 0;
            for (Item item : VGlobal.ITEM_LIST) {
                String name = item.getDsItem();
                if (yml.get(name) != null) {
                    cod = (item.getCdItem() - 1);
                    VGlobal.ITEM_LIST.get(cod).addTraducao(lang, yml.get(name).toString());
                }
            }
            files.delete();
        }
    }

}