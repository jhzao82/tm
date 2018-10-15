package org.strangeforest.tcb.stats.controller;

import java.time.*;
import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.strangeforest.tcb.stats.model.price.*;
import org.strangeforest.tcb.stats.service.*;

public abstract class PageController extends BaseController {

	@Autowired protected DataService dataService;

	@ModelAttribute("dataUpdate")
	public LocalDate getDataUpdate() {
		return dataService.getDataUpdate();
	}

	@ModelAttribute("priceFormats")
	public PriceFormat[] getPriceFormats() {
		return PriceFormat.values();
	}

    @ModelAttribute("language")
    public String getLanguage(HttpServletRequest request) {
        String language = request.getParameter("language");

        if (language != null && !language.isEmpty()) {
            return "/" + language;
        }

        return "";
    }
}
