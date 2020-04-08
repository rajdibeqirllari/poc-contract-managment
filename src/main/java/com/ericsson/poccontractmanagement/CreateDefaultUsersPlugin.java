package com.ericsson.poccontractmanagement;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateDefaultUsersPlugin extends AbstractProcessEnginePlugin {

	private Logger logger = LoggerFactory.getLogger(CreateDefaultUsersPlugin.class);

	private String[] defaultUsers = new String[] { "satinalmayoneticisi", "satinalmauzmani", "hukukbirimi" };

	public void postProcessEngineBuild(ProcessEngine engine) {

		final IdentityService identityService = engine.getIdentityService();

		if (identityService.isReadOnly()) {
			logger.info("Identity service provider is Read Only, not creating user.");
			return;
		}

		for (int i = 0; i < defaultUsers.length; i++) {
			String userName = defaultUsers[i];
			User singleResult = identityService.createUserQuery().userId(userName).singleResult();
			if (singleResult != null) {
				logger.info("user already exists, no need to create a Admin user");
				return;
			} else {
				logger.info("user does not currently exist");
			}
			logger.info("Generating user");
			User user = identityService.newUser(userName);
			user.setFirstName(userName);
			user.setLastName(userName);
			user.setPassword(userName);
			identityService.saveUser(user);
		}

	}

}