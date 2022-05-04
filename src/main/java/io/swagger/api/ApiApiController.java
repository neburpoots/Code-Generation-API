package io.swagger.api;

import io.swagger.model.AccountDTO;
import io.swagger.model.AccountDTOPOST;
import io.swagger.model.AccountPutDTO;
import java.math.BigDecimal;
import io.swagger.model.InlineResponse200;
import io.swagger.model.InlineResponse2001;
import io.swagger.model.InlineResponse2002;
import io.swagger.model.InlineResponse2003;
import io.swagger.model.InlineResponse2004;
import io.swagger.model.InlineResponse2005;
import io.swagger.model.InlineResponse2006;
import io.swagger.model.InlineResponse2007;
import io.swagger.model.InlineResponse201;
import io.swagger.model.InlineResponse2011;
import io.swagger.model.InlineResponse400;
import io.swagger.model.InlineResponse401;
import io.swagger.model.InlineResponse403;
import io.swagger.model.InlineResponse404;
import io.swagger.model.InlineResponse500;
import io.swagger.model.TransactionDTO;
import io.swagger.model.UserAccountDTO;
import io.swagger.model.UserDTOPOST;
import io.swagger.model.UserLoginDTO;
import io.swagger.model.UserPasswordDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T19:02:55.812Z[GMT]")
@RestController
public class ApiApiController implements ApiApi {

