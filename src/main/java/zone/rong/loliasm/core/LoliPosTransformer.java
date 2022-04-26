package zone.rong.loliasm.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class LoliPosTransformer implements IClassTransformer {

    public static final LoliPosTransformer INSTANCE = new LoliPosTransformer();

    public int amountOfClassesWithBlockPosFields, amountOfClassesWithMethodsTakeBlockPos, amountOfClassesWithBlockPosReturningMethods;
    public int blockPosFields, methodsTakeBlockPos, methodsGiveBlockPos;

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        ClassNode node = new ClassNode();
        new ClassReader(bytes).accept(node, 0);

        boolean classFields = false, methodTake = false, methodGive = false;

        for (FieldNode field : node.fields) {
            if (field.desc.equals("Lnet/minecraft/util/math/BlockPos;")) {
                if (!classFields) {
                    classFields = true;
                    amountOfClassesWithBlockPosFields++;
                }
                blockPosFields++;
            }
        }

        for (MethodNode method : node.methods) {
            String[] splits = method.desc.split("\\)");
            if (splits[0].contains("Lnet/minecraft/util/math/BlockPos;")) {
                if (!methodTake) {
                    methodTake = true;
                    amountOfClassesWithMethodsTakeBlockPos++;
                }
                methodsTakeBlockPos++;
            }
            if (splits[1].contains("Lnet/minecraft/util/math/BlockPos;")) {
                if (!methodGive) {
                    methodGive = true;
                    amountOfClassesWithBlockPosReturningMethods++;
                }
                methodsGiveBlockPos++;
            }
        }

        return bytes;
    }

}
