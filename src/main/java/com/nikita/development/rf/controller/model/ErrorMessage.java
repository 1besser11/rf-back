package com.nikita.development.rf.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage <T> {

	T object;
	String error = "Some error";
	
}
