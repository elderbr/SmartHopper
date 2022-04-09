package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.model.ConfigModel;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    public static int VERSION() {
        return Integer.parseInt(YML.getString("version").replaceAll("[.]", ""));
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
        YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
        // INFORMAÇÕES
        add("author", "ElderBR", Arrays.asList("Criado e desenvolvido"));
        add("discord", "ElderBR#5398", Arrays.asList("Entre em contato"));

        // VERSÃO DO PLUGIN
        add("version", VGlobal.VERSION, Arrays.asList("Versão atual do plugin"));

        // Administrador do Smart Hopper
        add("adm",  Arrays.asList("ElderBR"),
                Arrays.asList("Adminsitradores é responsável por adicionar ou remover operadores",
                        "Fica responsável por adicionar, alterar ou remover grupos"));

        add("langs", Arrays.asList("pt_br", "pt_pt"), Arrays.asList("Linguagens disponível"));
    }

    public static YamlConfiguration GET_CONFIG() {
        return YML;
    }

    private void add(String key, String value, List<String> comment){
        YML.setComments(key, comment);
        YML.set(key, value);
    }
    private void add(String key, List<String> list, List<String> comment){
        YML.setComments(key, comment);
        YML.set(key, list);
    }


}
