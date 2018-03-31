package com.wynprice.modjam5.common.core;

import java.util.function.Consumer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.wynprice.modjam5.client.IWorldPaintColorResolver;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;

public class WorldPaintTransformer implements IClassTransformer {
	
	private final Consumer<ClassNode> BiomeColorHelper = (node) -> {
		for(MethodNode method : node.methods) {
			if(method.name.equals(getName("getColorAtPos", "func_180285_a "))) {
				AbstractInsnNode ins = method.instructions.get(method.instructions.size() - 2);
				if(ins.getOpcode() == Opcodes.IRETURN) {
					InsnList list = new InsnList();
					list.add(new VarInsnNode(Opcodes.ALOAD, 0));
					list.add(new VarInsnNode(Opcodes.ALOAD, 1));
					list.add(new VarInsnNode(Opcodes.ALOAD, 2));
					list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/modjam5/common/core/WorldPaintHooks", "getProperBiomeColor", "(ILnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lcom/wynprice/modjam5/client/IWorldPaintColorResolver;)I", false));
					method.instructions.insertBefore(ins, list);
					
				}
			}
		}
	};
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(transformedName.equals("net.minecraft.world.biome.BiomeColorHelper")) {
			basicClass = runConsumer(BiomeColorHelper, transformedName, basicClass);
		} else if(transformedName.equals("net.minecraft.world.biome.BiomeColorHelper$ColorResolver")) {
			basicClass = runConsumer((node) -> node.interfaces.add(Type.getInternalName(IWorldPaintColorResolver.class)), transformedName, basicClass);
		}
		return basicClass;
		
	}
	
	private byte[] runConsumer(Consumer<ClassNode> cons, String transformedName, byte[] basicClass) {			
		FMLLog.info("[WorldPaintTransformer] Running Transform on " + transformedName);
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
		cons.accept(node);
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
	    node.accept(writer);
	    return writer.toByteArray();
	}
	
	private String getName(String workspaceName, String mcpName) {
		return WorldPaintCore.isDebofEnabled ? mcpName : workspaceName;
	}
}
