package com.edexer.mbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.edexer.service.LookUpsServiceManager;
import com.edexer.service.TagsServiceManager;
import com.edexer.auth.HttpSessionUtil;
import com.edexer.model.Tags;
import com.edexer.model.User;

@ManagedBean(name = "tagsManagedBean", eager = true)
@ApplicationScoped
public class TagsManagedBean implements Serializable {

	@ManagedProperty("#{tagsServiceManager}")
	private TagsServiceManager tagsService;

	public List<Tags> getPersonalTagsList(String query) {
		User user = (User) HttpSessionUtil.getSession().getAttribute(
				Constants.USER);
		List<Tags> allTags = tagsService.getPersonalTagsList(user);
		List<Tags> filteredTags = new ArrayList<Tags>();

		for (int i = 0; i < allTags.size(); i++) {
			Tags tag = allTags.get(i);
			if (tag.getTagName().toLowerCase().startsWith(query)) {
				filteredTags.add(tag);
			}
		}
		return filteredTags;
	}

	public List<Tags> getCorporateTagsList(String query) {
		User user = (User) HttpSessionUtil.getSession().getAttribute(
				Constants.USER);
		List<Tags> allTags = getTagsService().getCorporateTagsList(user);
		List<Tags> filteredTags = new ArrayList<Tags>();

		for (int i = 0; i < allTags.size(); i++) {
			Tags tag = allTags.get(i);
			if (tag.getTagName().toLowerCase().startsWith(query)) {
				filteredTags.add(tag);
			}
		}
		return filteredTags;

	}

	public List<Tags> getTagsListByUser(String query) {
		User user = (User) HttpSessionUtil.getSession().getAttribute(
				Constants.USER);

		List<Tags> allTags = getTagsService().getCorporateTagsList(user);
		for (Tags _tag : getTagsService().getPersonalTagsList(user)) {
			allTags.add(_tag);
		}
		List<Tags> filteredTags = new ArrayList<Tags>();

		for (int i = 0; i < allTags.size(); i++) {
			Tags tag = allTags.get(i);
			if (tag.getTagName().toLowerCase().startsWith(query)) {
				filteredTags.add(tag);
			}
		}
		return filteredTags;

	}

	public Tags getTag(int tagId) {
		return tagsService.get(tagId);
	}

	public TagsServiceManager getTagsService() {
		return tagsService;
	}

	public void setTagsService(TagsServiceManager tagsService) {
		this.tagsService = tagsService;
	}

}
