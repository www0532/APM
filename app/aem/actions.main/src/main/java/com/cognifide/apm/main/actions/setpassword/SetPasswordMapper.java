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
package com.cognifide.apm.main.actions.setpassword;

import com.cognifide.apm.api.actions.Action;
import com.cognifide.apm.api.actions.annotations.Mapper;
import com.cognifide.apm.api.actions.annotations.Mapping;
import com.cognifide.apm.api.actions.annotations.Required;
import com.cognifide.apm.main.actions.ActionGroup;

@Mapper(value = "set-password", group = ActionGroup.CORE)
public final class SetPasswordMapper {

  @Mapping(
      examples = "SET-PASSWORD 'p@$$w0rd'",
      reference = "Set new password for a current user."
  )
  public Action mapAction(@Required(value = "password", description = "password e.g.: 'p@$$w0rd'") String password) {
    return new SetPassword(password);
  }

}
