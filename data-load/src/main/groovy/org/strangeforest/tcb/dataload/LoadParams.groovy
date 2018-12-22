package org.strangeforest.tcb.dataload

class LoadParams {

	static final String BASE_DIR_PROPERTY = 'tcb.data.base-dir'
	static final String FULL_LOAD_PROPERTY = 'tcb.data.full-load'
	static final String MAX_RANK_PROPERTY = 'tcb.data.max-rank'
	static final String VERBOSE_PROPERTY = 'tcb.data.verbose'
	static final String RECORD_PAUSE_PROPERTY = 'tcb.data.record-pause'
	static final String FORCE_FORECAST_PROPERTY = 'tcb.data.force-forecast'
	static final String THREADS_PROPERTY = 'tcb.data.threads'

	static final boolean FULL_LOAD_DEFAULT = true
	static final boolean VERBOSE_DEFAULT = false
	static final long RECORD_PAUSE_DEFAULT = 2000L
	static final boolean FORCE_FORECAST_DEFAULT = false
	static final int THREADS_DEFAULT = Runtime.runtime.availableProcessors()

	static Boolean getBooleanProperty(String name, Boolean defValue = null) {
		System.getProperty(name, defValue?.toString())?.toBoolean()
	}

	static Integer getIntProperty(String name, Integer defValue = null) {
		System.getProperty(name, defValue?.toString())?.toInteger()
	}

	static Long getLongProperty(String name, Long defValue = null) {
		System.getProperty(name, defValue?.toString())?.toLong()
	}
}
