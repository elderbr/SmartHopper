package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.interfaces.VGlobal;
import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigDao implements VGlobal {

    private static final File file = new File(VGlobal.ARQUIVO, "config.yml");
    private YamlConfiguration config;

    public ConfigDao() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        config = YamlConfiguration.loadConfiguration(file);
        saveVersion();// Salvando a versão atual
    }

    public void createFile() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar o arquivo config.yml");
        }
    }

    public void deleteFile() {
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o arquivo config.yml");
        }
    }

    public void restart() {
        findByAll();// Carrega todos os nome do Adm
        deleteFile();// Deleta o arquivo config.yml
        createFile();// Cria o arquivo config.yml
        saveAuthor();// Adiciona o autor no arquivo config.yml
        saveDiscord();// Adicionando o discord no arquivo config.yml
        saveVersion();// Adicionando a versão no arquivo config.yml
        saveTutorial();// Adicionando o link do tutorial no arquivo config.yml
        saveTexture();// Adicionando o link para o download da textura no arquivo config.yml
        useTextureSave();// Adicionando a pergunta do uso da textura
        save("adm", ADM_LIST, "Administradores do Smart Hopper");
    }

    public void addAdm(String name) {
        ADM_LIST.add(name);
        Collections.sort(ADM_LIST);
        save("adm", ADM_LIST, "Administradores do Smart Hopper");
    }

    public boolean containsAdm(String name) {
        config = YamlConfiguration.loadConfiguration(file);
        return config.getList("adm").contains(name);
    }
    public boolean containsAdm(Player player) {
        config = YamlConfiguration.loadConfiguration(file);
        return config.getList("adm").contains(player.getName());
    }

    public void removeAdm(String name) {
        ADM_LIST.remove(name);
        Collections.sort(ADM_LIST);
        config = YamlConfiguration.loadConfiguration(file);
        save("adm", ADM_LIST, "Administradores do Smart Hopper");
    }

    public void findByAll() {
        config = YamlConfiguration.loadConfiguration(file);
        if(config.getList("adm") == null ) return;
        for (Object admName : config.getList("adm")) {
            if (!ADM_LIST.contains(admName.toString())) {
                ADM_LIST.add(admName.toString());
            }
        }
        Collections.sort(ADM_LIST);
    }

    public void saveAuthor() {
        save("author", "ElderBR", "Configuração Separador inteligente SmartHopper", "", "Desenvolvidor");
    }

    public void saveDiscord() {
        save("discord", "ElderBR#5398", "Entre em contato através do Discord");
    }

    public void saveVersion() {
        save("version", VERSION, "Versão atual do plugin");
    }

    public void saveTutorial() {
        save("tutorial", "https://youtube/fBIeZ57ka1M?si=ZqE5TQRG2KjlaHdD", "Tutorial de como usar");
    }

    public void saveTexture(){
        try {
            List<String> list = new ArrayList<>();
            list.add("Para ter uma visualização melhor use a textura personalizada do Smart Hopper");
            list.add("a textura traz imagens para os botões de navegação de pagina do grupo");
            list.add("textura do botão próxima página");
            list.add("textura do botão anterior retorna a página anterior");
            list.add("textura do botão salvar que salva ou atera o grupo");
            config.set("texture", "http://elderbr.com/minecraft/textures/SmartHopper.zip");
            config.setComments("texture", list);
            config.save(file);
        }catch (IOException e){
            Msg.ServidorErro("Erro ao criar a textura no config!!!", "saveTexture()", getClass(), e);
        }
    }

    public void useTextureSave(){
        save("useTexture", true, "Se for setado como verdadeiro obrigado o jogador a usar a textura");
    }

    public static boolean useTexture(){
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        return yml.getBoolean("useTexture");
    }

    private void save(String key, Object value, String... comment) {
        try {
            config = YamlConfiguration.loadConfiguration(file);
            config.set(key, value);
            if (comment.length > 0) {
                config.setComments(key, Arrays.asList(comment));
            }
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
