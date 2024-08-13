package mc.elderbr.smarthopper.interfaces;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public interface IConfig extends IItem {

    default <T extends IItem> boolean save(Class<T> clazz) {
        try {
            IItem obj = clazz.getConstructor().newInstance();

            String clazzName = clazz.getClass().getSimpleName() + ".yml";
            File file = new File(clazzName);

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set(getName().toLowerCase().concat(".id"), getId());
            config.set(getName().toLowerCase().concat(".name"), getName().toLowerCase());
            config.set(getName().toLowerCase().concat(".lang"), getTranslations());
            config.save(file);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T extends IItem> T findById(Integer code){
        try {
            IItem obj = getClass().getConstructor().newInstance();
            return (T) obj;
        }catch (Exception e){
            throw new RuntimeException("Erro ao buscar pelo ID: "+ e.getMessage());
        }
    }

}
