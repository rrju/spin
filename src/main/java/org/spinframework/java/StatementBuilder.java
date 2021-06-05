package org.spinframework.java;

import java.util.*;

public abstract class StatementBuilder
{
	protected Variable returnVariable;

	public abstract String toSourceCode();

	public abstract StatementBuilder build(List<Variable> inputs);

	public StatementBuilder build()
	{
		return build(Collections.emptyList());
	}

	public StatementBuilder build(Variable input)
	{
		return build(Collections.singletonList(input));
	}

	public Set<String> getClassImports()
	{
		return Collections.emptySet();
	}

	public Variable getReturnVariable()
	{
		return returnVariable;
	}
}
