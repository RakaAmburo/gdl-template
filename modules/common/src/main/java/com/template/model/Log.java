package com.template.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Log {

  private Long id;
  private String type;
  private String origin;
  private String entry;
  private Integer rate;
}
