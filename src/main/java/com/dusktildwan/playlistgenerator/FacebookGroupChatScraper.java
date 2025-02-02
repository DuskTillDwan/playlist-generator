package com.dusktildwan.playlistgenerator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@Getter
@Setter
public class FacebookGroupChatScraper {

    // Method that simulates scraping and parsing the messages using mock data
    public List<String> scrapeAndExtractLinks() {
        List<String> extractedLinks = new ArrayList<>();

        // Use the mock data from the constants class
        for (String message : MockDataConstants.SAMPLE_MESSAGES) {
            extractedLinks.addAll(MessageExtractor.extractLinks(message));
        }

        return extractedLinks;
    }
}

