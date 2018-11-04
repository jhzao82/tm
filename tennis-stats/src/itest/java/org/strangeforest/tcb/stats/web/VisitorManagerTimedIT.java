package org.strangeforest.tcb.stats.web;

import java.time.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

@ExtendWith(MockitoExtension.class)
class VisitorManagerTimedIT extends BaseVisitorManagerTest {

	@BeforeEach
	@Override public void setUp() {
		setField(manager, "expiryPeriod", Duration.ofSeconds(1L));
		setField(manager, "expiryCheckPeriod", Duration.ofSeconds(1L));
		super.setUp();
	}

	@Test
	void whenVisitorIsExpiredVisitBySameIPAddressCreatesNewVisitor() throws InterruptedException {
		String ipAddress = "192.168.1.1";

		Visitor visitor = visitAndVerifyFirstVisit(ipAddress);

		when(repository.findAll()).thenReturn(asList(visitor));
		Thread.sleep(2000L);

		verify(repository, atLeast(1)).findAll();
		verify(repository, atLeast(1)).expire(visitorCaptor.capture());
		assertThat(visitorCaptor.getValue().getIpAddress()).isEqualTo(ipAddress);

		visitAndVerifyFirstVisit(ipAddress, times(2));
	}
}
