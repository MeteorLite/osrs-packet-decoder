/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.deob.deobfuscators.mapping;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.runelite.asm.ClassFile;
import net.runelite.asm.ClassGroup;
import net.runelite.asm.Field;
import net.runelite.asm.Method;
import net.runelite.asm.attributes.code.Instruction;

import java.util.*;
import java.util.Map.Entry;

public class ParallelExecutorMapping
{

	private ClassGroup group, group2;
	private Multimap<Object, Mapping> map = HashMultimap.create();
	public Method m1, m2;
	public boolean crashed;
	public int same;

	public ParallelExecutorMapping(ClassGroup group, ClassGroup group2)
	{
		this.group = group;
		this.group2 = group2;
		assert group != group2;
	}

	@Override
	public String toString()
	{
		return "ParallelExecutorMapping{size = " + map.keySet().size() + ", crashed = " + crashed + ", same = " + same + ", m1 = " + m1 + ", m2 = " + m2 + "}";
	}

	private Mapping getMapping(Object from, Object to)
	{
		for (Mapping m : map.get(from))
		{
			if (m.getObject() == to)
			{
				return m;
			}
		}

		Mapping m = new Mapping(from, to);
		map.put(from, m);
		return m;
	}

	private Object highest(Object from)
	{
		Mapping highest = null;
		for (Mapping m : map.get(from))
		{
			if (highest == null || m.getCount() > highest.getCount())
			{
				highest = m;
			}
			else if (m.getCount() == highest.getCount() && getName(from).compareTo(getName(highest.getObject())) > 0)
			{
				highest = m;
			}
		}
		return highest != null ? highest.getObject() : null;
	}

	public Mapping map(Instruction mapper, Object one, Object two)
	{
		Mapping m = getMapping(one, two);

		if (mapper != null)
		{
			m.addInstruction(mapper);
		}

		belongs(one, group);
		belongs(two, group2);

		sanityCheck(one, two);

		m.inc();

		return m;
	}

	private void sanityCheck(Object one, Object two)
	{
		if (one instanceof Field && two instanceof Field)
		{
			Field f1 = (Field) one;
			Field f2 = (Field) two;

			assert f1.isStatic() == f2.isStatic() : "field map with static mismatch!";
		}
	}

	public Object get(Object o)
	{
		return highest(o);
	}

	public Map<Object, Object> getMap()
	{
		Map<Object, Object> m = new HashMap<>();

		for (Object o : map.keySet())
		{
			m.put(o, highest(o));
		}

		return m;
	}

	private void belongs(Object o, ClassGroup to)
	{
		if (o instanceof Field)
		{
			Field f = (Field) o;
			assert f.getClassFile().getGroup() == to;
		}
		else if (o instanceof Method)
		{
			Method m = (Method) o;
			assert m.getClassFile().getGroup() == to;
		}
		else if (o instanceof ClassFile)
		{
			ClassFile c = (ClassFile) o;
			assert c.getGroup() == to;
		}
		else
		{
			assert false;
		}
	}

	private String getName(Object o)
	{
		if (o instanceof Field)
		{
			Field f = (Field) o;
			return f.getClassFile().getClassName() + "." + f.getName();
		}
		else if (o instanceof Method)
		{
			Method m = (Method) o;
			return m.getClassFile().getClassName() + "." + m.getName();
		}
		else if (o instanceof ClassFile)
		{
			ClassFile c = (ClassFile) o;
			return c.getName();
		}
		else
		{
			assert false;
			return null;
		}
	}
}
