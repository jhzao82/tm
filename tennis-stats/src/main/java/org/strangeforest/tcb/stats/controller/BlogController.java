package org.strangeforest.tcb.stats.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.strangeforest.tcb.stats.model.*;
import org.strangeforest.tcb.stats.service.*;

import static java.util.Arrays.*;

@Controller
public class BlogController extends PageController {

	@Autowired private GOATListService goatListService;

	private static final String DEFAULT_BLOG_SECTION = "newBlogSection";
	private static final Collection<String> VALID_BLOG_SECTIONS = asList(DEFAULT_BLOG_SECTION, "eloKfactorTweaks", "gameEvolution", "mythBusters", "big4CourtSpeed", "h2hSkew");

	@GetMapping("/blog")
	public ModelAndView blog(
		@RequestParam(name = "post", defaultValue = DEFAULT_BLOG_SECTION) String post
	) {
		List<PlayerRanking> goatTopN = goatListService.getGOATTopN(20);
		
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("post", post);
		modelMap.addAttribute("goatTopN", goatTopN);
		return new ModelAndView("blog/blog", modelMap);
	}

	@GetMapping("/blog/{post}")
	public ModelAndView post(
		@PathVariable String post
	) {
		if (!VALID_BLOG_SECTIONS.contains(post))
			post = DEFAULT_BLOG_SECTION;
		return new ModelAndView("blog/" + post);
	}
}
