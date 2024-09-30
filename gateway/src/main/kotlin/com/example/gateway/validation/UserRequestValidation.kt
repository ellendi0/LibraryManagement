package com.example.com.example.gateway.validation

import com.example.com.example.gateway.validation.model.UserRequestDto
import com.example.internalapi.model.UserInput
import com.example.internalapi.request.user.create.proto.CreateUserRequest
import com.example.internalapi.request.user.update.proto.UpdateUserRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserRequestValidation(private val validator: Validator) {
    fun validate(request: CreateUserRequest): Mono<CreateUserRequest> {
        validator.validate(mapper(request.user))
        return Mono.just(request)
    }

    fun validate(request: UpdateUserRequest): Mono<UpdateUserRequest> {
        validator.validate(mapper(request.user))
        return Mono.just(request)
    }

    private fun mapper(user: UserInput): UserRequestDto {
        return with(user) {
            UserRequestDto(
                id = id,
                firstName = firstName,
                lastName = lastName,
                email = email,
                phoneNumber = phoneNumber,
                password = password
            )
        }
    }
}
