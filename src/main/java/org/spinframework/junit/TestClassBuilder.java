package org.spinframework.junit;

import java.io.*;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class TestClassBuilder
{
	private File javaFile;
	private CompilationUnit compUnit;

	public TestClassBuilder setJavaFile(File javaFile)
	{
		this.javaFile = javaFile;
		return this;
	}

	public String toSourceCode()
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			parseJava(javaFile);
			String javaClassName = getJavaClassName();

			sb.append("package ").append(getJavaPackageName()).append("; ");
			sb.append("import org.junit.*; ");
			sb.append("public class ").append(javaClassName).append("Test ");
			sb.append("{");
			sb.append("@Test public void test").append(javaClassName).append("() ");
			sb.append("throws Exception ");
			sb.append("{");
			sb.append(javaClassName).append(" me = new ").append(javaClassName).append("(); ");

			new VoidVisitorAdapter<Object>()
			{
				@Override
				public void visit(MethodDeclaration node, Object arg)
				{
					if (node.isCallableDeclaration())
					{
						sb.append("me.").append(node.getName()).append("(); ");
					}
				}
			}.visit(compUnit, null);

			sb.append("Assert.assertNotNull(me); ");
			sb.append("}}");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String getJavaPackageName() throws ParseException
	{
		Optional<PackageDeclaration> opt = compUnit.getPackageDeclaration();
		if (!opt.isPresent())
		{
			throw new ParseException("no PackageDeclaration");
		}
		return opt.get().getName().asString();
	}

	private String getJavaClassName() throws ParseException
	{
		Optional<String> opt = compUnit.getPrimaryTypeName();
		if (!opt.isPresent())
		{
			throw new ParseException("no PrimaryTypeName");
		}
		return opt.get();
	}

	private void parseJava(File javaFile) throws IOException, ParseException
	{
		ParseResult<CompilationUnit> parseResult = new JavaParser().parse(javaFile);
		Optional<CompilationUnit> opt = parseResult.getResult();
		if (!opt.isPresent())
		{
			for (Problem prob : parseResult.getProblems())
			{
				System.err.println(prob.getVerboseMessage());
			}
			throw new ParseException("no CompilationUnit");
		}
		this.compUnit = opt.get();
	}
}
