package io.bootify.visitor_app.service;

import io.bootify.visitor_app.domain.Address;
import io.bootify.visitor_app.domain.Flat;
import io.bootify.visitor_app.domain.Role;
import io.bootify.visitor_app.domain.User;
import io.bootify.visitor_app.model.AddressDTO;
import io.bootify.visitor_app.model.CreateUserDTO;
import io.bootify.visitor_app.model.RoleDTO;
import io.bootify.visitor_app.model.UserDTO;
import io.bootify.visitor_app.repos.AddressRepository;
import io.bootify.visitor_app.repos.FlatRepository;
import io.bootify.visitor_app.repos.RoleRepository;
import io.bootify.visitor_app.repos.UserRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminPanelService {

    private static Logger LOGGER= LoggerFactory.getLogger(AdminPanelService.class);

    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private FlatService flatService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private RoleRepository roleRepository;

    public Long createUser(final CreateUserDTO createUserDTO){
        LOGGER.info("Creating user: {}", createUserDTO);
        AddressDTO addressDTO= new AddressDTO();
        addressDTO.setLine1(createUserDTO.getLine1());
        addressDTO.setLine2(createUserDTO.getLine2());
        addressDTO.setPincode(createUserDTO.getPincode());
        addressDTO.setCity(createUserDTO.getCity());
        addressDTO.setState(createUserDTO.getState());
        Long addressId= addressService.create(addressDTO);

        UserDTO userDTO= new UserDTO();
        userDTO.setName(createUserDTO.getName());
        userDTO.setPhone(createUserDTO.getPhone());
        userDTO.setEmail(createUserDTO.getEmail());
        userDTO.setAddressId(addressId);
        userDTO.setFlatId(createUserDTO.getFlatId());
        userDTO.setRoleId(createUserDTO.getRoleId());

        return userService.create(userDTO);
    }

    @Transactional
    public void updateUser(final Long id, final CreateUserDTO createUserDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final Address address= user.getAddress();

        //Address Update
        address.setLine1(createUserDTO.getLine1());
        address.setLine2(createUserDTO.getLine2());
        address.setPincode(createUserDTO.getPincode());
        address.setCity(createUserDTO.getCity());
        address.setState(createUserDTO.getState());
        AddressDTO addressDTO= addressService.mapToDTO(address, new AddressDTO());
        addressService.update(address.getId(), addressDTO);

        //Update user
        user.setName(createUserDTO.getName());
        user.setEmail(createUserDTO.getEmail());
        user.setPhone(createUserDTO.getPhone());

        user.updateRole(roleRepository.findById(createUserDTO.getRoleId()));
        if(createUserDTO.getFlatId()!=null){
            user.updateFlat(flatRepository.findById(createUserDTO.getFlatId()));
        }
        user.setAddress(address);
        UserDTO userDTO= userService.mapToDTO(user, new UserDTO());
        userService.update(id, userDTO);
    }
}
