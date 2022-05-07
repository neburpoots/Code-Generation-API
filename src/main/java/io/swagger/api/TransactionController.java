package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.Account;
import io.swagger.model.AccountPatchDTO;
import io.swagger.model.AccountPostDTO;
import io.swagger.model.Error;
import io.swagger.model.Transaction;
import io.swagger.model.TransactionPostDTO;
import io.swagger.model.User;
import io.swagger.model.UserLoginDTO;
import io.swagger.model.UserPasswordDTO;
import io.swagger.model.UserPatchDTO;
import io.swagger.model.UserPostDTO;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")
@RestController
@Api(tags = {"transactions"})
public class TransactionController implements TransactionControllerInterface {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Transaction> addTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Created Transaction object", required=true, schema=@Schema()) @Valid @RequestBody TransactionPostDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Transaction>(objectMapper.readValue("{\n  \"amount\" : 250,\n  \"from_account\" : \"NL43INHO0348271748\",\n  \"type\" : {\n    \"name\" : \"Primary to savings\",\n    \"id\" : 2\n  },\n  \"to_account\" : \"NL41INHO0546284337\",\n  \"timestamp\" : \"2022-01-19 03:14:07\"\n}", Transaction.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Transaction>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Transaction>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Transaction> getTransactionById(@Parameter(in = ParameterIn.PATH, description = "Id of transaction", required=true, schema=@Schema()) @PathVariable("id") String id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Transaction>(objectMapper.readValue("{\n  \"amount\" : 250,\n  \"from_account\" : \"NL43INHO0348271748\",\n  \"type\" : {\n    \"name\" : \"Primary to savings\",\n    \"id\" : 2\n  },\n  \"to_account\" : \"NL41INHO0546284337\",\n  \"timestamp\" : \"2022-01-19 03:14:07\"\n}", Transaction.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Transaction>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Transaction>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Object>> getTransactions(@NotNull @Parameter(in = ParameterIn.QUERY, description = "Page number for pagination" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "page", required = true) Integer page,@Parameter(in = ParameterIn.QUERY, description = "Date value that needs to be considered for filter" ,schema=@Schema()) @Valid @RequestParam(value = "date", required = false) String date,@Parameter(in = ParameterIn.QUERY, description = "User value that needs to be considered for filter" ,schema=@Schema()) @Valid @RequestParam(value = "user_id", required = false) String userId,@Parameter(in = ParameterIn.QUERY, description = "From IBAN account that needs to be considered for filter" ,schema=@Schema()) @Valid @RequestParam(value = "from_iban", required = false) String fromIban,@Parameter(in = ParameterIn.QUERY, description = "To IBAN account that needs to be considered for filter" ,schema=@Schema()) @Valid @RequestParam(value = "to_iban", required = false) String toIban,@Parameter(in = ParameterIn.QUERY, description = "Equals given amount that needs to be considered for filter" ,schema=@Schema()) @Valid @RequestParam(value = "as_eq", required = false) String asEq,@Parameter(in = ParameterIn.QUERY, description = "Less than given amount that needs to be considered for filter" ,schema=@Schema()) @Valid @RequestParam(value = "as_lt", required = false) String asLt,@Parameter(in = ParameterIn.QUERY, description = "More than given amount that needs to be considered for filter" ,schema=@Schema()) @Valid @RequestParam(value = "as_mt", required = false) String asMt) {
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
}
