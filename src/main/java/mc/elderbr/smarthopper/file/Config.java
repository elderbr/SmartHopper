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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config {
    public final File directoryFile = new File(VGlobal.ARQUIVO.getAbsolutePath());
    private static final File FILE_CONFIG = new File(VGlobal.ARQUIVO, "config.YML");
    private BufferedWriter escrever;
    private Charset utf8 = StandardCharsets.UTF_8;
    private static YamlConfiguration YML;
    public static ConfigModel CONFIG_MODEL;

    public static List<String> ADM_LIST;
    public static List<String> OPERADOR_LIST;
    public static List<String> LANG_LIST = new ArrayList<>();

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
        ADM_LIST = YML.getStringList("adm");// Pega a lista de adm
        OPERADOR_LIST = YML.getStringList("operador");// Pega a lista dos operadores
        LANG_LIST = YML.getStringList("lang");// Pega lista de linguagem
    }

    //=================== VERSAO DO PLUGIN ===============================/
    public static double VERSION() {
        return Double.parseDouble(YML.getString("version").replaceAll(".", ""));
    }

    public static void SET_VERSION(String version) throws IOException {
        YML.set("version", version);
        YML.save(FILE_CONFIG);
    }

    //=================== ADMINSTRADORES ===============================/

    public static boolean AddAdmList(String player) throws IOException {
        if (!ADM_LIST.contains(player)) {// Se o player não estiver na lista adiciona
            ADM_LIST.add(player);
            Collections.sort(ADM_LIST);
            YML.set("adm", ADM_LIST);
            YML.save(FILE_CONFIG);
            return true;
        }
        return false;
    }

    public static boolean RemoveAddList(String player) throws IOException {
        if (ADM_LIST.contains(player)) {
            ADM_LIST.remove(player);// Remove da lista do adm
            Collections.sort(ADM_LIST);
            YML.set("adm", ADM_LIST);
            YML.save(FILE_CONFIG);
            return true;
        }
        return false;
    }

    //=================== OPERADORES ===============================/

    public static boolean AddOperadorList(String player) throws IOException {
        if (!OPERADOR_LIST.contains(player)) {
            OPERADOR_LIST.add(player);
            Collections.sort(OPERADOR_LIST);
            YML.set("operador", OPERADOR_LIST);
            YML.save(FILE_CONFIG);
            return true;
        }
        return false;
    }

    public static boolean RemoveOperadorList(String player) throws IOException {
        if (OPERADOR_LIST.contains(player)) {
            OPERADOR_LIST.remove(player);
            Collections.sort(OPERADOR_LIST);
            YML.set("operador", OPERADOR_LIST);
            YML.save(FILE_CONFIG);
            return true;
        }
        return false;
    }

    //================== LANGUAGEM ================================/

    public List<String> getLangList() {
        return LANG_LIST;
    }

    public static boolean AddLangList(String lang) throws IOException {
        if (!LANG_LIST.contains(lang)) {
            LANG_LIST.add(lang);
            Collections.sort(LANG_LIST);
            YML.set("lang", LANG_LIST);
            YML.save(FILE_CONFIG);
            return true;
        }
        return false;
    }

    public boolean removerLangList(String lang) {
        if (LANG_LIST.contains(lang)) {// Verifica se existe a linguagem na lista
            LANG_LIST.remove(lang);// Remove da lista da linguagem
            return true;
        }
        return false;
    }

    private void saveDefault() {

        CONFIG_MODEL = new ConfigModel();
        CONFIG_MODEL.setName("Separador inteligente - SmartHopper");
        CONFIG_MODEL.setVersao(4.0);

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
        CONFIG_MODEL.setAdm(ADM_LIST);

        // Adicionar Operador
        CONFIG_MODEL.setOperador(OPERADOR_LIST);

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
