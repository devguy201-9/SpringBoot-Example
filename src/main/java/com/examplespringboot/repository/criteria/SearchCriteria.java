package com.examplespringboot.repository.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String key; // lastName, firstName, email, id, ...
    private String operation; // <, >, =, ...
    private Object value;
}
