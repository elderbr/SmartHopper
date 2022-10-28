package mc.elderbr.smarthopper.exceptions;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class ItemException extends RuntimeException {
    private String world, msg;
    private int x, y, z;

    private final String pularLinha = "=====================================================\n";


    public ItemException() {
    }

    public ItemException(@NotNull String message) {
        super(message);
        msg = message;
    }

    public ItemException(@NotNull String message,@NotNull Block block) throws Exception {
        world = block.getWorld().getName();
        x = block.getLocation().getBlockX();
        y = block.getLocation().getBlockY();
        z = block.getLocation().getBlockZ();

        msg = pularLinha + message + "\nHopper localizado no mundo " + world + " x: " + x + " | y: " + y + " | z: " + z;

        Bukkit.getServer().getConsoleSender().sendMessage(msg);
        throw new Exception(msg);
    }

    @Override
    public String getMessage(){
        return msg;
    }
}
