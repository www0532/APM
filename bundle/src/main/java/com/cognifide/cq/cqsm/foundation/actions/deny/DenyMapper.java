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
package com.cognifide.cq.cqsm.foundation.actions.deny;

import static com.cognifide.cq.cqsm.foundation.actions.CommonFlags.IF_EXISTS;

import com.cognifide.cq.cqsm.api.actions.Action;
import com.cognifide.cq.cqsm.api.actions.annotations.Flag;
import com.cognifide.cq.cqsm.api.actions.annotations.Flags;
import com.cognifide.cq.cqsm.api.actions.annotations.Mapper;
import com.cognifide.cq.cqsm.api.actions.annotations.Mapping;
import com.cognifide.cq.cqsm.api.actions.annotations.Named;
import com.cognifide.cq.cqsm.api.actions.annotations.Required;
import com.cognifide.cq.cqsm.foundation.actions.ActionGroup;
import java.util.List;

@Mapper(value = "deny", group = ActionGroup.CORE)
public class DenyMapper {

  public static final String REFERENCE = "This action is an complementary one for ALLOW action, and can be used to"
      + " add deny permission for current authorizable on specified path. Script fails if path doesn't exist.";

  @Mapping(
      examples = {
          "DENY '/content/dam' [READ]",
          "DENY '/content/dam' properties= ['jcr:title'] [MODIFY]",
          "DENY '/content/dam' properties= ['nt:folder'] [MODIFY]",
          "DENY '/content/dam/domain' [READ, MODIFY] --IF-EXISTS"
      },
      reference = REFERENCE
  )
  public Action create(
      @Required(value = "path", description = "e.g.: '/content/dam'") String path,
      @Required(value = "permissions", description = "e.g.: [READ, 'jcr:all']") List<String> permissions,
      @Named(value = "glob", description = "regular expression to narrow set of paths") String glob,
      @Named(value = "types", description = "list of jcr types which will be affected") List<String> types,
      @Named(value = "properties", description = "list of properties which will be affected ") List<String> items,
      @Flags({
          @Flag(value = IF_EXISTS, description = "script doesn't fail if path doesn't exist")}) List<String> flags) {
    return new Deny(path, permissions, glob, types, items, flags.contains(IF_EXISTS));
  }
}
