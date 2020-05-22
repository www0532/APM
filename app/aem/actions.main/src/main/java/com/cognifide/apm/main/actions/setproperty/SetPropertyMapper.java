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
package com.cognifide.apm.main.actions.setproperty;

import com.cognifide.apm.api.actions.Action;
import com.cognifide.apm.api.actions.annotations.Mapper;
import com.cognifide.apm.api.actions.annotations.Mapping;
import com.cognifide.apm.api.actions.annotations.Required;
import com.cognifide.apm.main.actions.ActionGroup;

@Mapper(value = "set-property", group = ActionGroup.CORE)
public final class SetPropertyMapper {

  @Mapping(
      examples = "SET-PROPERTY 'jcr:title' 'title'",
      reference = "This is general purpose action which can be used to assign specified value to the specified property."
  )
  public Action mapAction(
      @Required(value = "name", description = "property's name e.g.: 'jcr:title'") String name,
      @Required(value = "value", description = "property's value e.g.: 'title'") String value) {
    return new SetProperty(name, value);
  }

}