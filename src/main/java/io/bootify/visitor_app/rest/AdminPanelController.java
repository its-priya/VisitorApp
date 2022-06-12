package io.bootify.visitor_app.rest;

import io.bootify.visitor_app.model.CreateUserDTO;
import io.bootify.visitor_app.model.FlatDTO;
import io.bootify.visitor_app.model.RoleDTO;
import io.bootify.visitor_app.model.UserDTO;
import io.bootify.visitor_app.service.AdminPanelService;
import io.bootify.visitor_app.service.FlatService;
import io.bootify.visitor_app.service.RoleService;
import io.bootify.visitor_app.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@RequestMapping("/api/admin-panel")
public class AdminPanelController {
    static private Logger LOGGER = LoggerFactory.getLogger(AdminPanelController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AdminPanelService adminPanelService;

    @Autowired
    private FlatService flatService;

    @Autowired
    private RoleService roleService;

    //User
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/user")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUser(@RequestBody @Valid final CreateUserDTO createUserDTO) {
        return new ResponseEntity<>(adminPanelService.createUser(createUserDTO), HttpStatus.CREATED);
    }
    @GetMapping("/user")
    public ResponseEntity<List<UserDTO>> getUser() {
        return ResponseEntity.ok(userService.findAll());
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable final Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable final Long id,
                                           @RequestBody @Valid final CreateUserDTO userDTO) {
        adminPanelService.updateUser(id, userDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/csv-upload")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file")MultipartFile file){
        String message= "";
        try{
            BufferedReader fileReader= new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
            CSVParser csvParser= new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            Iterable<CSVRecord> csvRecords= csvParser.getRecords();
            for(CSVRecord record: csvRecords){
                CreateUserDTO userDTO= new CreateUserDTO();
                userDTO.setName(record.get("name"));
                userDTO.setEmail(record.get("email"));
                userDTO.setPhone(record.get("phone"));
                userDTO.setRoleId(Long.parseLong(record.get("roleId")));
                userDTO.setFlatId(Long.parseLong(record.get("flatId")));
                userDTO.setLine1(record.get("line1"));
                userDTO.setLine2(record.get("line2"));
                userDTO.setPincode(record.get("pincode"));
                userDTO.setCity(record.get("city"));
                userDTO.setState(record.get("state"));
                adminPanelService.createUser(userDTO);
                LOGGER.info("User created: {}", userDTO.getId() + " -> " + userDTO.getName());
            }
            message= "File Uploaded successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }catch(Exception e){
            LOGGER.error("Exception occurred: {}",e);
            message = "Upload failed: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @DeleteMapping("/user/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Flat
    @PostMapping("/flat")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createFlat(@RequestBody @Valid final FlatDTO flatDTO){
        return new ResponseEntity<>(flatService.create(flatDTO), HttpStatus.CREATED);
    }

    @GetMapping("/flat")
    public ResponseEntity<List<FlatDTO>> getAllFlats() {
        return ResponseEntity.ok(flatService.findAll());
    }

    @GetMapping("/flat/{id}")
    public ResponseEntity<FlatDTO> getFlat(@PathVariable final Long id) {
        return ResponseEntity.ok(flatService.get(id));
    }

    @PutMapping("/flat/{id}")
    public ResponseEntity<Void> updateFlat(@PathVariable final Long id,
                                           @RequestBody @Valid final FlatDTO flatDTO) {
        flatService.update(id, flatDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/flat/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteFlat(@PathVariable final Long id) {
        flatService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Role
    @GetMapping("/role")
    public ResponseEntity<List<RoleDTO>> getAllRole() {
        return ResponseEntity.ok(roleService.findAll());
    }
    @PostMapping("/role")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRole(@RequestBody @Valid final RoleDTO roleDTO) {
        return new ResponseEntity<>(roleService.create(roleDTO), HttpStatus.CREATED);
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable final Long id) {
        return ResponseEntity.ok(roleService.get(id));
    }

    @PutMapping("/role/{id}")
    public ResponseEntity<Void> updateRole(@PathVariable final Long id,
                                           @RequestBody @Valid final RoleDTO roleDTO) {
        roleService.update(id, roleDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/role/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRole(@PathVariable final Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
