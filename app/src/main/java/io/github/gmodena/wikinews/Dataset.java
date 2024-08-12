package io.github.gmodena.wikinews;

import java.util.ArrayList;

public record Dataset(ArrayList<float[]> vectors, ArrayList<Integer> ids) {
}
