package org.spinframework.java;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.spinframework.NameGenerator;

public class ClassBuilder
{
	private BuilderCentral builderCentral;

	private String packageName;
	private String className;
	private int methodCount = 16;
	private List<MethodBuilder> methods = new ArrayList<>();

	public ClassBuilder(BuilderCentral builderCentral)
	{
		this.builderCentral = builderCentral;
	}

	public ClassBuilder setPackageName(String packageName)
	{
		this.packageName = packageName;
		return this;
	}

	public ClassBuilder setClassName(String className)
	{
		this.className = className;
		return this;
	}

	public ClassBuilder setMethodCount(int methodCount)
	{
		this.methodCount = methodCount;
		return this;
	}

	public ClassBuilder build()
	{
		for (int i = 0; i < methodCount; ++i)
		{
			MethodBuilder mb = new MethodBuilder(builderCentral).setMethodName(NameGenerator.generateMethodName());
			methods.add(mb.build());
		}
		return this;
	}

	private Set<String> getClassImports()
	{
		Set<String> classImports = new HashSet<>();
		for (MethodBuilder mb : methods)
		{
			classImports.addAll(mb.getClassImports());
		}
		return classImports;
	}

	public String toSourceCode()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(packageName).append("; ");
		for (String classImport : getClassImports())
		{
			if (!StringUtils.substringBeforeLast(classImport, ".").equals("java.lang"))
			{
				sb.append("import ").append(classImport).append("; ");
			}
		}
		sb.append("public class ").append(className);
		sb.append("{");
		for (MethodBuilder mb : methods)
		{
			sb.append(mb.toSourceCode());
		}
		sb.append("}");
		return sb.toString();
	}
}
