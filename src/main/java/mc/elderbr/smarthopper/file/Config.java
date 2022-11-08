package mc.elderbr.smarthopper.file;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config implements VGlobal{

    private static final File FILE_CONFIG = new File(VGlobal.ARQUIVO, "config.yml");
    private BufferedWriter escrever;
    private Charset utf8 = StandardCharsets.UTF_8;
    private static YamlConfiguration YML;

    public Config() {

        try {
            // Se o arquivo não existir cria
            if (!FILE_CONFIG.exists()) {
                FILE_CONFIG.createNewFile();
                saveDefault();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        reload();
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

    public static void SET_VERSION(){
        YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
        YML.set("version", VERSION);
        try {
            SAVE();
        }catch (IOException e){
            throw new RuntimeException("Erro ao salvar a versão do plugin!");
        }
    }

    //=================== ADMINISTRADORES DO SMART HOPPER ===============================/
    public static boolean ADD_ADM(String player) {
        try {
            YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
            VGlobal.ADM_LIST.add(player);
            Collections.sort(VGlobal.ADM_LIST);
            YML.setComments("adm", Arrays.asList("Administradores do SmartHopper"));
            YML.set("adm", VGlobal.ADM_LIST);
            SAVE();
            return true;
        } catch (IOException e) {
            Msg.ServidorErro("Erro ao salvar a lista de administrador no arquivo config", "ADD_ADM", Config.class, e);
        }
        return false;
    }
    public static boolean CONTAINS_ADD(Object player){
        YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
        if(player instanceof Player player1) {
            return YML.getList("adm").contains(player1.getName());
        }
        if(player instanceof String){
            return YML.getList("adm").contains(player);
        }
        return false;
    }

    public static boolean REMOVE_ADM(String player) {
        try {
            YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
            VGlobal.ADM_LIST.remove(player);
            Collections.sort(VGlobal.ADM_LIST);
            YML.setComments("adm", Arrays.asList("Administradores do SmartHopper"));
            YML.set("adm", VGlobal.ADM_LIST);
            SAVE();
            return true;
        } catch (IOException e) {
            Msg.ServidorErro("Erro ao remover da lista de administrador no arquivo config", "REMOVE_ADM", Config.class, e);
        }
        return false;
    }

    public void reload(){
        YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
        for(Object adm : YML.getList("adm")){
            VGlobal.ADM_LIST.add(adm.toString());
        }
        // Adicionando a lista de linguagem
        for(Object lang : YML.getList("lang")){
            VGlobal.LANG_NAME_LIST.add(lang.toString());
        }
        Collections.sort(VGlobal.ADM_LIST);
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

        add("isUseTexture", true, Arrays.asList("Usa textura personalizada para os botões de navegação dos itens do grupo"));

        // Administrador do Smart Hopper
        add("adm", Arrays.asList("ElderBR"),
                Arrays.asList("Adminsitradores é responsável por adicionar ou remover operadores",
                        "Fica responsável por adicionar, alterar ou remover grupos"));

        add("langs", Arrays.asList("pt_br", "pt_pt"), Arrays.asList("Linguagens disponível"));
    }

    private void add(String key, Object value, List<String> comment) {
        YML.setComments(key, comment);
        YML.set(key, value);
    }

    private void add(String key, List<String> list, List<String> comment) {
        YML.setComments(key, comment);
        YML.set(key, list);
    }

    public static void SetUpdateItem(@NotNull  boolean status) {
        try {
            YML.setComments("item atualizado", Arrays.asList("Se a lista de item está atualizado"));
            YML.set("item atualizado", status);
            YML.save(FILE_CONFIG);
        } catch (IOException e) {
            Msg.ServidorErro("Erro ao atualizar o estado do item!!!", "setItemUpdate()", Config.class, e);
        }
    }

    public static boolean isItemUpdate() {
        return YML.getBoolean("item atualizado");
    }

    public static void SetUpdateGrupo(@NotNull boolean status) {
        try {
            YML.setComments("grupo atualizado", Arrays.asList("Se a lista do grupo está atualizada"));
            YML.set("grupo atualizado", status);
            YML.save(FILE_CONFIG);
        } catch (IOException e) {
            Msg.ServidorErro("Erro ao atualizar o estado do grupo!!!", "setGrupoUpdate()", Config.class, e);
        }
    }

    public static boolean IsGrupoUpdate() {
        return YML.getBoolean("grupo atualizado");
    }

    /***
     *
     *  USO DA TEXTURA NO SERVIDOR
     *
     */
    public static boolean ExistUseTexture(){
        YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
        return (YML.get("isUseTexture")!=null);
    }
    public static boolean IsUseTexture(){
        YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
        return YML.getBoolean("isUseTexture");
    }

    public static void SetUseTexture(boolean use) throws IOException {
        YML = YamlConfiguration.loadConfiguration(FILE_CONFIG);
        YML.setComments("isUseTexture",
                Arrays.asList("Usa textura personalizada para os botões de navegação dos itens do grupo"
                , "link texture pack http://elderbr.com/minecraft/textures/SmartHopper.zip"));
        YML.set("isUseTexture", use);
        SAVE();
    }


}