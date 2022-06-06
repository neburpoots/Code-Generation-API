package io.swagger.controller;

import org.apache.tomcat.jni.Local;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebAppConfiguration
@SpringBootTest
public class TransactionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMVC;

    @BeforeEach
    private void setup() throws Exception{
        this.mockMVC = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void createUserTest() throws Exception {
    //When provided a
        JSONObject payload = new JSONObject();
        payload.put("toAccount", "NL01INHO0000000004");
        payload.put("fromAccount", "NL01INHO0000000005");
        payload.put("amount", 20.10);
        payload.put("type", 2);

        mockMVC.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content(payload.toString())).andExpect(status().isCreated()).andReturn();
    }

    @Test
    public void createUserTestFails() throws Exception {
        //When provided a
        JSONObject payload = new JSONObject();
        payload.put("toAccount", "NL01INHO0000000004");
        payload.put("fromAccount", "NL01INHO0000000005");
        payload.put("amount", -20.10);
        payload.put("type", 2);

        mockMVC.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content(payload.toString())).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void getTransactions() throws Exception {

        ArrayList p = new ArrayList();
        LocalDate date = LocalDate.now();
        p.add("?from_iban=NL01INHO0000000004");
        p.add("&to_iban=NL01INHO0000000005");
        p.add("&date=" + date.getDayOfMonth()+ "-" + date.getMonthValue() + "-" + date.getYear());
        System.out.println(p.get(2));

        mockMVC.perform(get("/api/transactions/" + p.get(0) + p.get(1)))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getTransactionsFails() throws Exception {

        ArrayList p = new ArrayList();
        LocalDate date = LocalDate.now();
        p.add("?from_iban=falseIban");
        p.add("&to_iban=NL01INHO0000000005");
        p.add("&date=" + date.getDayOfMonth()+ "-" + date.getMonthValue() + "-" + date.getYear());
        System.out.println(p.get(2));

        mockMVC.perform(get("/api/transactions/" + p.get(0) + p.get(1)))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void testGetTransactionsById() throws Exception {
        String id = "NL01INHO0000000004";

        mockMVC.perform(get("/api/transactions/" + id))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void testGetTransactionsByIdFails() throws Exception {
        String id = "NL01INHO00";

        mockMVC.perform(get("/api/transactions/" + id))
                .andExpect(status().isBadRequest()).andReturn();
    }
}