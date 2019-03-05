package com.example.controller;

import com.example.model.Address;
import com.example.model.Province;
import com.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    private final String API = "/api/users";
    
    // Get
    
    @Test
    public void test01GetUser() throws Exception {
        this.mockMvc.perform( get(this.API + "/1") )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(1)) )
                .andExpect( jsonPath("$.firstName", is("Michael")) )
                .andExpect( jsonPath("$.lastName", is("Jordan")) )
                .andExpect( jsonPath("$.homeAddress.street", is("123 King Ave.")) )
                .andExpect( jsonPath("$.homeAddress.city", is("Vancouver")) )
                .andExpect( jsonPath("$.homeAddress.province", is("BC")) )
                .andExpect( jsonPath("$.homeAddress.postalCode", is("1A12B2")) )
                .andExpect( jsonPath("$.billingAddress.street", is("555 Best Rd.")) )
                .andExpect( jsonPath("$.billingAddress.city", is("Toronto")) )
                .andExpect( jsonPath("$.billingAddress.province", is("ON")) )
                .andExpect( jsonPath("$.billingAddress.postalCode", is("1M15H3")) )
                .andReturn();
    }
        
    @Test
    public void test02GetUserWithBillingAddressNull() throws Exception {
        this.mockMvc.perform( get(this.API + "/2") )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(2)) )
                .andExpect( jsonPath("$.firstName", is("Kobe")) )
                .andExpect( jsonPath("$.lastName", is("Bryant")) )
                .andExpect( jsonPath("$.homeAddress.street", is("2 LA St.")) )
                .andExpect( jsonPath("$.homeAddress.city", is("Burnaby")) )
                .andExpect( jsonPath("$.homeAddress.province", is("BC")) )
                .andExpect( jsonPath("$.homeAddress.postalCode", is("5L50P0")) )
                .andExpect( jsonPath("$.billingAddress", nullValue()) )
                .andReturn();
    }
    
    @Test
    public void test03GetUserWithWrongFirstName() throws Exception {
        this.mockMvc.perform( get(this.API + "/2") )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(2)) )
                .andExpect( jsonPath("$.firstName", not("Michael")) )
                .andExpect( jsonPath("$.lastName", is("Bryant")) )
                .andExpect( jsonPath("$.homeAddress.street", is("2 LA St.")) )
                .andExpect( jsonPath("$.homeAddress.city", is("Burnaby")) )
                .andExpect( jsonPath("$.homeAddress.province", is("BC")) )
                .andExpect( jsonPath("$.homeAddress.postalCode", is("5L50P0")) )
                .andExpect( jsonPath("$.billingAddress", nullValue()) )
                .andReturn();
    }
    
    @Test
    public void test04GetUserWithInvalidId() throws Exception {
        this.mockMvc.perform( get(this.API + "/abc") )
                .andExpect( status().isBadRequest() )
                .andReturn();
    }
    
    @Test
    public void test05GetUserNotFound() throws Exception {
        this.mockMvc.perform( get(this.API + "/100") )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$", is("Could not found User id 100")) )
                .andReturn();
    }    
    
    @Test
    public void test06GetUserHomeAddress() throws Exception {
        this.mockMvc.perform( get(this.API + "/1/home_address") )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.street", is("123 King Ave.")) )
                .andExpect( jsonPath("$.city", is("Vancouver")) )
                .andExpect( jsonPath("$.province", is("BC")) )
                .andExpect( jsonPath("$.postalCode", is("1A12B2")) )
                .andReturn();
    }
    
    @Test
    public void test07GetUserBillingAddress() throws Exception {
        this.mockMvc.perform( get(this.API + "/1/billing_address") )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.street", is("555 Best Rd.")) )
                .andExpect( jsonPath("$.city", is("Toronto")) )
                .andExpect( jsonPath("$.province", is("ON")) )
                .andExpect( jsonPath("$.postalCode", is("1M15H3")) )
                .andReturn();
    }
    
    @Test
    public void test08GetUserBillingAddressNull() throws Exception {
        this.mockMvc.perform( get(this.API + "/2/billing_address") )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.street", nullValue()) )
                .andExpect( jsonPath("$.city", nullValue()) )
                .andExpect( jsonPath("$.province", nullValue()) )
                .andExpect( jsonPath("$.postalCode", nullValue()) )
                .andReturn();
    }
    
    @Test
    public void test09GetAllUsers() throws Exception {
        this.mockMvc.perform( get(this.API) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$", hasSize(2)) )
                .andExpect( jsonPath("$[0].id", is(1)) )
                .andExpect( jsonPath("$[0].firstName", is("Michael")) )
                .andExpect( jsonPath("$[0].lastName", is("Jordan")) )
                .andExpect( jsonPath("$[0].homeAddress.street", is("123 King Ave.")) )
                .andExpect( jsonPath("$[0].homeAddress.city", is("Vancouver")) )
                .andExpect( jsonPath("$[0].homeAddress.province", is("BC")) )
                .andExpect( jsonPath("$[0].homeAddress.postalCode", is("1A12B2")) )
                .andExpect( jsonPath("$[0].billingAddress.street", is("555 Best Rd.")) )
                .andExpect( jsonPath("$[0].billingAddress.city", is("Toronto")) )
                .andExpect( jsonPath("$[0].billingAddress.province", is("ON")) )
                .andExpect( jsonPath("$[0].billingAddress.postalCode", is("1M15H3")) )
                .andExpect( jsonPath("$[1].firstName", not("Michael")) )
                .andExpect( jsonPath("$[1].lastName", is("Bryant")) )
                .andExpect( jsonPath("$[1].homeAddress.street", is("2 LA St.")) )
                .andExpect( jsonPath("$[1].homeAddress.city", is("Burnaby")) )
                .andExpect( jsonPath("$[1].homeAddress.province", is("BC")) )
                .andExpect( jsonPath("$[1].homeAddress.postalCode", is("5L50P0")) )
                .andExpect( jsonPath("$[1].billingAddress", nullValue()) )
                .andReturn();
    }
    
    // Post
    
    @Test
    public void test10AddUser() throws Exception {
        User user = new User("LeBron", "James");
        user.setHomeAddress( new Address("0 Kinsway St.", "West Vancouver", Province.BC, "1D35H5") );
        
        this.mockMvc.perform( post(this.API).contentType(MediaType.APPLICATION_JSON_UTF8).content(asJsonString(user)) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(3)) )
                .andExpect( jsonPath("$.firstName", is("LeBron")) )
                .andExpect( jsonPath("$.lastName", is("James")) )
                .andExpect( jsonPath("$.homeAddress.street", is("0 Kinsway St.")) )
                .andExpect( jsonPath("$.homeAddress.city", is("West Vancouver")) )
                .andExpect( jsonPath("$.homeAddress.province", is("BC")) )
                .andExpect( jsonPath("$.homeAddress.postalCode", is("1D35H5")) )
                .andExpect( jsonPath("$.billingAddress", nullValue()) )
                .andReturn();
    }
    
    @Test
    public void test11AddUserError() throws Exception {
        User user = new User("LeBron", "James");
        
        this.mockMvc.perform( post(this.API).contentType(MediaType.APPLICATION_JSON_UTF8).content(asJsonString(user)) )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("$", is("User could not be saved")) )
                .andReturn();
    }
    
    // Update
    
    @Test
    public void test12UpdateUser() throws Exception {
        User user = getUser( apiGet("/1").getResponse() );
        user.setFirstName("Michael Legend");
        user.getHomeAddress().setCity("East Vancouver");
        user.getBillingAddress().setProvince(Province.BC);
        
        this.mockMvc.perform( put(this.API + "/1").contentType(MediaType.APPLICATION_JSON_UTF8).content( asJsonString(user)) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(1)) )
                .andExpect( jsonPath("$.firstName", is("Michael Legend")) )
                .andExpect( jsonPath("$.lastName", is("Jordan")) )
                .andExpect( jsonPath("$.homeAddress.street", is("123 King Ave.")) )
                .andExpect( jsonPath("$.homeAddress.city", is("East Vancouver")) )
                .andExpect( jsonPath("$.homeAddress.province", is("BC")) )
                .andExpect( jsonPath("$.homeAddress.postalCode", is("1A12B2")) )
                .andExpect( jsonPath("$.billingAddress.street", is("555 Best Rd.")) )
                .andExpect( jsonPath("$.billingAddress.city", is("Toronto")) )
                .andExpect( jsonPath("$.billingAddress.province", is("BC")) )
                .andExpect( jsonPath("$.billingAddress.postalCode", is("1M15H3")) )
                .andReturn();
    }
    
    @Test
    public void test13UpdateUserWithInvalidId() throws Exception {
        User user = getUser( apiGet("/1").getResponse() );
        this.mockMvc.perform( put(this.API + "/abc").contentType(MediaType.APPLICATION_JSON_UTF8).content( asJsonString(user)) )
                .andExpect( status().isBadRequest() )
                .andReturn();
    }
    
    @Test
    public void test14UpdateUserError() throws Exception {
        User user = getUser( apiGet("/1").getResponse() );
        user.setFirstName("Michael Legend");
        user.setHomeAddress(null);
        
        this.mockMvc.perform( put(this.API + "/1").contentType(MediaType.APPLICATION_JSON_UTF8).content( asJsonString(user)) )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("$", is("User could not be updated")) )
                .andReturn();
    }
    
    @Test
    public void test15UpdateUserNotFound() throws Exception {
        User user = new User("test", "test");
        user.setHomeAddress( new Address("test", "test", Province.BC, "test") );
        
        this.mockMvc.perform( put(this.API + "/100").contentType(MediaType.APPLICATION_JSON_UTF8).content( asJsonString(user)) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$", is("Could not found User id 100")) )
                .andReturn();
    }
    
    // Delete
    
    @Test
    public void test16DeleteUser() throws Exception {
        this.mockMvc.perform( delete(this.API + "/1") )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$", is("Deleted a user with id 1")) )
                .andReturn();
    }
    
    @Test
    public void test17DeleteUserWithInvalidId() throws Exception {
        this.mockMvc.perform( delete(this.API + "/abc") )
                .andExpect( status().isBadRequest() )
                .andReturn();
    }
    
    @Test
    public void test18DeleteUserNotFound() throws Exception {
        this.mockMvc.perform( delete(this.API + "/100") )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$", is("Could not found User id 100")) )
                .andReturn();
    }
    
    @Test
    public void test19DeleteAllUsers() throws Exception {
        this.mockMvc.perform(delete(this.API))
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$", is("All users are deleted successfully")) )
                .andReturn();
    }
    
    
    // Helper methods
    
    public String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
    
    public MvcResult apiGet(String uri) throws Exception {
        return this.mockMvc.perform(get(this.API + uri))
            .andExpect(status().isOk())
            .andReturn();
    }
    
    public User getUser(MockHttpServletResponse res) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(res.getContentAsString(), User.class);
    }
    
}
