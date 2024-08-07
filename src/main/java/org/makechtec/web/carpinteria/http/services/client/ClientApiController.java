package org.makechtec.web.carpinteria.http.services.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.makechtec.software.json_tree.builders.ArrayObjectLeafBuilder;
import org.makechtec.software.json_tree.builders.ObjectLeafBuilder;
import org.makechtec.web.carpinteria.components.client.ClientDatabaseProxy;
import org.makechtec.web.carpinteria.components.client.ClientRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/client")
public class ClientApiController {

    private final ObjectMapper objectMapper;
    private final ClientDatabaseProxy clientDatabaseProxy;

    @Autowired
    public ClientApiController(ObjectMapper objectMapper, ClientDatabaseProxy clientDatabaseProxy) {
        this.objectMapper = objectMapper;
        this.clientDatabaseProxy = clientDatabaseProxy;
    }

    @GetMapping
    public ResponseEntity<String> index() {

        var results = clientDatabaseProxy.all();

        var responseBuilder = ArrayObjectLeafBuilder.builder();

        results.forEach(clientRow -> {
            responseBuilder.add(ObjectLeafBuilder.builder()
                        .put("id", clientRow.id())
                        .put("name", clientRow.name())
                        .build());
        });

        return new ResponseEntity<>(responseBuilder.build().getLeafValue(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> show(@PathVariable("id") int id) {

        var result = clientDatabaseProxy.byId(String.valueOf(id));

        var response = ObjectLeafBuilder.builder()
                .put("id", result.id())
                .put("name", result.name())
                .put("contact", result.contact())
                .build();

        return new ResponseEntity<>(response.getLeafValue(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> register(@RequestBody String body){

        JsonNode json = null;

        try {
            json = objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        this.clientDatabaseProxy.register(new ClientRegisterRequest(json.get("name").asText(), json.get("contact").asText()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
