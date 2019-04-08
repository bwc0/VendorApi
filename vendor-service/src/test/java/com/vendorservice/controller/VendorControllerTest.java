package com.vendorservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendorservice.controller.ExceptionHandler.RestExceptionHandler;
import com.vendorservice.exception.ResourceNotFoundException;
import com.vendorservice.model.Vendor;
import com.vendorservice.repository.VendorRepository;
import com.vendorservice.service.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

//import static com.vendorservice.controller.VendorController.VENDOR_URL;
import static java.util.Arrays.asList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VendorControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "TEST_VENDOR";
    private Vendor vendor;
    private MockMvc mockMvc;

    @Mock
    VendorRepository repository;

    @Mock
    VendorService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        VendorController controller = new VendorController(repository, service);

        vendor = new Vendor(NAME);
        vendor.setId(ID);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET / endpoint should return 200")
    void getAllVendors_ShouldReturn_200Test() throws Exception {

        mockMvc.perform(get( "/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(repository).findAll();
    }

    @Test
    @DisplayName("GET / endpoint should list all vendors test")
    void getAllVendors_ShouldReturn_ListTest() throws Exception {

        List<Vendor> data = asList(vendor, new Vendor("2nd Vendor name"));

        given(repository.findAll()).willReturn(data);

        mockMvc.perform(get("/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("GET /{id} endpoint should return vendor by id test")
    void getVendorById_ShouldReturn200Test() throws Exception {

        mockMvc.perform(get( "/1"))
                .andExpect(status().isOk());

        verify(service).getVendorById(anyLong());
    }

    @Test
    @DisplayName("GET /{id} endpoint should list vendor by id test")
    void getVendorById_ShouldReturnVendorTest() throws Exception {

        given(service.getVendorById(anyLong())).willReturn(vendor);

        mockMvc.perform(get("/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(vendor.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(vendor.getName())));
    }

    @Test
    @DisplayName("POST / endpoint should return 201 test")
    void createVendor_ShouldReturn201Test() throws Exception {

        given(repository.save(any())).willReturn(vendor);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vendor)))
                .andExpect(status().isCreated());

        verify(repository).save(any());
    }

    @Test
    @DisplayName("POST / endpoint should return Vendor test")
    void createVendor_ShouldReturnVendorTest() throws Exception {

        given(repository.save(any())).willReturn(vendor);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vendor)))
                .andExpect(jsonPath("$.id", equalTo(vendor.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(vendor.getName())));
    }

    @Test
    @DisplayName("PUT /{id} endpoint should return 200 test")
    void updateVendor_ShouldReturn200Test() throws Exception {

        given(service.updateVendor(anyLong(), any())).willReturn(vendor);

        mockMvc.perform(put( "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vendor)))
                .andExpect(status().isOk());

        verify(service).updateVendor(anyLong(), any());
    }

    @Test
    @DisplayName("PUT /{id} endpoint should return updated Vendor test")
    void updateVendor_ShouldReturnVendorTest() throws Exception {

        given(service.updateVendor(anyLong(), any())).willReturn(vendor);

        mockMvc.perform(put( "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vendor)))
                .andExpect(jsonPath("$.id", equalTo(vendor.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(vendor.getName())));
    }

    @Test
    @DisplayName("DELETE /{id} endpoint should delete Vendor test")
    void deleteVendor() throws Exception {
        mockMvc.perform(delete( "/1"))
                .andExpect(status().isOk());

        verify(repository).deleteById(anyLong());
    }

    @Test
    @DisplayName("GET /id endpoint test, should return not found test")
    void getVendorById_ShouldReturnNotFoundTest() throws Exception {
        given(service.getVendorById(anyLong())).willThrow(ResourceNotFoundException.class);

        mockMvc.perform(get( "/1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).getVendorById(anyLong());
    }
}