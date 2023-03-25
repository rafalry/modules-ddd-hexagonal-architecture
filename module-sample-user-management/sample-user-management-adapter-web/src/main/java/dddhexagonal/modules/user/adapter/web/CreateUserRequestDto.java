/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.user.adapter.web;

public record CreateUserRequestDto(String email, boolean triggerOnboarding) {

}