    private static final Logger log = LoggerFactory.getLogger(ApiApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ApiApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<InlineResponse201> addUser(@Parameter(in = ParameterIn.DEFAULT, description = "Example of user item to add", required=true, schema=@Schema()) @Valid @RequestBody UserDTOPOST body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse201>(objectMapper.readValue("{\n  \"description\" : \"Successfully created user\",\n  \"user\" : {\n    \"firstname\" : \"Kiana\",\n    \"roles\" : [ {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    }, {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    } ],\n    \"transaction_limit\" : 100,\n    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n    \"email\" : \"Kiana.Padilla@gmail.com\",\n    \"daily_limit\" : 25000,\n    \"lastname\" : \"Padilla\"\n  }\n}", InlineResponse201.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse201>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse201>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse2003> changeUserPassword(@Parameter(in = ParameterIn.HEADER, description = "" ,required=true,schema=@Schema()) @RequestHeader(value="Jwt token", required=true) String jwtToken,@Parameter(in = ParameterIn.DEFAULT, description = "Example of user login information to provide", required=true, schema=@Schema()) @Valid @RequestBody UserPasswordDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse2003>(objectMapper.readValue("{\n  \"description\" : \"Successfully changed the password\"\n}", InlineResponse2003.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse2003>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse2003>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse2011> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "Example of account information to provide.", required=true, schema=@Schema()) @Valid @RequestBody AccountDTOPOST body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse2011>(objectMapper.readValue("{\n  \"description\" : \"Account successfully created\",\n  \"account\" : {\n    \"account_type\" : \"Primary\",\n    \"balance\" : 200000,\n    \"iban\" : \"NLINHO5831335380\",\n    \"absolute_limit\" : -1000,\n    \"user\" : {\n      \"firstname\" : \"Kiana\",\n      \"roles\" : [ {\n        \"name\" : \"Employee\",\n        \"id\" : 1\n      }, {\n        \"name\" : \"Employee\",\n        \"id\" : 1\n      } ],\n      \"transaction_limit\" : 100,\n      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n      \"email\" : \"Kiana.Padilla@gmail.com\",\n      \"daily_limit\" : 25000,\n      \"lastname\" : \"Padilla\"\n    },\n    \"status\" : true\n  }\n}", InlineResponse2011.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse2011>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse2011>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse2005> editAccount(@Parameter(in = ParameterIn.PATH, description = "iban of the account you want to update", required=true, schema=@Schema()) @PathVariable("iban") String iban,@Parameter(in = ParameterIn.DEFAULT, description = "Example of account item to edit", required=true, schema=@Schema()) @Valid @RequestBody AccountPutDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse2005>(objectMapper.readValue("{\n  \"description\" : \"Account successfully edited\",\n  \"account\" : {\n    \"account_type\" : \"Primary\",\n    \"balance\" : 200000,\n    \"iban\" : \"NLINHO5831335380\",\n    \"absolute_limit\" : -1000,\n    \"user\" : {\n      \"firstname\" : \"Kiana\",\n      \"roles\" : [ {\n        \"name\" : \"Employee\",\n        \"id\" : 1\n      }, {\n        \"name\" : \"Employee\",\n        \"id\" : 1\n      } ],\n      \"transaction_limit\" : 100,\n      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n      \"email\" : \"Kiana.Padilla@gmail.com\",\n      \"daily_limit\" : 25000,\n      \"lastname\" : \"Padilla\"\n    },\n    \"status\" : true\n  }\n}", InlineResponse2005.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse2005>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse2005>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse2001> editUserAccount(@Parameter(in = ParameterIn.HEADER, description = "" ,required=true,schema=@Schema()) @RequestHeader(value="Jwt token", required=true) String jwtToken,@Parameter(in = ParameterIn.PATH, description = "id of the user you want to get.", required=true, schema=@Schema()) @PathVariable("id") Integer id,@Parameter(in = ParameterIn.DEFAULT, description = "Example of user login information to provide", required=true, schema=@Schema()) @Valid @RequestBody UserAccountDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse2001>(objectMapper.readValue("{\n  \"description\" : \"Successfully edited user\",\n  \"user\" : {\n    \"firstname\" : \"Kiana\",\n    \"roles\" : [ {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    }, {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    } ],\n    \"transaction_limit\" : 100,\n    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n    \"email\" : \"Kiana.Padilla@gmail.com\",\n    \"daily_limit\" : 25000,\n    \"lastname\" : \"Padilla\"\n  }\n}", InlineResponse2001.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse2001>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse2001>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse2004> getAccountById(@Parameter(in = ParameterIn.PATH, description = "iban of the account you want to get", required=true, schema=@Schema()) @PathVariable("iban") String iban) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse2004>(objectMapper.readValue("{\n  \"description\" : \"Successfully retrieved account\",\n  \"account\" : {\n    \"account_type\" : \"Primary\",\n    \"balance\" : 200000,\n    \"iban\" : \"NLINHO5831335380\",\n    \"absolute_limit\" : -1000,\n    \"user\" : {\n      \"firstname\" : \"Kiana\",\n      \"roles\" : [ {\n        \"name\" : \"Employee\",\n        \"id\" : 1\n      }, {\n        \"name\" : \"Employee\",\n        \"id\" : 1\n      } ],\n      \"transaction_limit\" : 100,\n      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n      \"email\" : \"Kiana.Padilla@gmail.com\",\n      \"daily_limit\" : 25000,\n      \"lastname\" : \"Padilla\"\n    },\n    \"status\" : true\n  }\n}", InlineResponse2004.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse2004>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse2004>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<AccountDTO>> getAllAccounts(@Parameter(in = ParameterIn.QUERY, description = "this query will get both accounts that belong to matching user id." ,schema=@Schema()) @Valid @RequestParam(value = "user_id", required = false) String userId,@Parameter(in = ParameterIn.QUERY, description = "this query will filter either 'primary' or 'savings'." ,schema=@Schema()) @Valid @RequestParam(value = "type", required = false) String type) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<AccountDTO>>(objectMapper.readValue("[ {\n  \"account_type\" : \"Primary\",\n  \"balance\" : 200000,\n  \"iban\" : \"NLINHO5831335380\",\n  \"absolute_limit\" : -1000,\n  \"user\" : {\n    \"firstname\" : \"Kiana\",\n    \"roles\" : [ {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    }, {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    } ],\n    \"transaction_limit\" : 100,\n    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n    \"email\" : \"Kiana.Padilla@gmail.com\",\n    \"daily_limit\" : 25000,\n    \"lastname\" : \"Padilla\"\n  },\n  \"status\" : true\n}, {\n  \"account_type\" : \"Primary\",\n  \"balance\" : 200000,\n  \"iban\" : \"NLINHO5831335380\",\n  \"absolute_limit\" : -1000,\n  \"user\" : {\n    \"firstname\" : \"Kiana\",\n    \"roles\" : [ {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    }, {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    } ],\n    \"transaction_limit\" : 100,\n    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n    \"email\" : \"Kiana.Padilla@gmail.com\",\n    \"daily_limit\" : 25000,\n    \"lastname\" : \"Padilla\"\n  },\n  \"status\" : true\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<AccountDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<AccountDTO>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse2006> getTransactionById(@Parameter(in = ParameterIn.PATH, description = "id of the transaction to return", required=true, schema=@Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse2006>(objectMapper.readValue("{\n  \"description\" : \"Account successfully created\",\n  \"transaction\" : {\n    \"amount\" : 0,\n    \"from_account\" : \"NLINHO2342353232\",\n    \"transaction_type\" : 3,\n    \"to_account\" : \"NLINHO5831335380\"\n  }\n}", InlineResponse2006.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse2006>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse2006>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse200> getUserById(@Parameter(in = ParameterIn.PATH, description = "id of the user you want to get.", required=true, schema=@Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse200>(objectMapper.readValue("{\n  \"description\" : \"Successfully executed request\",\n  \"user\" : {\n    \"firstname\" : \"Kiana\",\n    \"roles\" : [ {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    }, {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    } ],\n    \"transaction_limit\" : 100,\n    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n    \"email\" : \"Kiana.Padilla@gmail.com\",\n    \"daily_limit\" : 25000,\n    \"lastname\" : \"Padilla\"\n  }\n}", InlineResponse200.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse200>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse200>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Object>> getUsers(@NotNull @Parameter(in = ParameterIn.QUERY, description = "page number for pagination" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "page", required = true) Integer page,@Parameter(in = ParameterIn.QUERY, description = "this allows filtering based on name" ,schema=@Schema()) @Valid @RequestParam(value = "name", required = false) String name,@Parameter(in = ParameterIn.QUERY, description = "this allows filtering based on iban" ,schema=@Schema()) @Valid @RequestParam(value = "iban", required = false) String iban) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Object>>(objectMapper.readValue("[ \"\", \"\" ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Object>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse2002> loginUser(@Parameter(in = ParameterIn.DEFAULT, description = "Example of user login information to provide", required=true, schema=@Schema()) @Valid @RequestBody UserLoginDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse2002>(objectMapper.readValue("{\n  \"access_token\" : \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\",\n  \"description\" : \"Successfully logged in\",\n  \"user\" : {\n    \"firstname\" : \"Kiana\",\n    \"roles\" : [ {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    }, {\n      \"name\" : \"Employee\",\n      \"id\" : 1\n    } ],\n    \"transaction_limit\" : 100,\n    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n    \"email\" : \"Kiana.Padilla@gmail.com\",\n    \"daily_limit\" : 25000,\n    \"lastname\" : \"Padilla\"\n  }\n}", InlineResponse2002.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse2002>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse2002>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse2006> performTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Example of account information to provide.", required=true, schema=@Schema()) @Valid @RequestBody TransactionDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse2006>(objectMapper.readValue("{\n  \"description\" : \"Account successfully created\",\n  \"transaction\" : {\n    \"amount\" : 0,\n    \"from_account\" : \"NLINHO2342353232\",\n    \"transaction_type\" : 3,\n    \"to_account\" : \"NLINHO5831335380\"\n  }\n}", InlineResponse2006.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse2006>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse2006>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse2007> searchTransactions(@Parameter(in = ParameterIn.QUERY, description = "Allows to search on particular date" ,schema=@Schema()) @Valid @RequestParam(value = "date", required = false) String date,@Parameter(in = ParameterIn.QUERY, description = "Allows to search on particular user id" ,schema=@Schema()) @Valid @RequestParam(value = "user", required = false) Integer user,@Parameter(in = ParameterIn.QUERY, description = "Allows to search on all transactions for an account" ,schema=@Schema()) @Valid @RequestParam(value = "account", required = false) Integer account,@Parameter(in = ParameterIn.QUERY, description = "shows all transactions from the given iban." ,schema=@Schema()) @Valid @RequestParam(value = "from", required = false) String from,@Parameter(in = ParameterIn.QUERY, description = "returns all transactions that were sent to the given iban." ,schema=@Schema()) @Valid @RequestParam(value = "to", required = false) String to,@Parameter(in = ParameterIn.QUERY, description = "returns account where the balance matches given number." ,schema=@Schema()) @Valid @RequestParam(value = "as_eq", required = false) Integer asEq,@Parameter(in = ParameterIn.QUERY, description = "returns account where the balance is lower than given number." ,schema=@Schema()) @Valid @RequestParam(value = "as_lt", required = false) Integer asLt,@Parameter(in = ParameterIn.QUERY, description = "returns account where the balance is higher than given number." ,schema=@Schema()) @Valid @RequestParam(value = "as_mt", required = false) Integer asMt) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse2007>(objectMapper.readValue("{\n  \"data\" : [ \"\", \"\" ],\n  \"description\" : \"Retrieved all transactions\"\n}", InlineResponse2007.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse2007>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse2007>(HttpStatus.NOT_IMPLEMENTED);
    }

}
