package br.com.devmeeting.networks;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import br.com.devmeeting.exceptions.FetchRSSException;
import br.com.devmeeting.models.Event;

public class RSSParser extends DefaultHandler {

    private static final String RSS_ITEM_TAG = "item";
    private static final String RSS_TITLE_TAG = "title";
    private static final String RSS_LINK_TAG = "link";
    private static final String RSS_DESCRIPTION_TAG = "description";
    private static final String RSS_DATE_FORMAT = "yyyy-MM-dd";

    private static final int NUM_EXTRA_DATA = 2;
    private static final String EXTRA_DATA_SEPARATOR = ";";

    private URL url;
    private StringBuilder content;
    private Event event;
    private List<Event> eventList = new ArrayList<>();

    public RSSParser(URL url) {
        this.url = url;
        this.content = new StringBuilder();
    }

    public void parse() throws FetchRSSException {

        InputStream inputStream = null;

        try {

            inputStream = this.url.openConnection().getInputStream();
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(inputStream, this);

        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new FetchRSSException();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {}
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase(RSS_ITEM_TAG)) {
            this.event = new Event();
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase(RSS_ITEM_TAG)) {
            this.eventList.add(this.event);
            this.event = null;
        } else if (qName.equalsIgnoreCase(RSS_TITLE_TAG) && this.event != null) {
            this.event.setTitle(this.content.toString().trim());
        } else if (qName.equalsIgnoreCase(RSS_LINK_TAG) && this.event != null) {
            this.event.setWebsite(this.content.toString().trim());
        } else if (qName.equalsIgnoreCase(RSS_DESCRIPTION_TAG) && this.event != null) {
            String[] description = this.content.toString().split(EXTRA_DATA_SEPARATOR);
            if (description.length == NUM_EXTRA_DATA) {
                try {
                    Date dtEvent = new SimpleDateFormat(RSS_DATE_FORMAT).parse(description[1].trim());
                    event.setDate(dtEvent);
                    event.setAddress(description[0]);
                } catch (ArrayIndexOutOfBoundsException | ParseException e) {
                    this.event = null;
                }
            }
        }

        this.content.setLength(0);
    }

    public void characters(char[] ch, int start, int length) {
        this.content.append(ch, start, length);
    }

    public List<Event> getEventList() {
        return eventList;
    }
}
