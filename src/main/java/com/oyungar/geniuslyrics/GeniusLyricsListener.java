package com.oyungar.geniuslyrics;

import core.GLA;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Objects;

public class GeniusLyricsListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        // Lyrics command
        if (event.getName().equals("lyrics")){
            event.deferReply().queue();
            var songName = event.getOption("song").getAsString();
            getLyrics(event, songName);
        }

        // Now Playing command
        else if (event.getName().equals("nowplaying")){
            event.deferReply().queue();
            var spotifyTrack = getSpotifyTrack(event.getMember());
            if (spotifyTrack != null){
                getLyrics(event, spotifyTrack);
            }
            else {
                InputStream is = getClass().getClassLoader().getResourceAsStream("images/not-listening-to-spotify.png");
                if (is != null) {
                    event.getHook().sendMessage(":warning: You are not currently listening to Spotify" +
                                    " or your Discord activity for Spotify is not visible.")
                                    .addFile(is, "not-listening-to-spotify.png").queue();
                }
            }
        }

        // Help command
        else if (event.getName().equals("help")){
            var embedBuilder = new EmbedBuilder();
            embedBuilder
                    .setTitle("Genius Lyrics Help")
                    .setThumbnail("https://assets.genius.com/images/apple-touch-icon.png")
                    .setColor(Color.yellow)
                    .addField("Lyrics Command", "`/lyrics <song name>`", false)
                    .addField("Invite Command", "`/invite`", false)
                    .addField("Help Command", "`/help`", false);
            event.replyEmbeds(embedBuilder.build()).queue();
        }

        // Invite command
        else if (event.getName().equals("invite")){
            var inviteLink = "https://discord.com/oauth2/authorize?client_id=907979765909180506&scope=bot+applications.commands";
            event.reply("Click the button for invite me to your server!")
                    .addActionRow(Button.link(inviteLink, "Invite me to your server")).queue();
        }
    }

    // Guild Join Event
    @Override
    public void onGuildJoin (@NotNull GuildJoinEvent event){
        System.out.println("[DCLOG " + getTime() + " ] Joined guild named: " + event.getGuild().getName());
    }

    // Guild Leave Event
    @Override
    public void onGuildLeave (@NotNull GuildLeaveEvent event){
        System.out.println("[DCLOG " + getTime() + " ] Left guild named: " + event.getGuild().getName());
    }

    // Search for the lyrics of a song
    private void getLyrics(@NotNull SlashCommandInteractionEvent event, String songName) {
        try {
            var geniusApi = new GLA();
            var searchResult = geniusApi.search(songName).getHits().getFirst();

            // If search result is null, then the song was not found
            if (searchResult == null || searchResult.getUrl().isEmpty()){return;}

            // If search result is not null, then the song was found
            if (!searchResult.getUrl().isEmpty()){
                if (searchResult.fetchLyrics().toCharArray().length > 4096) {
                    // If the lyrics are too long, then send a message saying so
                    InputStream is = getClass().getClassLoader().getResourceAsStream("images/too-long.png");
                    if (is != null) {
                        event.getHook()
                                .sendMessage(":warning: Lyrics of the sound is reaching "
                                        + "**message limit of Discord** "
                                        + "you can visit [Genius Page of "+ searchResult.getTitle() +"]"
                                        + "(" + searchResult.getUrl() + ")").addFile(is, "too-long.png").queue();
                        System.out.println("[DCLOG " + getTime() +  " ] DescriptionTooLongException for " +
                                songName + " at " + event.getGuild().getName());
                    }
                }
                else{
                    // If the lyrics are not too long, then send them
                    var embedContent = EmbedTemplate.embedBuilder(searchResult, event.getUser());
                    event.getHook().sendMessageEmbeds(embedContent.build()).queue();
                    System.out.println("[DCLOG " + getTime() +  " ] Lyrics for " + songName + " at " + event.getGuild().getName());
                }
            }
        }
        catch (IOException e){
            System.out.println("[GENIUSAPI] Error: " + e.getMessage());
        }
        catch (NoSuchElementException noSuchElementException){
            System.out.println("[GENIUSAPI " + getTime() + " ] Error: NoSuchElementException for " + songName + " at " + event.getGuild().getName());
            InputStream is = getClass().getClassLoader().getResourceAsStream("images/not-found.png");
            if (is != null) {
                event.getHook().sendMessage(":warning: No lyrics found for " + songName)
                        .addFile(is, "not-found.png").queue();
            }
        }
    }

    // Get Member's Spotify Current Playing Track
    private String getSpotifyTrack(Member member){
        var spotify = member.getActivities().stream()
                .filter(activity -> activity.getName().equals("Spotify"))
                .findFirst()
                .orElse(null);
        if (spotify != null){
            var trackName = Objects.requireNonNull(spotify.asRichPresence()).getDetails();
            var trackArtist = Objects.requireNonNull(spotify.asRichPresence()).getState();
            return trackName + " " + trackArtist;
        }
        return null;
    }

    // Gets the current time
    private String getTime(){
        var myDateObj = LocalDateTime.now();
        var myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }
}
