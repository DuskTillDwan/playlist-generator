package com.dusktildwan.playlistgenerator.scraper;

import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
public class MessengerLinkParser {

    public static void parseUrl(String[] args) {
        String messengerLink = "https://l.messenger.com/l.php?u=https%3A%2F%2Fopen.spotify.com%2Ftrack%2F6pTtDlxT35UY4qi8yzrJFM%3Fsi%3Ddab0bad787124c34&h=AT0V7TsE-iqaaqscrGA0wrlr_JL7exwytUEH8YNTddXE81OasgCcTDgVfxc4qgNFdhrBnCTcW-RIl1nVsdqFtkCfBB42xSqjgdJdjR_KrY8whR6ixwZPhIQUcJkCmHW3T0e91g";

        // Extract the "u" parameter
        String urlParameter = extractUrlFromMessengerLink(messengerLink);

        if (urlParameter != null) {
            decodeUrl(urlParameter);
        } else {
            System.out.println("URL parameter not found.");
        }
    }

    static String decodeUrl(String urlParameter) {
        // Decode the URL
        String decodedUrl = URLDecoder.decode(urlParameter, StandardCharsets.UTF_8);
        System.out.println("Decoded URL: " + decodedUrl);
        return decodedUrl;
    }


    static String extractUrlFromMessengerLink(String messengerLink) {
        // Find the position of the 'u=' parameter
        int startIndex = messengerLink.indexOf("u=");
        if (startIndex == -1) {
            return null; // No 'u' parameter found
        }
        // Extract the URL from the 'u=' parameter
        String url = messengerLink.substring(startIndex + 2); // Skip the 'u=' part
        // Get rid of anything after '&' (in case there are additional query parameters)
        int endIndex = url.indexOf("&");
        if (endIndex != -1) {
            url = url.substring(0, endIndex);
        }
        return url;
    }
}
