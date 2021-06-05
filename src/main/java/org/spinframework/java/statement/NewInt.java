package org.spinframework.java.statement;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.spinframework.NameGenerator;
import org.spinframework.java.StatementBuilder;
import org.spinframework.java.Variable;

public class NewInt extends StatementBuilder
{
	private int literal;

	@Override
	public StatementBuilder build(List<Variable> inputs)
	{
		literal = RandomUtils.nextInt(1, 10);
		String varName = NameGenerator.generateVariableName(1, 6);
		returnVariable = new Variable(int.class, varName);
		return this;
	}

	@Override
	public String toSourceCode()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("int ").append(returnVariable.getName());
		sb.append(" = ").append(literal).append("; ");
		return sb.toString();
	}
}
