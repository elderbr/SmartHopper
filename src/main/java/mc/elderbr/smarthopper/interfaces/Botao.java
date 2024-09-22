package mc.elderbr.smarthopper.interfaces;

import mc.elderbr.smarthopper.model.Item;
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

    default Item BtnNavegation(){
        return new Item(new ItemStack(Material.BARRIER, 1));
    }

    default ItemStack BtnNextPage(int page){
        ItemStack btn = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = btn.getItemMeta();
        meta.setDisplayName("§9§lPágina "+ (page));
        meta.setLore(Arrays.asList("§fIr para a página "+(page)));
        meta.setCustomModelData(11);
        btn.setItemMeta(meta);
        return btn;
    }

    default ItemStack BtnPreviewPage(int page){
        ItemStack btn = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = btn.getItemMeta();
        meta.setDisplayName("§9§lRetorna página "+ (page));
        meta.setLore(Arrays.asList("§fRetorna para página "+ (page)));
        meta.setCustomModelData(12);
        btn.setItemMeta(meta);
        return btn;
    }

    default boolean equalButton(ItemStack itemStack){
        if(itemStack == null
                || itemStack.getType() != Material.BARRIER
                || !itemStack.hasItemMeta()
                || !itemStack.getItemMeta().hasCustomModelData()
                || itemStack.getItemMeta().getCustomModelData() < 1
                ){
            return false;
        }
        switch (itemStack.getItemMeta().getCustomModelData()){
            case 10:
            case 11:
            case 12:
                return true;
            default:
                return false;
        }
    }
}
