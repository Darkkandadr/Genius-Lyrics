package com.oyungar.geniuslyrics;

import core.GLA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

public class GeniusLyricsListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("lyrics")){
            var geniusApi = new GLA();
            var songName = event.getOption("song").getAsString();
            try {
                event.deferReply().queue();
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
                        }
                    }
                    else{
                        // If the lyrics are not too long, then send them
                        var embedContent = EmbedTemplate.embedBuilder(searchResult, event.getUser());
                        event.getHook().sendMessageEmbeds(embedContent.build()).queue();
                    }
                }
            }
            catch (IOException e){
                System.out.println("[GENIUSAPI] Error: " + e.getMessage());
            }
            catch (NoSuchElementException noSuchElementException){
                System.out.println("[GENIUSAPI] Error: NoSuchElementException for " + songName);
                InputStream is = getClass().getClassLoader().getResourceAsStream("images/not-found.png");
                if (is != null) {
                    event.getHook().sendMessage(":warning: No lyrics found for " + songName)
                            .addFile(is, "not-found.png").queue();
                }
            }
        }
    }
}
