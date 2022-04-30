package com.oyungar.geniuslyrics;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.*;


public class GeniusLyrics {

    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
         JDA jda = JDABuilder.createDefault(getToken())
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

    private static String getToken() throws IOException {
        var file = new File("token.lyrics");
        if (!file.exists()){
            file.createNewFile();
            System.out.println("[SYSTEM] Discord bot token file doesn't found. 'token.lyrics' file created.");
            return null;
        }

        var reader = new BufferedReader(new FileReader("token.lyrics"));
        var token = reader.readLine();
        reader.close();
        return token;
    }
}
