package org.makechtec.web.carpinteria.http.services.materials;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.makechtec.software.json_tree.builders.ArrayObjectLeafBuilder;
import org.makechtec.software.json_tree.builders.ObjectLeafBuilder;
import org.makechtec.web.carpinteria.components.materials.MaterialTemplateDatabaseProxy;
import org.makechtec.web.carpinteria.components.materials.MaterialTemplateRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/material/template")
public class MaterialTemplateApiController {

    private final MaterialTemplateDatabaseProxy materialTemplateDatabaseProxy;

    @Autowired
    public MaterialTemplateApiController(MaterialTemplateDatabaseProxy materialTemplateDatabaseProxy) {
        this.materialTemplateDatabaseProxy = materialTemplateDatabaseProxy;
    }

    @GetMapping
    public ResponseEntity<String> index(){

        var results = materialTemplateDatabaseProxy.all();

        var responseBuilder = ArrayObjectLeafBuilder.builder();

        results.forEach(materialTemplateRow -> {
            responseBuilder.add(ObjectLeafBuilder.builder()
                        .put("id", materialTemplateRow.id())
                        .put("name", materialTemplateRow.name())
                        .build());
        });

        return new ResponseEntity<>(responseBuilder.build().getLeafValue(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> edit(@PathVariable("id") int id) {


        var current = materialTemplateDatabaseProxy.byId(String.valueOf(id));

        var response  = ObjectLeafBuilder.builder()
                .put("id", current.id())
                .put("name", current.name())
                .put("unit", current.unit())
                .put("unitPrice", current.unitPrice())
                .build();

        return ResponseEntity.ok(response.getLeafValue());
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody String body){

        var mapper = new ObjectMapper();

        try {

            var tree = mapper.readTree(body);

            materialTemplateDatabaseProxy.register(new MaterialTemplateRegisterRequest(
                    tree.get("name").asText(),
                    tree.get("unit").asText(),
                    tree.get("unitPrice").asDouble()
            ));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().build();
    }

}
