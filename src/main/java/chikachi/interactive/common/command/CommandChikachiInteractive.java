package chikachi.interactive.common.command;

import chikachi.interactive.common.command.sub.*;
import chikachi.lib.common.command.sub.CommandChikachiBase;
import chikachi.lib.common.command.sub.CommandChikachiBasePlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;

public class CommandChikachiInteractive extends CommandChikachiBasePlayer {
    private Hashtable<String, CommandChikachiBase> subCommands = new Hashtable<String, CommandChikachiBase>();

    public CommandChikachiInteractive() {
        super("interactive");
        RegisterSubCommandHandler(new CommandChikachiInteractiveReload());
        RegisterSubCommandHandler(new CommandChikachiInteractiveStart());
        RegisterSubCommandHandler(new CommandChikachiInteractiveStatus());
        RegisterSubCommandHandler(new CommandChikachiInteractiveStop());
        RegisterSubCommandHandler(new CommandChikachiInteractiveTest());
        RegisterSubCommandHandler(new CommandChikachiInteractiveGui());
        RegisterSubCommandHandler(new CommandChikachiInteractiveRestart());
    }

    public void RegisterSubCommandHandler(CommandChikachiBase handler) {
        String[] aliases = handler.getAliases();
        for (String alias : aliases) {
            if (!subCommands.containsKey(alias)) {
                subCommands.put(alias, handler);
            }
        }
    }

    @Override
    public void execute(EntityPlayerMP sender, String[] args) {
        if (args.length > 0) {
            String arg = args[0];

            if (subCommands.containsKey(arg)) {
                CommandChikachiBase subCommand = subCommands.get(arg);
                if (subCommand.canCommandSenderUseCommand(sender)) {
                    subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                } else {
                    sender.addChatMessage(new ChatComponentTranslation("commands.generic.permission").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                }
                return;
            }
        }

        sender.addChatMessage(new ChatComponentTranslation("chat.ChikachiLib.invalidArguments").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
    }

    @Override
    public boolean canCommandSenderUseCommand(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i) {
        return false;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 0) return null;
        if (args.length == 1) {
            List<String> matchingSubCommands = new ArrayList<String>();

            Enumeration<String> keys = subCommands.keys();

            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                if (key.startsWith(args[0])) {
                    matchingSubCommands.add(key);
                }
            }

            return matchingSubCommands;
        }

        String arg = args[0];

        if (subCommands.containsKey(arg)) {
            CommandChikachiBase subCommand = subCommands.get(arg);
            return subCommand.addTabCompletionOptions(sender, Arrays.copyOfRange(args, 1, args.length));
        }

        return null;
    }
}
