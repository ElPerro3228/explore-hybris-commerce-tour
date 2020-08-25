package concerttours.daos.impl;

import concerttours.daos.VenueDAO;
import concerttours.model.VenueModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@IntegrationTest
public class DefaultVenueDAOIntegrationTest extends ServicelayerTransactionalTest {

    @Resource
    private VenueDAO venueDAO;
    @Resource
    private ModelService modelService;

    private static final String VENUE_CODE = "VENUE-11";
    private static final String VENUE_NAME = "TEST VENUE";
    private static final String VENUE_LOCATION = "Minsk, Belarus";
    private static final String VENUE_DESCRIPTION = "test venue";

    @Before
    public void setUp() { }

    @Test
    public void testFindVenues() {
        List<VenueModel> allVenues = venueDAO.findVenues();
        final int size = allVenues.size();
        final VenueModel venueModel = modelService.create(VenueModel.class);
        venueModel.setCode(VENUE_CODE);
        venueModel.setName(VENUE_NAME);
        venueModel.setLocation(VENUE_LOCATION);
        venueModel.setDescription(VENUE_DESCRIPTION);
        modelService.save(venueModel);
        allVenues = venueDAO.findVenues();
        assertEquals(size + 1, allVenues.size());
        assertTrue("venue not found", allVenues.contains(venueModel));
    }

    @Test
    public void testFindVenueByCode() {
        final VenueModel venueModel = modelService.create(VenueModel.class);
        venueModel.setCode(VENUE_CODE);
        venueModel.setName(VENUE_NAME);
        venueModel.setLocation(VENUE_LOCATION);
        venueModel.setDescription(VENUE_DESCRIPTION);
        modelService.save(venueModel);
        List<VenueModel> venueByCode = venueDAO.findVenueByCode(VENUE_CODE);
        assertEquals("Did not find the Venue which was just saved", 1, venueByCode.size());
        assertEquals("Retrieved Venue's code attribute incorrect", VENUE_CODE, venueByCode.get(0).getCode());
        assertEquals("Retrieved Venue's name attribute incorrect", VENUE_NAME, venueByCode.get(0).getName());
        assertEquals("Retrieved Venue's location attribute incorrect", VENUE_LOCATION, venueByCode.get(0).getLocation());
        assertEquals("Retrieved Venue's description attribute incorrect", VENUE_DESCRIPTION, venueByCode.get(0).getDescription());
    }
    @Test
    public void shouldReturnEmptyListWhenEmptyStringParamWasGiven() {
        final List<VenueModel> venueModels = venueDAO.findVenueByCode("");
        assertTrue("No venue should be returned", venueModels.isEmpty());
    }
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenStringParamIsNull() {
        venueDAO.findVenueByCode(null);
    }
}
