package org.spinframework.java;

import java.util.Arrays;

import org.apache.commons.lang3.ArchUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class BuilderCentral
{
	private FunctionPool funcPool;

	public BuilderCentral() throws Exception
	{
		funcPool = new FunctionPool();

		funcPool.addClass(ArchUtils.class, null);

		funcPool.addClass(CharUtils.class, Arrays.asList("toIntValue"));

		funcPool.addClass(StringUtils.class, Arrays.asList("abbreviate", "abbreviateMiddle", "getBytes"));

		funcPool.addClass(SystemUtils.class, null);

		funcPool.addClass(NumberUtils.class, Arrays.asList("createBigDecimal", "createBigInteger", "createDouble",
			"createFloat", "createInteger", "createLong", "createNumber", "toScaledBigDecimal"));
	}

	public FunctionPool getFunctionPool()
	{
		return funcPool;
	}
}
