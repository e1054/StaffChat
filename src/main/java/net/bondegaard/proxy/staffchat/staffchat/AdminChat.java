package net.bondegaard.proxy.staffchat.staffchat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.bondegaard.proxy.staffchat.Main;

public class AdminChat extends Command {

    protected String message = Main.getInstance().getConfig().getString("AdminChat-Format");

    private final Main plugin;

    public AdminChat(Main plugin)
    {
        super("adminchat", "adminchat.use", "ac");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer))
        {
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;

        String message = getMessage(args, 0);
        String server = player.getServer().getInfo().getName();

        plugin.getProxy().getPlayers().stream().filter(proxiedPlayer -> proxiedPlayer.hasPermission("adminchat.use")).forEach(proxiedPlayer ->
        {
            proxiedPlayer.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', this.message.replace("{player}", player.getName()).replace("{server}", server).replace("{message}", message))));
        });

    }
    private String getMessage(String[] args, int x)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = x; i < args.length; i++)
        {
            stringBuilder.append(args[i]).append( x >= args.length - 1 ? "" : " " );
        }
        return stringBuilder.toString();
    }

}
