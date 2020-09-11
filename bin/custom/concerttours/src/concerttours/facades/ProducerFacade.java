package concerttours.facades;

import concerttours.data.ProducerData;

import java.util.List;

public interface ProducerFacade {
    ProducerData getProducer(String code);
    List<ProducerData> getProducers();
}
