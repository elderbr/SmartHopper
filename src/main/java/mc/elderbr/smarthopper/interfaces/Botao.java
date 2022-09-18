package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public interface Botao {

    default ItemStack BtnSalva(){
        ItemStack btn = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = btn.getItemMeta();
        meta.setDisplayName(Msg.Color("$2$lSalva"));
        meta.setLore(Arrays.asList(Msg.Color("$fSalva a lista")));
        meta.setCustomModelData(10);
        btn.setItemMeta(meta);
        return btn;
    }

    default ItemStack BtnProximo(){
        ItemStack btn = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = btn.getItemMeta();
        meta.setDisplayName(Msg.Color("$9$l Próximo"));
        meta.setLore(Arrays.asList(Msg.Color("$f Vai para próxima lista")));
        meta.setCustomModelData(11);
        btn.setItemMeta(meta);
        return btn;
    }

    default ItemStack BtnAnterior(){
        ItemStack btn = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = btn.getItemMeta();
        meta.setDisplayName(Msg.Color("$9$lRetorna"));
        meta.setLore(Arrays.asList(Msg.Color("$fRetorna a lista anteior")));
        meta.setCustomModelData(12);
        btn.setItemMeta(meta);
        return btn;
    }
}
