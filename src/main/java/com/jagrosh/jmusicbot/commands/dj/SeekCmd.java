package com.jagrosh.jmusicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.commands.DJCommand;
import com.jagrosh.jmusicbot.utils.FormatUtil;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekCmd  extends DJCommand
{
    public SeekCmd(Bot bot)
    {
        super(bot);
        this.name = "seek";
        this.help = "seek music";
        this.arguments = "[+|-][[hh:]mm:]<ss>";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bePlaying = true;
    }

    // override musiccommand's execute because we don't actually care where this is used
    @Override
    public void doCommand(CommandEvent event){
        String args = event.getArgs();
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        if(args.isEmpty()) {
            String message = event.getClient().getWarning() + " Seek Commands:\n" +
                    "\n`" + event.getClient().getPrefix() + name + " [[hh:]mm:]ss` - Seek to position" +
                    "\n`" + event.getClient().getPrefix() + name + " +[[hh:]mm:]ss` - Seek forward" +
                    "\n`" + event.getClient().getPrefix() + name + " -[[hh:]mm:]ss` - Seek backward";
            event.reply(message);
            return;
        }

        AudioTrack track = handler.getPlayer().getPlayingTrack();
        if(!track.isSeekable()){
            event.reply(event.getClient().getError()+" This track could not be seekable.");
            return;
        }
        if(track.getDuration() == Long.MAX_VALUE){
            event.reply(event.getClient().getError()+" This track could not be seekable.");
            return;
        }

        boolean isRelative = false;
        boolean seekForward = true;
        if(args.startsWith("+")){
            isRelative = true;
        }
        else if(args.startsWith("-")){
            isRelative = true;
            seekForward = false;
        }else if(!isStarsWithNumber(args)){
            event.reply(event.getClient().getError()+" argument is not parsable.");
            return;
        }
        if(isRelative){
            args = args.substring(1);
        }
        String[] parts = args.split(":");
        if(parts.length > 3){
            event.reply(event.getClient().getError()+" argument is not parsable.");
            return;
        }

        int h = 0, m = 0, s = 0;
        try {
            if (parts.length > 0) {
                s = Integer.parseInt(parts[0]);
            }
            if (parts.length > 1) {
                m = s;
                s = Integer.parseInt(parts[1]);
            }
            if (parts.length > 2) {
                h = m;
                m = s;
                s = Integer.parseInt(parts[2]);
            }
        }catch(NumberFormatException e){
            event.reply(event.getClient().getError()+" argument is not parsable.");
            return;
        }
        long position = (h * 3600 + m * 60 + s) * 1000;
        long current = track.getPosition();
        if(isRelative){
            if(seekForward){
                current += position;
            }else{
                current -= position;
            }
            current = Math.min(Math.max(0, current), track.getDuration());
        }else{
            current = Math.min(Math.max(0, position), track.getDuration());
        }
        track.setPosition(current);
        event.reply(event.getClient().getSuccess()+" Seeked to `"+FormatUtil.formatTime(current)+"`");
    }

    private boolean isStarsWithNumber(String s){
        byte b = s.getBytes()[0];
        return '0' <= b && b <= '9';
    }
}
