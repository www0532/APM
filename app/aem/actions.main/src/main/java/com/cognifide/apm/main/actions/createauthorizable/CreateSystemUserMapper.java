/*-
 * ========================LICENSE_START=================================
 * AEM Permission Management
 * %%
 * Copyright (C) 2013 Cognifide Limited
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package com.cognifide.apm.main.actions.createauthorizable;

import static com.cognifide.apm.main.actions.CommonFlags.IF_NOT_EXISTS;

import com.cognifide.apm.api.actions.Action;
import com.cognifide.apm.api.actions.annotations.Flag;
import com.cognifide.apm.api.actions.annotations.Flags;
import com.cognifide.apm.api.actions.annotations.Mapper;
import com.cognifide.apm.api.actions.annotations.Mapping;
import com.cognifide.apm.api.actions.annotations.Named;
import com.cognifide.apm.api.actions.annotations.Required;
import com.cognifide.apm.main.actions.ActionGroup;
import java.util.List;

@Mapper(value = "create-system-user", group = ActionGroup.CORE)
public class CreateSystemUserMapper {

  public static final String REFERENCE = "Create a system user. Script fails if user already exists.";

  @Mapping(
      examples = {
          "CREATE-USER 'apm-user'",
          "CREATE-USER 'apm-user' password= 'p@$$w0rd' --IF-NOT-EXISTS",
          "CREATE-USER 'apm-user' path= '/home/users/client/domain'"
      },
      reference = REFERENCE
  )
  public Action mapAction(
      @Required(value = "userId", description = "user's login e.g.: 'apm-user'") String userId,
      @Named(value = "password", description = "user's password e.g.: 'p@$$w0rd'") String password,
      @Named(value = "path", description = "user's home e.g.: '/home/users/domain'") String path,
      @Flags({@Flag(value = IF_NOT_EXISTS, description = "action is executed only if user doesn't exist, "
          + "and script doesn't fail in that case")}) List<String> flags) {
    return new CreateAuthorizable(userId, null, path, flags.contains(IF_NOT_EXISTS), CreateAuthorizableStrategy.SYSTEM_USER);
  }

}
