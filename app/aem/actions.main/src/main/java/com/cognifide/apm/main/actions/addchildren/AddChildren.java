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
package com.cognifide.apm.main.actions.addchildren;

import com.cognifide.apm.api.actions.Action;
import com.cognifide.apm.api.actions.ActionResult;
import com.cognifide.apm.api.actions.Context;
import com.cognifide.apm.api.exceptions.ActionExecutionException;
import com.cognifide.apm.main.utils.ActionUtils;
import com.cognifide.apm.main.utils.MessagingUtils;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.RepositoryException;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddChildren implements Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(AddChildren.class);

  private final List<String> authorizableIds;

  public AddChildren(final List<String> authorizableIds) {
    this.authorizableIds = authorizableIds;
  }

  @Override
  public ActionResult simulate(Context context) {
    return process(context, false);
  }

  @Override
  public ActionResult execute(final Context context) {
    return process(context, true);
  }

  private ActionResult process(Context context, boolean execute) {
    ActionResult actionResult = context.createActionResult();
    Group group = null;
    try {
      group = context.getCurrentGroup();
      actionResult.setAuthorizable(group.getID());
      LOGGER.info(String.format("Adding authorizables %s to group with id = %s",
          StringUtils.join(authorizableIds, ", "), group.getID()));
    } catch (ActionExecutionException e) {
      actionResult.logError(MessagingUtils.createMessage(e));
      return actionResult;
    } catch (RepositoryException e) {
      actionResult.logError(MessagingUtils.createMessage(e));
      return actionResult;
    }

    List<String> errors = new ArrayList<>();

    for (String authorizableId : authorizableIds) {
      try {

        Authorizable authorizable = context.getAuthorizableManager().getAuthorizable(authorizableId);

        if (authorizable.isGroup()) {
          ActionUtils.checkCyclicRelations(group, (Group) authorizable);
        }

        if (execute) {
          group.addMember(authorizable);
        }

        actionResult.logMessage(MessagingUtils.addedToGroup(authorizableId, group.getID()));
      } catch (RepositoryException | ActionExecutionException e) {
        errors.add(MessagingUtils.createMessage(e));
      }
    }

    if (!errors.isEmpty()) {
      for (String error : errors) {
        actionResult.logError(error);
      }
      actionResult.logError("Execution interrupted");
    }
    return actionResult;
  }

  @Override
  public boolean isGeneric() {
    return false;
  }

}
