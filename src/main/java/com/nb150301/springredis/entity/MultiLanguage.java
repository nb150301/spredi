package com.nb150301.springredis.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MultiLanguage implements Serializable {
  private String en;
  private String ru;
}
