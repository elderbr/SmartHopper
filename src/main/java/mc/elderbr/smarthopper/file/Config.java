package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.Adm;
import mc.elderbr.smarthopper.model.ConfigModel;
import mc.elderbr.smarthopper.model.Lang;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Config {
    private static final File FILE_CONFIG = new File(VGlobal.ARQUIVO, "config.YML");
    private BufferedWriter escrever;
    private Charset utf8 = StandardCharsets.UTF_8;
    private static YamlConfiguration YML;
    public static ConfigModel CONFIG_MODEL;

    public Config() {

        CONFIG_MODEL = new ConfigModel();
        try {
            // Se o arquivo não existir cria
            if (!FILE_CONFIG.exists()) {
                FILE_CONFIG.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
        saveDefault();
    }

    private void saveDefault() {

        CONFIG_MODEL = new ConfigModel();
        CONFIG_MODEL.setName("Separador inteligente - SmartHopper");
        CONFIG_MODEL.setVersao(VGlobal.VERSION);

        List<String> adm = new ArrayList<>();
        adm.add("ElderBR");
        CONFIG_MODEL.setAdm(VGlobal.ADM_LIST);

        try {

            escrever = Files.newBufferedWriter(FILE_CONFIG.toPath(), utf8);

            // Informações do plugin
            escrever.write("#Configuração Separador de item inteligente SmartHopper");
            escrever.newLine();

            //Desenvolvedor
            escrever.write("author: ElderBR");
            escrever.newLine();
            escrever.write("discord: ElderBR#5398");
            escrever.newLine();

            //Versão do plugin
            escrever.write("#Versão do plugin");
            escrever.newLine();
            escrever.write("versao: " + CONFIG_MODEL.toVersao());
            escrever.newLine();

            //Adminsitradores do plugin podem adicionar novos operadores ou remover
            escrever.write("#Adminsitradores é responsável por adicionar ou remover operadores" +
                    "\n#Fica responsável por adicionar, alterar ou remover grupos");
            escrever.newLine();
            escrever.write("adm:");
            for (Adm adms : VGlobal.ADM_LIST) {
                if (adms.getCdCargo() == 1) {
                    escrever.newLine();
                    escrever.write("  -" + adms.getDsAdm());
                }
            }
            escrever.newLine();


            //Operadores do plugin podem adicionar novos grupos ou remover
            escrever.write("#Operadores é responsável por adicionar, alterar ou deletar os grupos");
            escrever.newLine();
            escrever.write("operador:");
            for (Adm adms : VGlobal.ADM_LIST) {
                if (adms.getCdCargo() == 2) {
                    escrever.newLine();
                    escrever.write("  -" + adms.getDsAdm());
                }
            }
            escrever.newLine();

            //Linguagem disponiveis
            escrever.write("#Linguagens disponível");
            escrever.newLine();
            escrever.write("lang: ");
            for(Map.Entry<String, Lang> lang : VGlobal.LANG_NAME_MAP.entrySet()){
                escrever.newLine();
                escrever.write("  -"+lang.getValue().getDsLang());
            }
            escrever.flush();
            escrever.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
