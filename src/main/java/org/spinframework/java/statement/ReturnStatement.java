package org.spinframework.java.statement;

import java.util.List;

import org.spinframework.java.StatementBuilder;
import org.spinframework.java.Variable;

public class ReturnStatement extends StatementBuilder
{
	private Variable input;

	@Override
	public StatementBuilder build(List<Variable> inputs)
	{
		if (inputs != null && !inputs.isEmpty())
		{
			this.input = inputs.get(0);
		}
		return this;
	}

	@Override
	public String toSourceCode()
	{
		return (input == null ? "" : "return " + input.getName() + "; ");
	}
}
