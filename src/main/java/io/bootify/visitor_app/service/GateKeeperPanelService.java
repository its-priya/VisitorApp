package io.bootify.visitor_app.service;

import io.bootify.visitor_app.model.AddressDTO;
import io.bootify.visitor_app.model.CreateVisitorDTO;
import io.bootify.visitor_app.model.VisitorDTO;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateKeeperPanelService {

    private static Logger LOGGER= LoggerFactory.getLogger(GateKeeperPanelService.class);

    @Autowired
    private AddressService addressService;

    @Autowired
    private VisitorService visitorService;

    public Long createVisitor(final CreateVisitorDTO createVisitorDTO){
        LOGGER.info("Creating visitor: {}", createVisitorDTO);
        AddressDTO addressDTO= new AddressDTO();
        addressDTO.setLine1(createVisitorDTO.getLine1());
        addressDTO.setLine2(createVisitorDTO.getLine2());
        addressDTO.setPincode(createVisitorDTO.getPincode());
        addressDTO.setCity(createVisitorDTO.getCity());
        addressDTO.setState(createVisitorDTO.getState());
        Long addressId= addressService.create(addressDTO);

        VisitorDTO visitorDTO= new VisitorDTO();
        visitorDTO.setName(createVisitorDTO.getName());
        visitorDTO.setPhone(createVisitorDTO.getPhone());
        visitorDTO.setEmail(createVisitorDTO.getEmail());
        visitorDTO.setAddressId(addressId);

        return visitorService.create(visitorDTO);
    }

}
