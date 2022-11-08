package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.model.Item;
import org.bukkit.enchantments.Enchantment;

public interface LivroEncantado {

    Item setName(String name);
    String getName();

    default Enchantment getEncantamento(){
        String encantamento = getName().replaceAll("enchanted book","").trim();
        for (Enchantment enchantment : Enchantment.values()) {
            String nameEnch = enchantment.getKey().getKey().replaceAll("_", " ").toLowerCase();
            if(encantamento.equalsIgnoreCase(nameEnch)){
                return enchantment;
            }
        }
        return null;
    }
}
