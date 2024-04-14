package com.nb150301.springredis.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class MultiLanguage implements Serializable {
  private String en;
  private String ru;
}
