package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.commands.MusicCommand;

public class JoinCmd extends MusicCommand {
    public JoinCmd(Bot bot) {
        super(bot);
        this.name = "join";
        this.arguments = "";
        this.help = "summon bot in VC";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.beListening = true;
        this.bePlaying = false;
    }

    @Override
    public void doCommand(CommandEvent event) { }
}