package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
        if(!directoryFile.exists()){
            directoryFile.mkdir();
        }
        if(!fileBR.exists()) {
            createBR();
        }
        if(!filePT.exists()) {
            createTP();
        }
    }

    public YamlConfiguration configBR(){
        yml = YamlConfiguration.loadConfiguration(fileBR);
        return yml;
    }

    public YamlConfiguration configPT(){
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
                escrever.write(StringUtils.capitalize(txtReader));
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

}