package com.example.diplomchick.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPassword {
    String value;
    String salt;
}
