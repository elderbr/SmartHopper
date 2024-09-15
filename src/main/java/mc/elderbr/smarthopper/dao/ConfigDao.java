package mc.elderbr.smarthopper.dao;

import mc.elderbr.smarthopper.enums.EConfig;
import mc.elderbr.smarthopper.exceptions.ConfigException;
import mc.elderbr.smarthopper.interfaces.VGlobal;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    public void saveAdm() {
        save(EConfig.ADM);
    }

    public boolean addAdm(String name) {
        List<String> adms = findFileByAdms();// Pegando a lista de administradores
        adms.add(name);
        Collections.sort(adms);
        save(adms, EConfig.ADM);
        ADM_LIST.add(name);// Adicionando na lista de adm na variavel global
        return true;
    }

    public boolean containsAdm(String name) {
        config = YamlConfiguration.loadConfiguration(file);
        if (config.get(KEY_ADM) == null) {
            throw new ConfigException("Lista de Adm não existe!");
        }
        return config.getList(KEY_ADM).contains(name);
    }

    public boolean containsAdm(Player player) {
        config = YamlConfiguration.loadConfiguration(file);
        if (config.get(KEY_ADM) == null) {
            throw new ConfigException("Lista de Adm não existe!");
        }
        return config.getList(KEY_ADM).contains(player.getName());
    }

    public void removeAdm(String name) {
        List<String> adms = findFileByAdms();// Busca os adm no arquivo config.yml
        adms.remove(name);// Remove da lista
        Collections.sort(adms);// Organiza em ordem alfabética
        save(adms, EConfig.ADM);// Salvando a lista de adms
        ADM_LIST.remove(name);// Removendo adm da lista global
    }

    public List<String> findByAdms(){
        return ADM_LIST;
    }

    public List<String> findFileByAdms() {
        Set<String> list = new HashSet<>();
        config = YamlConfiguration.loadConfiguration(file);// Acessando o arquivo config.yml
        ADM_LIST.clear();// Limpando a lista de adm global
        for (Object name : config.getList(KEY_ADM)) {
            list.add(name.toString());// Adicionando adm na lista
            ADM_LIST.add(name.toString());// Adicionando adm na lista global
        }
        return new ArrayList<>(list);
    }

    public void saveAuthor() {
        save(EConfig.AUTHOR);
    }

    public void saveDiscord() {
        save(EConfig.DISCORD);
    }

    public void saveVersion() {
        save(EConfig.VERSION);
    }

    public int findByVersion() {
        String version = config.getString(EConfig.VERSION.getKey());
        if (version == null) {
            throw new ConfigException("Não foi encontrado dados da versão!");
        }
        try {
            return Integer.parseInt(version.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new ConfigException("Não foi encontrado dados da versão:\nErro: " + e.getMessage());
        }
    }

    public void saveTutorial() {
        save(EConfig.TUTORIAL);
    }

    public String findByTutorial() {
        return config.getString(EConfig.TUTORIAL.getKey());
    }

    /**********************************************************
     *
     *              TEXTURE
     *
     **********************************************************/
    public void saveTexture() {
        save(EConfig.TEXTURE);
    }

    public String findByTexture() {
        config = YamlConfiguration.loadConfiguration(file);// Acessando o arquivo config.yml
        String key = EConfig.TEXTURE.getKey();
        if (config.getString(key) == null) {
            throw new ConfigException("O link da textura não encontrado!");
        }
        return config.getString(key);
    }

    public void saveUseTexture(boolean useTexture) {
        save(useTexture, EConfig.USE_TEXTURE);
    }

    public void saveUseTexture() {
        save(true, EConfig.USE_TEXTURE);
    }

    public boolean findByUseTexture() {
        config = YamlConfiguration.loadConfiguration(file);// Acessando o arquivo config.yml
        String key = EConfig.USE_TEXTURE.getKey();
        if (config.get(key) == null) {
            throw new ConfigException("Não foi encontrado o \"useTexture\"!");
        }
        return config.getBoolean(key);
    }

    /**********************************************************
     *                       SAVES
     **********************************************************/

    private void save(String key, Object value, String... comment) {
        try {
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
            config.set(type.getKey(), (obj == null ? type.getValue() : obj));
            config.setComments(type.getKey(), Arrays.asList(type.getComment()));
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar configuração do Smart Hopper");
        }
    }

    private void save(EConfig type) {
        try {
            config.set(type.getKey(), type.getValue());
            config.setComments(type.getKey(), Arrays.asList(type.getComment()));
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar configuração do Smart Hopper");
        }
    }
}
