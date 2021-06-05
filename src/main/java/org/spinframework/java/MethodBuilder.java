package org.spinframework.java;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomUtils;
import org.spinframework.java.statement.*;

public class MethodBuilder
{
	private BuilderCentral builderCentral;

	private String methodName;
	private int statementCount = 25;
	private List<StatementBuilder> statements = new ArrayList<>();
	private List<Variable> variables = new ArrayList<>();
	private Variable returnVariable;

	public MethodBuilder(BuilderCentral builderCentral)
	{
		this.builderCentral = builderCentral;
	}

	public MethodBuilder setMethodName(String methodName)
	{
		this.methodName = methodName;
		return this;
	}

	public MethodBuilder setStatementCount(int statementCount)
	{
		this.statementCount = statementCount;
		return this;
	}

	private void addStatement(StatementBuilder stmt)
	{
		Variable newVar = stmt.getReturnVariable();
		if (newVar != null)
		{
			String newVarName = newVar.getName();
			for (Variable v : variables)
			{
				if (v.getName().equals(newVarName))
				{
					return;
				}
			}
			variables.add(newVar);
		}
		statements.add(stmt);
	}

	public Set<String> getClassImports()
	{
		Set<String> ret = new HashSet<>();
		for (StatementBuilder stmt : statements)
		{
			ret.addAll(stmt.getClassImports());
		}
		if (returnVariable != null && !returnVariable.getClazz().isPrimitive())
		{
			ret.add(returnVariable.getClazz().getName());
		}
		return ret;
	}

	private Set<Class<?>> getAllVariableClasses()
	{
		return variables.stream().map(Variable::getClazz).collect(Collectors.toSet());
	}

	public MethodBuilder build()
	{
		addStatement(new NewChar().build());
		addStatement(new NewInt().build());
		addStatement(new NewDouble().build());
		addStatement(new NewString().build());
		addStatement(new NewString().build());

		for (int i = 0; i < statementCount; ++i)
		{
			Set<Class<?>> varCLasses = getAllVariableClasses();
			List<Method> suitableMethods = builderCentral.getFunctionPool().findSuitableMethods(varCLasses);
			if (suitableMethods != null && !suitableMethods.isEmpty())
			{
				int randomIdx = RandomUtils.nextInt(0, suitableMethods.size());

				StatementBuilder stmt = new StaticMethodCall(suitableMethods.get(randomIdx));
				statements.add(stmt.build(variables));
			}
		}

		chooseReturnVariable();
		addStatement(new ReturnStatement().build(returnVariable));
		return this;
	}

	private void chooseReturnVariable()
	{
		int cnt = variables.size();
		int randomIdx = RandomUtils.nextInt(0, cnt + 1);
		if (randomIdx < cnt)
		{
			returnVariable = variables.get(randomIdx);
		}
	}

	public String toSourceCode()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("public ");
		sb.append(returnVariable == null ? "void" : returnVariable.getClazz().getSimpleName());
		sb.append(" ").append(methodName).append("() ");
		sb.append("throws Exception ");
		sb.append("{");
		for (StatementBuilder stmt : statements)
		{
			sb.append(stmt.toSourceCode());
		}
		sb.append("}");
		return sb.toString();
	}
}
