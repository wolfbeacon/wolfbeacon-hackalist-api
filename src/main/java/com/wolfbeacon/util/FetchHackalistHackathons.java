package com.wolfbeacon.util;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.wolfbeacon.model.HackalistHackathon;
import com.wolfbeacon.service.HackalistHackathonService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Component
@EnableScheduling
@Transactional
public class FetchHackalistHackathons {

    private static boolean haveStoredOlderMonths = false;

    @Autowired
    private HackalistHackathonService hackalistHackathonService;

    @Value("${google_server_api_key}")
    private String GOOGLE_API_SERVER_KEY;

    private MonthUtils monthUtils = new MonthUtils();
    private static String HACKALIST_API_ROUTE = "http://www.hackalist.org/api/1.0/";
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * Update every 9 hours, delay in milliseconds
     * Sweep the API endpoints from the start and update DB
     */
    @Async
    @Scheduled(initialDelay = 1000, fixedDelay = 43200000)
    public void updateHackalistHackathonData() {
        //Default Start dates of the hackalist API
        int currYear = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        int currMonth = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
        // if previous months data stored, refresh only current month
        if (!haveStoredOlderMonths) {
            currYear = 2015;
            currMonth = 1;
            haveStoredOlderMonths = true;
        }
        boolean res = true;
        while (res) {
            try {
                //Convert month name to month number
                String monthNumber = monthUtils.getStringMonthNumberFromIntegerMonthNumber(currMonth);

                //HTTP request to fetch JSON from Hackalist API
                HttpGet request = new HttpGet(HACKALIST_API_ROUTE + Integer.toString(currYear) + "/" + monthNumber + ".json");
                System.out.println(request);
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpResponse response = httpClient.execute(request);
                String entityContents = EntityUtils.toString(response.getEntity());
                JSONObject json = new JSONObject(entityContents);

                //Hackathons as entries in list, with list accessed by Month
                JSONArray hackathonsThisMonth = json.getJSONArray(monthUtils.getMonthNameFromMonthNumber(currMonth));
                for (int i = 0; i < hackathonsThisMonth.length(); i++) {

                    //Get current hackathon object in JSON List
                    JSONObject currHackathon = hackathonsThisMonth.getJSONObject(i);

                    //Parse and Set Values
                    String title = parseTitle(currHackathon);
                    Date startDate = parseStartDate(currHackathon);
                    Date endDate = parseEndDate(currHackathon);
                    String location = parseLocation(currHackathon);
                    String eventLink = parseEventLink(currHackathon);
                    String host = parseHost(currHackathon);
                    Integer length = parseLength(currHackathon);
                    String size = parseSize(currHackathon);
                    Boolean travel = parseTravel(currHackathon);
                    Boolean prize = parsePrize(currHackathon);
                    Boolean highSchoolers = parseHighSchoolers(currHackathon);
                    String cost = parseCost(currHackathon);
                    String facebookLink = parseFacebookLink(currHackathon);
                    String twitterLink = parseTwitterLink(currHackathon);
                    String googlePlusLink = parseGPlusLink(currHackathon);
                    String notes = parseNotes(currHackathon);

                    //Get year from JSON but don't add as a column
                    Integer year = parseYear(currHackathon);

                    //Extract Image URL from Facebook page
                    String imageLink = null;
                    if (facebookLink != null) {
                        imageLink = obtainEventImageFromFacebook(facebookLink);
                    }
                    if (imageLink == null) {
                        imageLink = obtainEventImageFromTwitter(twitterLink);
                    }
                    //Latitude and Longitude
                    Double[] coordinates = obtainLocationCoordinates(location);
                    Double latitude = coordinates[0];
                    Double longitude = coordinates[1];

                    Date lastUpdatedTime = fetchUpdatedTimestamp();

                    //Generate ID as year + month + day + |year| where + is concatenate
                    Long id = generateIDForHackathon(currHackathon, title, year);

                    //Create Object
                    HackalistHackathon thisHackalistHackathon = new HackalistHackathon(id, title, eventLink, startDate, endDate, lastUpdatedTime, year, location, host, length, size, travel, prize, highSchoolers, cost, facebookLink, twitterLink, googlePlusLink, imageLink, latitude, longitude, notes);

                    //UPDATE DB
                    hackalistHackathonService.saveHackathon(thisHackalistHackathon);
                }
                currMonth++;
                if (currMonth == 13) {
                    currMonth = 1;
                    currYear++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                res = false;
            }
        }
    }

    @NotNull
    private Long generateIDForHackathon(JSONObject currHackathon, String title, Integer year) {
        String startDateParts[] = currHackathon.getString("startDate").split(" ");
        String currStartDate = startDateParts[1];
        String currStartMonth = monthUtils.getMonthNumberFromMonthName(startDateParts[0]);
        String idStr = Integer.toString(year) + currStartMonth + currStartDate + Math.abs(title.hashCode());
        return Long.parseLong(idStr);
    }

    @NotNull
    private String parseTitle(JSONObject currHackathon) throws Exception {
        return currHackathon.getString("title");
    }

    @NotNull
    private Date parseStartDate(JSONObject currHackathon) throws Exception {
        String year = currHackathon.getString("year");
        String startDateParts[] = currHackathon.getString("startDate").split(" ");
        String currStartDate = startDateParts[1];
        String currStartMonth = monthUtils.getMonthNumberFromMonthName(startDateParts[0]);
        return dateFormat.parse(year + "-" + currStartMonth + "-" + currStartDate);
    }

    @NotNull
    private Date parseEndDate(JSONObject currHackathon) throws Exception {
        String year = currHackathon.getString("year");
        String endDateParts[] = currHackathon.getString("endDate").trim().split(" ");
        String currEndDate = endDateParts[1];
        String currEndMonth = monthUtils.getMonthNumberFromMonthName(endDateParts[0]);
        return dateFormat.parse(year + "-" + currEndMonth + "-" + currEndDate);
    }

    @NotNull
    private Integer parseYear(JSONObject currHackathon) throws Exception {
        return Integer.parseInt(currHackathon.getString("year"));
    }

    @NotNull
    private String parseLocation(JSONObject currHackathon) throws Exception {
        return currHackathon.getString("city");
    }

    private String parseHost(JSONObject currHackathon) {
        return currHackathon.getString("host");
    }


    private Integer parseLength(JSONObject currHackathon) {
        Integer length = null;
        if (currHackathon.has("length") && !currHackathon.getString("length").equals("unknown")) {
            String lengthStr = currHackathon.getString("length");
            length = 0;
            try {
                String lengthStrI = "";
                for (int j = 0; j < lengthStr.length(); j++) {
                    if (lengthStr.charAt(j) >= 48 && lengthStr.charAt(j) <= 57) {
                        lengthStrI += lengthStr.charAt(j);
                    }
                }
                length = !lengthStrI.equals("") ? Integer.parseInt(lengthStrI) : null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return length;
    }

    private String parseCost(JSONObject currHackathon) {
        String cost = null;
        if (currHackathon.has("cost")) {
            cost = currHackathon.getString("cost");
        }
        return cost;
    }

    private Boolean parseHighSchoolers(JSONObject currHackathon) {
        Boolean highSchoolers = null;
        if (currHackathon.has("highSchoolers")) {
            String highSchoolersStr = currHackathon.getString("highSchoolers");
            if (!highSchoolersStr.equals("unknown")) {
                highSchoolers = highSchoolersStr.equals("yes");
            }
        }
        return highSchoolers;
    }

    private Boolean parseTravel(JSONObject currHackathon) {
        Boolean travel = null;
        if (currHackathon.has("travel")) {
            String travelStr = currHackathon.getString("travel");
            if (!travelStr.equals("unknown")) {
                travel = travelStr.equals("yes");
            }
        }
        return travel;
    }

    private Boolean parsePrize(JSONObject currHackathon) {
        Boolean prize = null;
        if (currHackathon.has("prize")) {
            String prizeStr = currHackathon.getString("prize");
            if (!prizeStr.equals("unknown")) {
                prize = prizeStr.equals("yes");
            }
        }
        return prize;
    }

    private String parseSize(JSONObject currHackathon) {
        if (currHackathon.has("size")) {
            return currHackathon.getString("size");
        }
        return null;
    }


    private String parseEventLink(JSONObject currHackathon) {
        return currHackathon.getString("url");
    }

    private String parseFacebookLink(JSONObject currHackathon) {
        String facebookLink = null;
        if (currHackathon.has("facebookURL")) {
            facebookLink = currHackathon.getString("facebookURL");
        }
        return facebookLink;
    }

    private String parseTwitterLink(JSONObject currHackathon) {
        String twitterLink = null;
        if (currHackathon.has("twitterURL")) {
            twitterLink = currHackathon.getString("twitterURL");
        }
        return twitterLink;
    }

    private String parseGPlusLink(JSONObject currHackathon) {
        String googlePlusLink = null;
        if (currHackathon.has("googlePlusURL")) {
            googlePlusLink = currHackathon.getString("googlePlusURL");
        }
        return googlePlusLink;
    }


    private String parseNotes(JSONObject currHackathon) {
        String notes = null;
        if (currHackathon.has("notes")) {
            notes = currHackathon.getString("notes");
            if (notes.length() > 255) {
                notes = notes.substring(255);
            }
        }
        return notes;
    }

    private Date fetchUpdatedTimestamp() {
        return Calendar.getInstance().getTime();
    }

    //Obtain Event Coordinates through Google Maps Geocoding API
    private Double[] obtainLocationCoordinates(String location) {
        Double[] ans = {null, null};
        try {
            GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_API_SERVER_KEY);
            GeocodingResult[] results = GeocodingApi.geocode(context, location).await();
            LatLng latLng = results[0].geometry.location;
            ans[0] = latLng.lat;
            ans[1] = latLng.lng;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    //Get the Event thumbnail from the Facebook Graph API
    private String obtainEventImageFromFacebook(String facebookLink) {
        //currently not working with events, only pages
        try {
            if (facebookLink.contains("https://www.")) {
                facebookLink = facebookLink.replace("https://www.", "https://graph.");
            } else if (facebookLink.contains("https://facebook")) {
                facebookLink = facebookLink.replace("https://facebook", "https://graph.facebook");
            }
            String toAppend = "";
            if (facebookLink.charAt(facebookLink.length() - 1) != '/') {
                toAppend += "/";
            }
            toAppend += "picture?type=normal";
            //get redirected link
            facebookLink += toAppend;
            URLConnection con = new URL(facebookLink).openConnection();
            con.connect();
            con.getInputStream();
            facebookLink = con.getURL().toString();
            return facebookLink;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Accessible directly from Twitter
    private String obtainEventImageFromTwitter(String twitterLink) {
        try {
            //returns a redirected URL directly to the image
            if (twitterLink.charAt(twitterLink.length() - 1) != '/') {
                twitterLink += "/";
            }
            twitterLink = twitterLink + "profile_image?size=normal";
            //get redirected link
            URLConnection con = new URL(twitterLink).openConnection();
            con.connect();
            con.getInputStream();
            twitterLink = con.getURL().toString();
            return twitterLink;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
