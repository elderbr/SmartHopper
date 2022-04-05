package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.dao.AdmDao;
import mc.elderbr.smarthopper.interfaces.Jogador;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.ConfigModel;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config {

    private final File directoryFile = new File(VGlobal.ARQUIVO.getAbsolutePath());
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
                saveDefault();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
    }

    //=================== VERSAO DO PLUGIN ===============================/
    public static double VERSION() {
        return Double.parseDouble(YML.getString("version").replaceAll(".", ""));
    }

    public static void SET_VERSION(String version) {
        try {
            YML.setComments("version", Arrays.asList("Versão atual do plugin"));
            YML.set("version", version);
            SAVE();
        } catch (IOException e) {
            Msg.ServidorErro("Erro ao salvar a versão do plugin!!!", "", Config.class, e);
        }
    }

    //=================== ADMINISTRADORES DO SMART HOPPER ===============================/
    public static void ADD_ADM() {
        try {
            YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
            Collections.sort(VGlobal.ADM_LIST);
            YML.setComments("adm", Arrays.asList("Administradores do SmartHopper"));
            YML.set("adm", VGlobal.ADM_LIST);
            SAVE();
            Msg.ServidorGreen("Adicionando novo adm", Config.class);
        } catch (IOException e) {
            Msg.ServidorErro("Erro ao salvar a lista de administrador no arquivo config", "ADD_ADM", Config.class, e);
        }
    }

    public static void REMOVER_ADM() {
        try {
            YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
            Collections.sort(VGlobal.ADM_LIST);
            YML.setComments("adm", Arrays.asList("Administradores do SmartHopper"));
            YML.set("adm", VGlobal.ADM_LIST);
            SAVE();
        } catch (IOException e) {
            Msg.ServidorErro("Erro ao remover a lista de administrador no arquivo config", "REMOVER_ADM", Config.class, e);
        }
    }


    //=================== LINGUAGENS USADAS NO JOGO ===============================/
    public static void ADD_LANG() {
        try {
            YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
            Collections.sort(VGlobal.LANG_NAME_LIST);
            YML.setComments("lang", Arrays.asList("Linguagens usadas na tradução"));
            YML.set("lang", VGlobal.LANG_NAME_LIST);
            SAVE();
        } catch (IOException e) {
            Msg.ServidorErro("Erro ao adicionar a lista de linguagem no arquivo config", "ADD_LANG", Config.class, e);
        }
    }

    public static void REMOVER_LANG() {
        try {
            YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
            Collections.sort(VGlobal.LANG_NAME_LIST);
            YML.setComments("lang", Arrays.asList("Linguagens usadas na tradução"));
            YML.set("lang", VGlobal.LANG_NAME_LIST);
            SAVE();
        } catch (IOException e) {
            Msg.ServidorErro("Erro ao remover a lista de linguagem no arquivo config", "ADD_LANG", Config.class, e);
        }
    }

    private static void SAVE() throws IOException {
        YML.save(FILE_CONFIG);
    }

    private void saveDefault() {

        CONFIG_MODEL = new ConfigModel();
        CONFIG_MODEL.setName("Separador inteligente - SmartHopper");
        CONFIG_MODEL.setVersao(VERSION());

        List<String> adm = new ArrayList<>();
        adm.add("ElderBR");
        CONFIG_MODEL.setAdm(adm);

        CONFIG_MODEL.setOperador(adm);

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
            escrever.write("versao: " + CONFIG_MODEL.getVersao());
            escrever.newLine();

            //Adminsitradores do plugin podem adicionar novos operadores ou remover
            escrever.write("#Adminsitradores é responsável por adicionar ou remover operadores" +
                    "\n#Fica responsável por adicionar, alterar ou remover grupos");
            escrever.newLine();
            escrever.write("adm:");
            for (String adms : CONFIG_MODEL.getAdm()) {
                escrever.write("\n  - " + adms);
            }
            escrever.newLine();

            //Operadores do plugin podem adicionar novos grupos ou remover
            escrever.write("#Operadores é responsável por adicionar, alterar ou deletar os grupos");
            escrever.newLine();
            escrever.write("operador:");
            for (String opers : CONFIG_MODEL.getOperador()) {
                escrever.write("\n  - " + opers);
            }
            escrever.newLine();

            //Linguagem disponiveis
            escrever.write("#Linguagens disponível");
            escrever.newLine();
            escrever.write("lang: ");
            CONFIG_MODEL.addLang("pt_br");
            CONFIG_MODEL.addLang("pt_pt");
            for (String langs : CONFIG_MODEL.getLang()) {
                escrever.write("\n  - " + langs);
            }
            escrever.flush();
            escrever.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {

        CONFIG_MODEL = new ConfigModel();

        // Informações do Plugin
        CONFIG_MODEL.setName("Separador inteligente - SmartHopper");
        CONFIG_MODEL.setVersao(4.0);

        // Adicionar Adm
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
            escrever.write("version: " + CONFIG_MODEL.getVersao());
            escrever.newLine();

            //Adminsitradores do plugin podem adicionar novos operadores ou remover
            escrever.write("#Adminsitradores é responsável por adicionar ou remover operadores" +
                    "\n#Fica responsável por adicionar, alterar ou remover grupos");
            escrever.newLine();
            escrever.write("adm:");
            for (String adms : CONFIG_MODEL.getAdm()) {
                escrever.write("\n  - " + adms);
            }
            escrever.newLine();

            //Operadores do plugin podem adicionar novos grupos ou remover
            escrever.write("#Operadores é responsável por adicionar, alterar ou deletar os grupos");
            escrever.newLine();
            escrever.write("operador:");
            for (String opers : CONFIG_MODEL.getOperador()) {
                escrever.write("\n  - " + opers);
            }
            escrever.newLine();

            //Linguagem disponiveis
            escrever.write("#Linguagens disponível");
            escrever.newLine();
            escrever.write("lang: ");
            for (String langs : CONFIG_MODEL.getLang()) {
                escrever.write("\n  - " + langs);
            }

            escrever.flush();
            escrever.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration GET_CONFIG() {
        return YML;
    }

    public static void REMOVER() {
        // REMOVE OS CAMPOS DESNECESSARIOS
        YML.set("isUpgradeItem", null);
        YML.set("isUpgradeGrupo", null);
        try {
            YML.save(FILE_CONFIG);
        } catch (IOException e) {
            Msg.ServidorErro(e, "REMOVER()", Config.class);
        }
    }

}
