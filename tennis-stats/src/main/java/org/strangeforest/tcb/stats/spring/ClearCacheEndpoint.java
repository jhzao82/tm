package org.strangeforest.tcb.stats.spring;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.actuate.endpoint.web.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.strangeforest.tcb.stats.service.*;

import static java.lang.String.*;

@Component @ControllerEndpoint(id = "clearcache")
@Profile("!dev")
public class ClearCacheEndpoint {

	@Autowired private DataService dataService;

	@GetMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE) @ResponseBody
	public String clearCache(
		@RequestParam(name = "name", defaultValue = ".*") String name
	) {
		int cacheCount = dataService.clearCaches(name);
		return format("OK (%1$d caches cleared)", cacheCount);
	}
}