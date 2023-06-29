package ru.plorum.reporterinitializr.model;

import jakarta.mail.util.ByteArrayDataSource;

public record Attachment(String name, ByteArrayDataSource value) {
}
