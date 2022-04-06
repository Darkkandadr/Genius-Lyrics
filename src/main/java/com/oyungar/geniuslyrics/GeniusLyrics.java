package com.oyungar.geniuslyrics;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.security.auth.login.LoginException;
public class GeniusLyrics {

    public static String BOT_TOKEN = "OTA3OTc5NzY1OTA5MTgwNTA2.YYvEDQ.D-4i3u_QxGu2kf74O5NXDhCEGzI";

    public static void main(String[] args) throws LoginException, InterruptedException {
         JDA jda = JDABuilder.createDefault(BOT_TOKEN)
                 .addEventListeners(new GeniusLyricsListener())
                 .setActivity(Activity.playing("/lyrics for search a song's lyrics"))
                 .build();
        jda.awaitReady();
        jda.upsertCommand("lyrics", "Learn the lyrics of the song").addOption(OptionType.STRING,"song", "The song you want to learn the lyrics of", true).queue();
        jda.upsertCommand("help", "Help for your highness").queue();
        }


}
