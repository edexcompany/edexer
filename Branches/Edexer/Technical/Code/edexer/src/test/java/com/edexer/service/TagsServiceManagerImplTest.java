/*package com.edexer.service;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.edexer.model.Tags;
import com.edexer.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class TagsServiceManagerImplTest extends TestCase {

	@Autowired
	TagsServiceManager tagsService;

	// @Test
	public void getTagsList() {
		User user = new User();
		user.setUserId(1);

		List<Tags> list = tagsService.getPersonalTagsList(user);
		for (Tags tag : list) {
			System.out.println(tag.getTagName());
		}
	}

	// @Test
	public void getTag() {

		Tags t = tagsService.get(2);
		System.out.println(tagsService.get(2).getTagName());
	}

	@Test
	public void test() {

	}
}
*/