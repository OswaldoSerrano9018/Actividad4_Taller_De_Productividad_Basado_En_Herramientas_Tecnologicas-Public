package org.makechtec.web.carpinteria.http.services.bom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.makechtec.software.json_tree.builders.ArrayObjectLeafBuilder;
import org.makechtec.software.json_tree.builders.ObjectLeafBuilder;
import org.makechtec.web.carpinteria.components.bom.BOMDatabaseProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/bom")
public class BOMApiController {

    private final ObjectMapper objectMapper;
    private final BOMDatabaseProxy bomDatabaseProxy;

    @Autowired
    public BOMApiController(ObjectMapper objectMapper, BOMDatabaseProxy bomDatabaseProxy) {
        this.objectMapper = objectMapper;
        this.bomDatabaseProxy = bomDatabaseProxy;
    }

    @GetMapping
    public ResponseEntity<String> index(){

        var responseBuilder = ArrayObjectLeafBuilder.builder();

        bomDatabaseProxy.all()
                .forEach(bomPreview -> {
                    responseBuilder.add(ObjectLeafBuilder.builder()
                            .put("id", bomPreview.id())
                            .build());
                } );

        var response = responseBuilder.build();

        return new ResponseEntity<>(response.getLeafValue(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> show( @PathVariable String id ){

        var materialsBuilder = ArrayObjectLeafBuilder.builder();

        bomDatabaseProxy.allAttachedMaterialsForBOM(id)
                .forEach(attachedMaterial -> {
                    materialsBuilder.add(ObjectLeafBuilder.builder()
                                    .put("id", attachedMaterial.materialId())
                                    .put("name", attachedMaterial.name())
                                    .put("quantity", attachedMaterial.quantity())
                            .build());
                });

        var response = ObjectLeafBuilder.builder()
                .put("id", id)
                .put("materials", materialsBuilder.build())
                .build();

        return new ResponseEntity<>(response.getLeafValue(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody String body){


        var bomId = bomDatabaseProxy.create();

        try {
            var materials = (ArrayNode) objectMapper.readTree(body).get("data").get("materials");

            for(JsonNode materialNode: materials){
                bomDatabaseProxy.attachMaterialProductToBOM(
                        materialNode.get("id").asText(),
                        bomId,
                        materialNode.get("quantity").asInt()
                );
            }

            return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
