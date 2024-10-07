package com.mowitnow.model;

import java.io.Serializable;

/**
 * Record representing the lawn dimensions
 */
public record Lawn(int width, int height)  implements Serializable {
}
