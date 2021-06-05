package org.spinframework.java;

public class Variable
{
	private Class<?> clazz;
	private String name;

	public Variable(Class<?> clazz, String name)
	{
		this.clazz = clazz;
		this.name = name;
	}

	public Class<?> getClazz()
	{
		return clazz;
	}

	public String getName()
	{
		return name;
	}
}
