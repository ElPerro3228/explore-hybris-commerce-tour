package concerttours.daos.impl;

import concerttours.daos.VenueDAO;
import concerttours.model.VenueModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "venueDAO")
public class DefaultVenueDAO implements VenueDAO {

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Override
    public List<VenueModel> findVenues() {
        final String queryString = "SELECT {v:" + VenueModel.PK + "} " +
                "FROM {" + VenueModel._TYPECODE + " AS v}";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        return flexibleSearchService.<VenueModel>search(query).getResult();
    }

    @Override
    public List<VenueModel> findVenueByCode(String code) {
        final String queryString = "SELECT {v:" + VenueModel.PK + "} " +
                "FROM {" + VenueModel._TYPECODE + " AS v} " +
                "WHERE {v:" + VenueModel.CODE + "}=?code";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.addQueryParameter("code", code);
        return flexibleSearchService.<VenueModel>search(query).getResult();
    }
}
