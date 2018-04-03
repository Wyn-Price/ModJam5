package com.wynprice.modjam5.common.core;

import java.util.function.Consumer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
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
					list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/modjam5/common/core/WorldPaintHooksClient", "getProperBiomeColor", "(ILnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lcom/wynprice/modjam5/client/IWorldPaintColorResolver;)I", false));
					method.instructions.insertBefore(ins, list);
					
				}
			}
		}
	};

	private final Consumer<ClassNode> WorldServer = (node) -> {
		for(MethodNode method : node.methods) {
			if(method.name.equals(getName("updateBlocks", "func_147456_g"))) {
				for(int i = 0; i < method.instructions.size(); i++) {
					AbstractInsnNode ins = method.instructions.get(i);
					if(ins.getOpcode() == Opcodes.LDC && ins instanceof LdcInsnNode) {
						LdcInsnNode lIns = (LdcInsnNode) ins;
						if(lIns.cst.equals("randomTick")) {
							InsnList list = new InsnList();
							list.add(new VarInsnNode(Opcodes.ALOAD, 18));
							list.add(new VarInsnNode(Opcodes.ALOAD, 0));
							list.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/util/math/BlockPos"));
							list.add(new InsnNode(Opcodes.DUP));
							list.add(new VarInsnNode(Opcodes.ILOAD, 14));
							list.add(new VarInsnNode(Opcodes.ILOAD, 6));
							list.add(new InsnNode(Opcodes.IADD));
							list.add(new VarInsnNode(Opcodes.ILOAD, 16));
							list.add(new VarInsnNode(Opcodes.ALOAD, 11));
							list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", getName("getYLocation", "func_76662_d"), "()I", false));
							list.add(new InsnNode(Opcodes.IADD));
							list.add(new VarInsnNode(Opcodes.ILOAD, 15));
							list.add(new VarInsnNode(Opcodes.ILOAD, 7));
							list.add(new InsnNode(Opcodes.IADD));
							list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/util/math/BlockPos", "<init>", "(III)V", false));
							list.add(new VarInsnNode(Opcodes.ALOAD, 17));
							list.add(new VarInsnNode(Opcodes.ALOAD, 0));
							list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/WorldServer", getName("rand", "field_73012_v"), "Ljava/util/Random;"));
							list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/modjam5/common/core/WorldPaintHooks", "onRandomTick", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Ljava/util/Random;)V", false));
						
							method.instructions.insert(method.instructions.get(i + 1), list);
						}
					}
				}	
			}
		}
	};
	
	private final Consumer<ClassNode> Block = (node) -> {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getSlipperiness") && method.desc.equals("(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)F")) {
				AbstractInsnNode startLabel = method.instructions.getFirst();
				AbstractInsnNode lineNumberLabel = method.instructions.get(1);
				AbstractInsnNode endLabel = method.instructions.getLast();
				InsnList list = new InsnList();
				list.add(startLabel);
				list.add(lineNumberLabel);
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));//block
				list.add(new VarInsnNode(Opcodes.ALOAD, 1));//state
				list.add(new VarInsnNode(Opcodes.ALOAD, 2));//world
				list.add(new VarInsnNode(Opcodes.ALOAD, 3));//postion
				list.add(new VarInsnNode(Opcodes.ALOAD, 4)); //entity
				
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/block/Block", getName("slipperiness", "field_149765_K"), "F"));
				list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/modjam5/common/core/WorldPaintHooks", "getBlockSlipperiness", "(Lnet/minecraft/block/Block;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)F", false));
				list.add(new InsnNode(Opcodes.FRETURN));
				list.add(endLabel);
				method.instructions = list;
			}
		}
	};
	
	private final Consumer<ClassNode> Entity = (node) -> {
		for(MethodNode method : node.methods) {
			if(method.name.equals(getName("move", "func_70091_d")) && method.desc.equals("(Lnet/minecraft/entity/MoverType;DDD)V")) {
				for(int i = 0; i < method.instructions.size(); i++) {
					AbstractInsnNode ins = method.instructions.get(i);
					if(ins instanceof MethodInsnNode && ins.getOpcode() == Opcodes.INVOKEVIRTUAL) {
						MethodInsnNode mIns = (MethodInsnNode) ins;
						if(mIns.owner.equals("net/minecraft/block/Block") && mIns.name.equals(getName("onLanded", "func_176216_a")) && mIns.desc.equals("(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;)V")) {
							method.instructions.set(ins, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/modjam5/common/core/WorldPaintHooks", "onLanded", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;)V", false));
						}
					}
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
		}  else if(transformedName.equals("net.minecraft.world.WorldServer")) {
			basicClass = runConsumer(WorldServer, transformedName, basicClass);
		} else if(transformedName.equals("net.minecraft.block.Block")) {
			basicClass = runConsumer(Block, transformedName, basicClass);
		}  else if(transformedName.equals("net.minecraft.entity.Entity")) {
			basicClass = runConsumer(Entity, transformedName, basicClass);
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
