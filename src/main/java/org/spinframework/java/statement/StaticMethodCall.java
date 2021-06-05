package org.spinframework.java.statement;

import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.lang3.RandomUtils;
import org.spinframework.java.StatementBuilder;
import org.spinframework.java.Variable;

public class StaticMethodCall extends StatementBuilder
{
	private Method method;
	private Map<Class<?>, List<Variable>> inputMap = new HashMap<>();

	public StaticMethodCall(Method method)
	{
		this.method = method;
	}

	@Override
	public StatementBuilder build(List<Variable> inputs)
	{
		for (Variable v : inputs)
		{
			inputMap.computeIfAbsent(v.getClazz(), k -> new ArrayList<>()).add(v);
		}
		return this;
	}

	@Override
	public Set<String> getClassImports()
	{
		return Collections.singleton(method.getDeclaringClass().getName());
	}

	@Override
	public String toSourceCode()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(method.getDeclaringClass().getSimpleName()).append(".").append(method.getName());
		sb.append("(");
		Class<?>[] paramClasses = method.getParameterTypes();
		if (paramClasses != null && paramClasses.length > 0)
		{
			for (int i = 0; i < paramClasses.length; ++i)
			{
				Variable input = findSuitableInput(paramClasses[i]);
				if (input != null)
				{
					if (i > 0)
					{
						sb.append(',');
					}
					sb.append(input.getName());
				}
			}
		}
		sb.append("); ");
		return sb.toString();
	}

	private Variable findSuitableInput(Class<?> paramClass)
	{
		List<Variable> vars = inputMap.get(paramClass);
		if (vars != null && !vars.isEmpty())
		{
			int randomIdx = RandomUtils.nextInt(0, vars.size());
			return vars.get(randomIdx);
		}
		return null;
	}
}
