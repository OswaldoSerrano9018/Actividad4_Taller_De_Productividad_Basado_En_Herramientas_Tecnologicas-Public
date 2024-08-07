package org.makechtec.web.carpinteria.components.bom;

public record AttachedMaterial(
        String bomId,
        String materialId,
        String name,
        int quantity
) {
}
