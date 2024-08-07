package org.makechtec.web.carpinteria.components.product;

import java.util.Set;

public record Product(
        String name,
        Set<String> features
) {
}
