package org.spinframework;

import javax.lang.model.SourceVersion;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.text.WordUtils;

import com.github.javafaker.Faker;

public class NameGenerator
{
	private static final Faker faker = new Faker();

	public static String generateClassName()
	{
		while (true)
		{
			String bz = faker.company().buzzword();
			String adj = faker.hacker().adjective();
			String noun = faker.hacker().noun();
			String str = bz + " " + adj + " " + noun;
			String word = WordUtils.capitalize(WordUtils.capitalizeFully(str).replaceAll("[^a-zA-Z]", ""));

			if (SourceVersion.isName(word))
			{
				return word;
			}
		}
	}

	public static String generateMethodName()
	{
		while (true)
		{
			String verb = faker.resolve("verbs.base");
			String noun = faker.resolve("construction.standard_cost_codes");
			String str = verb + " " + noun;
			String word = WordUtils.capitalize(WordUtils.capitalizeFully(str).replaceAll("[^a-zA-Z]", ""));

			if (SourceVersion.isName(word))
			{
				return word;
			}
		}
	}

	public static String generateVariableName(int minLength, int maxLength)
	{
		while (true)
		{
			String str = generateRandomString(minLength, maxLength);
			String word = WordUtils.uncapitalize(str);

			if (SourceVersion.isName(word))
			{
				return word;
			}
		}
	}

	public static String generateRandomString(int minLength, int maxLength)
	{
		int leng = RandomUtils.nextInt(minLength, maxLength + 1);
		return RandomStringUtils.randomAlphabetic(leng);
	}
}
