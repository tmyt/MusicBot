package com.jagrosh.jmusicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.commands.DJCommand;

public class DisconnectCmd extends DJCommand
{
    public DisconnectCmd(Bot bot)
    {
        super(bot);
        this.name = "disconnect";
        this.help = "disconnect from VC";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bePlaying = false;
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        handler.stopAndClear();
        event.getGuild().getAudioManager().closeAudioConnection();
        event.reply(event.getClient().getSuccess()+" The player has stopped and the queue has been cleared.");
    }
}