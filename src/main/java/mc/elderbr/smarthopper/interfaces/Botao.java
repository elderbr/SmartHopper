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

    default ItemStack BtnProximoPag2(){
        ItemStack btn = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = btn.getItemMeta();
        meta.setDisplayName(Msg.Color("$9$l Ir para pagina 2"));
        meta.setLore(Arrays.asList(Msg.Color("$f Vai para próxima lista")));
        meta.setCustomModelData(11);
        btn.setItemMeta(meta);
        return btn;
    }

    default ItemStack BtnProximoPag3(){
        ItemStack btn = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = btn.getItemMeta();
        meta.setDisplayName(Msg.Color("$9$l Ir para página 3"));
        meta.setLore(Arrays.asList(Msg.Color("$f Vai para próxima lista")));
        meta.setCustomModelData(11);
        btn.setItemMeta(meta);
        return btn;
    }

    default ItemStack BtnAnteriorPag1(){
        ItemStack btn = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = btn.getItemMeta();
        meta.setDisplayName(Msg.Color("$9$lRetorna a página 1"));
        meta.setLore(Arrays.asList(Msg.Color("$fRetorna a lista anteior")));
        meta.setCustomModelData(12);
        btn.setItemMeta(meta);
        return btn;
    }

    default ItemStack BtnAnteriorPag2(){
        ItemStack btn = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = btn.getItemMeta();
        meta.setDisplayName(Msg.Color("$9$lRetorna para página 2"));
        meta.setLore(Arrays.asList(Msg.Color("$fRetorna a lista anteior")));
        meta.setCustomModelData(12);
        btn.setItemMeta(meta);
        return btn;
    }

    default boolean equalButton(ItemStack itemStack){
        if(itemStack.equals(BtnSalva())){
            return true;
        }
        if(itemStack.equals(BtnProximo())){
            return true;
        }
        if(itemStack.equals(BtnProximoPag2())){
            return true;
        }
        if(itemStack.equals(BtnAnteriorPag1())){
            return true;
        }
        if(itemStack.equals(BtnAnteriorPag2())){
            return true;
        }
        if(itemStack.equals(BtnProximoPag3())){
            return true;
        }
        return false;
    }
}
