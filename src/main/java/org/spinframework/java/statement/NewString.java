package org.spinframework.java.statement;

import java.util.List;

import org.spinframework.NameGenerator;
import org.spinframework.java.StatementBuilder;
import org.spinframework.java.Variable;

public class NewString extends StatementBuilder
{
	private String literal;

	@Override
	public StatementBuilder build(List<Variable> inputs)
	{
		literal = NameGenerator.generateRandomString(10, 15);
		String varName = NameGenerator.generateVariableName(1, 6);
		returnVariable = new Variable(String.class, varName);
		return this;
	}

	@Override
	public String toSourceCode()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(returnVariable.getClazz().getSimpleName());
		sb.append(" ").append(returnVariable.getName());
		sb.append(" = \"").append(literal).append("\"; ");
		return sb.toString();
	}
}
