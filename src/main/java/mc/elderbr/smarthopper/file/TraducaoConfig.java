package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Grupo;
import mc.elderbr.smarthopper.model.Item;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class TraducaoConfig {
    private final File directoryFile = new File(VGlobal.FILE_LANG.getAbsolutePath());

    private String name;
    private String traducao;

    private BufferedWriter escrever;
    private BufferedReader reader;
    private InputStream input;
    private String txtReader;

    private static YamlConfiguration yml;

    private File fileBR = new File(directoryFile, "pt_br.yml");
    private File fileGrupoBR = new File(directoryFile, "grupo.yml");
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

    private void createGrupoBR() {
        try {

            input = getClass().getResourceAsStream("/grupo.yml");
            reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

            escrever = Files.newBufferedWriter(fileGrupoBR.toPath(), StandardCharsets.UTF_8);

            while ((txtReader = reader.readLine()) != null) {
                escrever.write(txtReader);
                escrever.newLine();
                escrever.flush();
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
            Msg.ServidorGold("Nome do arquivo: " + lang, getClass());

            if (lang.equals("grupo")) {
                for (Grupo grupo : VGlobal.GRUPO_LIST) {
                    for (Map.Entry<String, Object> obj : yml.getValues(false).entrySet()) {
                        name = obj.getKey();
                        traducao = String.valueOf(obj.getValue());
                        if (name.equals(grupo.getName())) {
                            Msg.ServidorGold("Grupo nome: "+ grupo.getName()+" - codigo: "+ grupo.getCodigo(), getClass());
                            VGlobal.GRUPO_LIST.get(grupo.getCodigo()-1).addTraducao("pt_br", traducao);
                            break;
                        }
                    }
                }
            } else {

                for (Item item : VGlobal.ITEM_LIST) {
                    for (Map.Entry<String, Object> obj : yml.getValues(false).entrySet()) {
                        name = obj.getKey();
                        traducao = String.valueOf(obj.getValue());
                        if (name.equals(item.getName())) {
                            VGlobal.ITEM_LIST.get(item.getCodigo()-1).addTraducao(lang, traducao);
                            break;
                        }
                    }
                }
            }
            //files.delete();
        }
    }

}