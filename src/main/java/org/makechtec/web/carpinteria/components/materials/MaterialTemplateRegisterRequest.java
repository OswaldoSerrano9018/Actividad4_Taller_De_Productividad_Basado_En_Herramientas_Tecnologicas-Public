package org.makechtec.web.carpinteria.components.materials;

public record MaterialTemplateRegisterRequest(
        String name,
        String unit,
        double unitPrice
) {
}
