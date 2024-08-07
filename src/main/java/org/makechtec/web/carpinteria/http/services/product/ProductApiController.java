package org.makechtec.web.carpinteria.http.services.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.makechtec.software.json_tree.builders.ArrayObjectLeafBuilder;
import org.makechtec.software.json_tree.builders.ObjectLeafBuilder;
import org.makechtec.web.carpinteria.components.product.ProductDatabaseProxy;
import org.makechtec.web.carpinteria.components.product.ProductRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/product")
public class ProductApiController {

    private final ProductDatabaseProxy productDatabaseProxy;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProductApiController(ProductDatabaseProxy productDatabaseProxy, ObjectMapper objectMapper) {
        this.productDatabaseProxy = productDatabaseProxy;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<String> index(){

        var results = productDatabaseProxy.all();

        var responseBuilder = ArrayObjectLeafBuilder.builder();

        results.forEach(productRow -> {
            responseBuilder.add(ObjectLeafBuilder.builder()
                            .put("id", productRow.id())
                            .put("name", productRow.name())
                            .build());
        });

        return new ResponseEntity<>(responseBuilder.build().getLeafValue(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<String> show(@PathVariable String id){
        var result = productDatabaseProxy.byid(id);

        var response = ObjectLeafBuilder.builder()
                .put("id", result.id())
                .put("name", result.name())
                .put("bomId", result.bomId())
                .put("dimensions", result.dimensions())
                .build();

        return new ResponseEntity<>(response.getLeafValue(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody String body){
        JsonNode json = null;

        try {
            json = objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        this.productDatabaseProxy.register(new ProductRegisterRequest(
                json.get("name").asText(),
                json.get("bomId").asText(),
                json.get("dimensions").asText()
        ));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
