package io.swagger.service;

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
class TransactionServiceTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMVC;

    @BeforeEach
    private void setup() throws Exception{
        this.mockMVC = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    @Autowired
    private TransactionService service;




    @Test
    void getTransactionById() throws Exception{
        String id = "NL01INHO0000000004";

        mockMVC.perform(get("/api/transactions/" + id))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void createTransaction() throws Exception{
        JSONObject payload = new JSONObject();
        payload.put("toAccount", "NL01INHO0000000004");
        payload.put("fromAccount", "NL01INHO0000000005");
        payload.put("amount", 20.10);
        payload.put("type", 2);

        mockMVC.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content(payload.toString())).andExpect(status().isCreated()).andReturn();

    }

    @Test
    void filterTransactions() throws Exception {
        ArrayList p = new ArrayList();
        LocalDate date = LocalDate.now();
        p.add("?from_iban=NL01INHO0000000004");
        p.add("&to_iban=NL01INHO0000000005");
        p.add("&date=" + date.getDayOfMonth()+ "-" + date.getMonthValue() + "-" + date.getYear());
        System.out.println(p.get(2));

        mockMVC.perform(get("/api/transactions/" + p.get(0) + p.get(1)))
                .andExpect(status().isOk()).andReturn();
    }
}