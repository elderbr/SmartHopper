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
<<<<<<< HEAD
import java.nio.file.Files;
import java.util.ArrayList;
=======
import java.util.Arrays;
import java.util.Collections;
>>>>>>> v4.0.0
import java.util.List;
import java.util.Map;

public class Config {
    private static final File FILE_CONFIG = new File(VGlobal.ARQUIVO, "config.yml");
    private BufferedWriter escrever;
    private Charset utf8 = StandardCharsets.UTF_8;
    private static YamlConfiguration config;
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
<<<<<<< HEAD
        config = YamlConfiguration.loadConfiguration(FILE_CONFIG);
        //saveDefault();
=======
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
>>>>>>> v4.0.0
    }

    private void saveDefault() {
        YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
        // INFORMAÇÕES
        add("author", "ElderBR", Arrays.asList("Criado e desenvolvido"));
        add("discord", "ElderBR#5398", Arrays.asList("Entre em contato"));

<<<<<<< HEAD
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

    public static void setVersion(){
        if(VGlobal.VERSAO > Config.Version()) {
            config.set("version", VGlobal.VERSION);
            try {
                Debugs.escrever("Alterando a versão do plugin");
                config.save(FILE_CONFIG);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static int Version(){
        return Integer.parseInt(config.getString("version").replaceAll("\\.",""));
    } 
=======
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


>>>>>>> v4.0.0
}
