package com.oyungar.geniuslyrics;

import core.GLA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class GeniusLyricsListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        if (event.getName().equals("lyrics")){
            var geniusApi = new GLA();
            var songName = event.getOption("song").getAsString();
            try {
                var searchResult = geniusApi.search(songName).getHits().getFirst();
                if (searchResult == null || searchResult.getUrl().isEmpty()){return;}
                var lyrics = searchResult.fetchLyrics();
                if (!lyrics.isEmpty()){
                    event.replyEmbeds(EmbedTemplate.embedBuilder(searchResult, event.getUser()).build()).queue();
                }
            } catch (IOException e) {
                System.out.println("[GENIUSAPI] Error: " + e.getMessage());
            }
        }
    }
}
