package concerttours.jobs;

import concerttours.service.VenueService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;

public class UpdateVenuesJob extends AbstractJobPerformable<CronJobModel> {

    private final static Logger LOGGER = Logger.getLogger(UpdateVenuesJob.class);
    private VenueService venueService;


    @Override
    public PerformResult perform(CronJobModel cronJobModel) {
        LOGGER.info("updating venues");
        try {
            venueService.updateVenues();
            LOGGER.info("updating finished successfully");
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        } catch (IOException e) {
            LOGGER.error("unable to update venues");
            e.printStackTrace();
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
        }
    }

    @Required
    public void setVenueService(VenueService venueService) {
        this.venueService = venueService;
    }
}
