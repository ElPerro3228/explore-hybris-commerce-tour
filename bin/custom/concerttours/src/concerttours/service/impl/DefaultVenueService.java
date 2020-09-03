package concerttours.service.impl;

import concerttours.model.VenueModel;
import concerttours.service.VenueService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.Model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class DefaultVenueService implements VenueService {

    public static final String APIKEY = "venue.service.apikey";
    public static final String API_URL = "https://api.songkick.com/api/3.0/search/venues.json?query=Minsk&apikey=%s&per_page=100";

    private ConfigurationService configurationService;
    private ModelService modelService;

    @Override
    public void updateVenues() throws IOException {
        String apikey = configurationService.getConfiguration().getString(APIKEY);
        String urlString = String.format(API_URL, apikey);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            updateVenues(connection);
        }
    }

    private void updateVenues(HttpURLConnection connection) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new InputStreamReader(connection.getInputStream())) {
            JSONArray venues = getVenuesArray(parser, reader);
            for (Object venueObj : venues) {
                VenueModel venueModel = createVenueModel((JSONObject) venueObj);
                modelService.save(venueModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private VenueModel createVenueModel(JSONObject venueObj) {
        JSONObject venue = venueObj;
        VenueModel venueModel = modelService.create(VenueModel.class);
        venueModel.setName(venue.get("displayName").toString(), Locale.ENGLISH);
        venueModel.setCode(venue.get("id").toString());
        venueModel.setDescription(venue.get("description").toString());
        JSONObject city = (JSONObject) venue.get("city");
        venueModel.setLocation(venue.get("zip").toString() + ", " + venue.get("street").toString() + ", " + city.get("displayName"));
        return venueModel;
    }

    private JSONArray getVenuesArray(JSONParser parser, Reader reader) throws IOException, ParseException {
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        JSONObject resultsPage = (JSONObject) jsonObject.get("resultsPage");
        JSONObject results = (JSONObject) resultsPage.get("results");
        return (JSONArray) results.get("venue");
    }

    @Required
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
    @Required
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
}
