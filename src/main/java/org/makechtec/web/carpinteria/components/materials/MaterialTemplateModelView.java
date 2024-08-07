package org.makechtec.web.carpinteria.components.materials;

public record MaterialTemplateModelView(
        String id,
        String name,
        String unit,
        double unitPrice,
        int quantity
) {
}
