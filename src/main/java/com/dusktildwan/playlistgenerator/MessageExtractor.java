package com.dusktildwan.playlistgenerator;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageExtractor {
    // Regular expression pattern to detect Messenger redirect URLs
    // private static final Pattern MESSENGER_LINK_PATTERN = Pattern.compile("https://l\\.messenger\\.com/l\\.php\\?u=([^&]+)");

    public static List<String> extractLinks(String text) {
        List<String> links = new ArrayList<>();
        Pattern pattern = Pattern.compile("(https?://[\\w./?v=-]+)");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            links.add(matcher.group());
        }

        return links;
    }

    //TODO: Beginning of messenger embedded links. This is what happens when you click the link. Pretty sure i don't need this.
//    public static List<String> extractLinks(String text) {
//
//        List<String> links = new ArrayList<>();
//        Pattern pattern = Pattern.compile("(https?://[\\w./?v=-]+)");
//        Matcher matcher = pattern.matcher(text);
//
//        while (matcher.find()) {
//            String link = matcher.group();
//            Matcher messengerMatcher = MESSENGER_LINK_PATTERN.matcher(link);
//            if (messengerMatcher.find()) {
//                // Extract the actual URL from the Messenger link
//                String decodedUrl = URLDecoder.decode(messengerMatcher.group(1), java.nio.charset.StandardCharsets.UTF_8);
//                links.add(decodedUrl);
//            } else {
//                // Add the link as is if it's not a Messenger redirect
//                links.add(link);
//            }
//        }
//
//        return links;
//    }
}
