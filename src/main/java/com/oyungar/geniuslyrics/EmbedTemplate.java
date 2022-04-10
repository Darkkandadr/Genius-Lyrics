package com.oyungar.geniuslyrics;

import genius.SongSearch;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class EmbedTemplate {

    public static EmbedBuilder embedBuilder(SongSearch.Hit song, User requestor) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Lyrics of " + song.getTitle() + " by " + song.getArtist().getName());
        embedBuilder.setAuthor("Genius Lyrics", song.getUrl(), "https://assets.genius.com/images/apple-touch-icon.png");
        embedBuilder.setThumbnail(song.getThumbnailUrl());
        embedBuilder.setColor(Color.yellow);
        var lyrics = song.fetchLyrics();
        embedBuilder.setDescription(lyrics);
        embedBuilder.setFooter("Requested by " + requestor.getName(), requestor.getAvatarUrl());
        return embedBuilder;
    }

    /* lyrics.replace("[Köprü]", "**[Köprü]**");
        lyrics.replace("[Verse]", "**[Verse]**");
        lyrics.replace("[Giriş]", "**[Giriş]**");
        lyrics.replace("[Verse 1]", "**[Verse 1]**");
        lyrics.replace("[Verse 2]", "**[Verse 2]**");
        lyrics.replace("[Verse 3]", "**[Verse 3]**");
        lyrics.replace("[Çıkış]", "**[Çıkış]**");
        lyrics.replace("[Nakarat]", "**[Nakarat]**");
        lyrics.replace("[Bridge]", "**[Bridge]**");
        lyrics.replace("[Outro]", "**[Outro]**");
        lyrics.replace("[Chorus]", "**[Chorus]**");
        lyrics.replace("[Intro]", "**[Intro]**");
        lyrics.replace("[Verse]", "**[Verse]**");
        lyrics.replace("[Chorus 1]", "**[Chorus 1]**");
        lyrics.replace("[Chorus 2]", "**[Chorus 2]**");
        lyrics.replace("[Chorus 3]", "**[Chorus 3]**");
        lyrics.replace("[Hook]", "**[Hook]**");
        lyrics.replace("[Instrumental]", "**[Instrumental]**");
        lyrics.replace("[Interlude]", "**[Interlude]**");
        lyrics.replace("[Part 1]", "**[Part 1]**");
        lyrics.replace("[Part 2]", "**[Part 2]**");
        lyrics.replace("[Part 3]", "**[Part 3]**"); */
}
