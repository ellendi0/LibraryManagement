package com.example.com.example.gateway.validation

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import org.springframework.stereotype.Component

@Component
class Validator {
    fun <T> validate(t: T) {
        val validatorFactory = Validation.buildDefaultValidatorFactory()
        val validator = validatorFactory.validator
        val violations = validator.validate(t)

        if (violations.isNotEmpty()) {
            val errorMessages = violations.map { violation -> "${violation.propertyPath}: ${violation.message}" }
            throw ConstraintViolationException(errorMessages.joinToString("\n"), violations)
        }
    }
}
