package com.empty.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.empty.controller.SessionController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/*.xml" })
public class TestController {
	private MockMvc mvc;

	@Before
	public void setup() {
		//SessionController sc = new SessionController();
		CommentController api = new CommentController();
		mvc = MockMvcBuilders.standaloneSetup(api).build();
	}

	@Test
	public void testGetSequence() {
		try {
			mvc.perform(MockMvcRequestBuilders.get("/api/comment/load?videoId=1")).andExpect(MockMvcResultMatchers.status().is(200));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}