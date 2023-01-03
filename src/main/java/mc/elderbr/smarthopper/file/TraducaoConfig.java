package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import static mc.elderbr.smarthopper.interfaces.VGlobal.TRADUCAO_GRUPO_LISTA;

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
            if (lang.equals("grupo")) {
              for(Map.Entry<String, Object> grups : yml.getValues(false).entrySet()){
                  TRADUCAO_GRUPO_LISTA.put(grups.getKey(), grups.getValue().toString());
              }
            }
            //files.delete();
        }
    }

}