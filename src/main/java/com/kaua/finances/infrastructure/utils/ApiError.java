package com.kaua.finances.infrastructure.utils;

import com.kaua.finances.domain.validate.Error;

import java.util.List;

public record ApiError(String message, List<Error> errors) {
}
