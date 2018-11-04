package org.strangeforest.tcb.stats.boot;

import java.lang.annotation.*;

import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import org.strangeforest.tcb.stats.web.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JdbcTest(
	includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = VisitorSupport.class)
)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public @interface ServiceTest {}
