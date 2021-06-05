package org.spinframework.java;

import java.lang.reflect.*;
import java.util.*;

public class FunctionPool
{
	private ArrayList<Method> allMethods = new ArrayList<>();

	private Set<Integer> methodIndexWithoutInput = new HashSet<>();
	private Map<Class<?>, Set<Integer>> methodIndex = new HashMap<>();

	public List<Method> getAllMethods()
	{
		return allMethods;
	}

	private List<Method> getMethodByIndex(Set<Integer> indexes)
	{
		List<Method> ret = new ArrayList<>();
		for (Integer i : indexes)
		{
			ret.add(allMethods.get(i));
		}
		return ret;
	}

	public void addClass(String className, Collection<String> blacklist) throws Exception
	{
		addClass(Class.forName(className), blacklist);
	}

	public void addClass(Class<?> clazz, Collection<String> blacklist) throws Exception
	{
		Set<String> blacklistSet = (blacklist == null ? null : new HashSet<>(blacklist));
		Method[] classMethods = clazz.getMethods();
		for (Method m : classMethods)
		{
			if (blacklistSet != null && blacklistSet.contains(m.getName()))
			{
				continue;
			}
			if (m.isAnnotationPresent(Deprecated.class))
			{
				continue;
			}
			if (!Modifier.isStatic(m.getModifiers()))
			{
				continue;
			}

			allMethods.add(m);
			int idx = allMethods.size() - 1;

			// build index
			Class<?>[] inputTypes = m.getParameterTypes();
			if (inputTypes.length == 0)
			{
				methodIndexWithoutInput.add(idx);
				continue;
			}
			for (Class<?> c : inputTypes)
			{
				methodIndex.computeIfAbsent(c, k -> new HashSet<>()).add(idx);
			}
		}
	}

	public List<Method> findSuitableMethods(Set<Class<?>> inputClasses)
	{
		Set<Integer> retIdx = new HashSet<>();
		if (inputClasses != null && !inputClasses.isEmpty())
		{
			for (Class<?> cls : inputClasses)
			{
				Set<Integer> indexByType = methodIndex.get(cls);
				if (indexByType != null && !indexByType.isEmpty())
				{
					retIdx.addAll(indexByType);
				}
			}
			for (Map.Entry<Class<?>, Set<Integer>> kv : methodIndex.entrySet())
			{
				if (!inputClasses.contains(kv.getKey()))
				{
					retIdx.removeAll(kv.getValue());
				}
			}
		}
		retIdx.addAll(methodIndexWithoutInput);
		return getMethodByIndex(retIdx);
	}
}
