package org.spinframework.mojo;

import java.io.*;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.spinframework.NameGenerator;
import org.spinframework.java.BuilderCentral;
import org.spinframework.java.ClassBuilder;
import org.spinframework.junit.TestClassBuilder;

/**
 * @phase generate-sources
 * @goal spin
 * 
 * @author robert
 */
public class SpinMojo extends AbstractMojo
{
	/**
	 * @parameter property = "mainJavaDirectory"
	 */
	protected File mainJavaDirectory;

	/**
	 * @parameter property = "testJavaDirectory"
	 */
	protected File testJavaDirectory;

	/**
	 * @parameter property = "packageName"
	 */
	protected String packageName;

	/**
	 * @parameter property = "numberOfFiles"
	 */
	protected Integer numberOfFiles;

	private BuilderCentral builderCentral;

	// --------------------------------------------------

	private void init() throws Exception
	{
		builderCentral = new BuilderCentral();
		getLog().debug("Total functions loaded: " + builderCentral.getFunctionPool().getAllMethods().size());
	}

	private void initDirectory(File dir) throws IOException
	{
		dir.mkdirs();
		FileUtils.cleanDirectory(dir);
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		try
		{
			init();

			File finalJavaDir = getFinalJavaDir();
			File finalTestDir = getFinalTestDir();
			initDirectory(finalJavaDir);
			initDirectory(finalTestDir);

			for (int i = 0; i < numberOfFiles; ++i)
			{
				generateJavaFile(finalJavaDir.getPath());
			}
			getLog().info("Total java source files generated: " + numberOfFiles);
			getLog().info("");

			for (File javaFile : finalJavaDir.listFiles())
			{
				generateTestFile(finalTestDir.getPath(), javaFile);
			}

		}
		catch (Exception e)
		{
			throw new MojoFailureException("Failed to generate files", e);
		}
	}

	private void generateJavaFile(String finalJavaDirPath) throws IOException
	{
		String className = NameGenerator.generateClassName();
		String fileName = className + ".java";
		getLog().info("Generating " + fileName);

		String filePath = finalJavaDirPath + File.separator + fileName;
		String sourceCode = new ClassBuilder(builderCentral).setPackageName(packageName).setClassName(className).build()
			.toSourceCode();
		FileUtils.writeStringToFile(new File(filePath), sourceCode, Charset.defaultCharset());
	}

	private void generateTestFile(String finalTestDirPath, File javaFile) throws IOException
	{
		String className = StringUtils.substringBefore(javaFile.getName(), '.') + "Test";
		String fileName = className + ".java";
		getLog().info("Generating " + fileName);

		String filePath = finalTestDirPath + File.separator + fileName;
		String sourceCode = new TestClassBuilder().setJavaFile(javaFile).toSourceCode();
		FileUtils.writeStringToFile(new File(filePath), sourceCode, Charset.defaultCharset());
	}

	// ----------------------------------------

	private File getFinalJavaDir()
	{
		String javaDirPath = mainJavaDirectory.getPath();
		String finalJavaDirPath = javaDirPath + "/" + (packageName.replace('.', '/'));
		return new File(FilenameUtils.separatorsToSystem(finalJavaDirPath));
	}

	private File getFinalTestDir()
	{
		String testDirPath = testJavaDirectory.getPath();
		String finalTestDirPath = testDirPath + "/" + (packageName.replace('.', '/'));
		return new File(FilenameUtils.separatorsToSystem(finalTestDirPath));
	}
}
