package io.swagger.controller;

import io.swagger.service.AccountService;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest extends TestCase {

    //Class to be tested
    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountController accountController;

    //Dependencies


    @Before
    public void setup(){

    }


    @Test
    public void givenIbanDoesNotBelongToAnyAccount_then404IsReceived()
            throws NotFoundException {
//        when(accountService.getAccount()).thenReturn(List.of(new Guitar(new Brand("Fender"), "Jazz", 1500)));
//        this.mockMvc.perform(get("/guitars"))
//                .andDo(print()).andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].brand.name").value("Fender"));
    }

}