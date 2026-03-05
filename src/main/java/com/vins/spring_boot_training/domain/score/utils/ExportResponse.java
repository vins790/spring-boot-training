package com.vins.spring_boot_training.domain.score.utils;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Getter
public class ExportResponse {
  public static ResponseEntity<byte[]> build(byte[] data, String fileName, MediaType mediaType) {
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
        .contentType(mediaType)
        .contentLength(data.length)
        .body(data);
  }
}
