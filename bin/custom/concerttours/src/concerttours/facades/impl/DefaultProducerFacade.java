package concerttours.facades.impl;

import concerttours.data.ProducerData;
import concerttours.facades.ProducerFacade;
import concerttours.model.ProducerModel;
import concerttours.service.ProducerService;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class DefaultProducerFacade implements ProducerFacade {

    private ProducerService producerService;

    @Override
    public ProducerData getProducer(String code) {
        ProducerModel producer = producerService.getProducersByCode(code);
        return convertProducerModelTpProducerData(producer);
    }

    private ProducerData convertProducerModelTpProducerData(ProducerModel producer) {
        ProducerData producerData = new ProducerData();
        producerData.setName(producer.getName());
        producerData.setSurname(producer.getSurname());
        return producerData;
    }

    @Override
    public List<ProducerData> getProducers() {
        List<ProducerModel> producerModels = producerService.getProducers();
        List<ProducerData> producerData = new ArrayList<>();
        for (ProducerModel producerModel : producerModels) {
            producerData.add(convertProducerModelTpProducerData(producerModel));
        }
        return producerData;
    }

    @Required
    public void setProducerService(ProducerService producerService) {
        this.producerService = producerService;
    }
}
