package org.makechtec.web.carpinteria.components.product;

public record ProductRegisterRequest(
        String name,
        String bomId,
        String dimensions
) {
}
