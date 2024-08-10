package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.enums.EConfig;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigDao implements VGlobal {

    private static final File file = new File(ARQUIVO, "config.yml");
    private YamlConfiguration config;

    private final String KEY_ADM = EConfig.ADM.getKey();

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
        save(ADM_LIST, EConfig.ADM);
    }

    public void addAdm(String name) {
        ADM_LIST.add(name);
        Collections.sort(ADM_LIST);
        save(ADM_LIST, EConfig.ADM);
    }

    public boolean containsAdm(String name) {
        config = YamlConfiguration.loadConfiguration(file);
        return config.getList(KEY_ADM).contains(name);
    }

    public boolean containsAdm(Player player) {
        config = YamlConfiguration.loadConfiguration(file);
        return config.getList(KEY_ADM).contains(player.getName());
    }

    public void removeAdm(String name) {
        ADM_LIST.remove(name);
        Collections.sort(ADM_LIST);
        config = YamlConfiguration.loadConfiguration(file);
        save(ADM_LIST, EConfig.ADM);
    }

    public void findByAll() {
        config = YamlConfiguration.loadConfiguration(file);
        if (config.getList(KEY_ADM) == null) return;
        for (Object admName : config.getList(KEY_ADM)) {
            if (!ADM_LIST.contains(admName.toString())) {
                ADM_LIST.add(admName.toString());
            }
        }
        Collections.sort(ADM_LIST);
    }

    public void saveAuthor() {
        save(EConfig.AUTHOR);
    }

    public void saveDiscord() {
        save("discord", "ElderBR#5398", "Entre em contato através do Discord");
    }

    public void saveVersion() {
        save(EConfig.VERSION);
    }

    public void saveTutorial() {
        save(EConfig.TUTORIAL);
    }

    /**********************************************************
     *
     *              TEXTURE
     *
     **********************************************************/
    public void saveTexture() {
        save(EConfig.TEXTURE);
    }

    public void saveUseTexture(boolean useTexture) {
        save(useTexture, EConfig.USE_TEXTURE);
    }

    public void useTextureSave() {
        save(true, EConfig.USE_TEXTURE);
    }

    public static boolean useTexture() {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        return yml.getBoolean(EConfig.USE_TEXTURE.getKey());
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

    private void save(Object obj, EConfig type) {
        try {
            config = YamlConfiguration.loadConfiguration(file);
            config.set(type.getKey(), (obj == null ? type.getValue() : obj));
            config.setComments(type.getKey(), Arrays.asList(type.getComment()));
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar configuração do Smart Hopper");
        }
    }

    private void save(EConfig type) {
        try {
            config = YamlConfiguration.loadConfiguration(file);
            config.set(type.getKey(), type.getValue());
            config.setComments(type.getKey(), Arrays.asList(type.getComment()));
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar configuração do Smart Hopper");
        }
    }
}
