package org.spinframework.java.statement;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.spinframework.NameGenerator;
import org.spinframework.java.StatementBuilder;
import org.spinframework.java.Variable;

public class NewChar extends StatementBuilder
{
	private char literal;

	@Override
	public StatementBuilder build(List<Variable> inputs)
	{
		literal = RandomStringUtils.randomAlphanumeric(1).charAt(0);
		String varName = NameGenerator.generateVariableName(1, 6);
		returnVariable = new Variable(char.class, varName);
		return this;
	}

	@Override
	public String toSourceCode()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("char ").append(returnVariable.getName());
		sb.append(" = '").append(literal).append("'; ");
		return sb.toString();
	}
}
