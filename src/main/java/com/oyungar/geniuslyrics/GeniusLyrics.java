package com.oyungar.geniuslyrics;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;


public class GeniusLyrics {

    private static final String BOT_TOKEN = "OTA3OTc5NzY1OTA5MTgwNTA2.YYvEDQ.D-4i3u_QxGu2kf74O5NXDhCEGzI";

    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
         JDA jda = JDABuilder.createDefault(BOT_TOKEN)
                 .addEventListeners(new GeniusLyricsListener())
                 .setActivity(Activity.playing("/lyrics for search a song's lyrics"))
                 .build();
        jda.awaitReady();
        jda.upsertCommand("lyrics", "Learn the lyrics of the song").addOption(OptionType.STRING,"song", "The song you want to learn the lyrics of", true).queue();
        jda.upsertCommand("help", "Help for your highness").queue();
        jda.upsertCommand("invite", "Invite me to your server").queue();
        PrintStream stream = new PrintStream(new FileOutputStream("console.log", true));
        System.setOut(stream);
        infoOut(jda);
    }

    private static void infoOut(@NotNull JDA jda) {
        var infoChannel = jda.getTextChannelById("963043965949583391");
        assert infoChannel != null;
        var guilds = jda.getGuilds();
        infoChannel.sendMessage("The guilds (" + guilds.size() +  ") that i serve:").queue();
        guilds.forEach(guild -> {
            infoChannel.sendMessage("Guild: **" + guild.getName() + "** Users: **" + guild.getMemberCount() + "**").queue();
        } );
    }

}
