package com.oyungar.geniuslyrics;

import core.GLA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.InputStream;

public class GeniusLyricsListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        if (event.getName().equals("lyrics")){
            var geniusApi = new GLA();
            var songName = event.getOption("song").getAsString();
            try {
                event.deferReply().queue();
                var searchResult = geniusApi.search(songName).getHits().getFirst();
                if (searchResult == null || searchResult.getUrl().isEmpty()){return;}
                if (!searchResult.getUrl().isEmpty()){
                    if (searchResult.fetchLyrics().toCharArray().length > 4096) {
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
                        var embedContent = EmbedTemplate.embedBuilder(searchResult, event.getUser());
                        event.getHook().sendMessageEmbeds(embedContent.build()).queue();
                    }
                }
            } catch (IOException e) {
                System.out.println("[GENIUSAPI] Error: " + e.getMessage());
            }
        }
    }
}
