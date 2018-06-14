package org.sp.poc.sling.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class UserInfo {
	@Inject
	private String text;


	public String getText() {
		return text;
	}
}