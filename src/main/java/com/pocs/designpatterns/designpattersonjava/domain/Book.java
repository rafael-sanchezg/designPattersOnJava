package com.pocs.designpatterns.designpattersonjava.domain;

/**
 * Book domain model.
 */
public record Book(
    int id,
    String title,
    String author,
    String type,
    String format,
    String state
) {}

