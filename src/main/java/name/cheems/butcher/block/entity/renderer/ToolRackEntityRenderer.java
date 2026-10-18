package name.cheems.butcher.block.entity.renderer;

import name.cheems.butcher.block.custom.ToolRackBlock;
import name.cheems.butcher.block.entity.custom.ToolRackEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class ToolRackEntityRenderer implements BlockEntityRenderer<ToolRackEntity> {
    public ToolRackEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(ToolRackEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        Direction facing = entity.getCachedState().get(ToolRackBlock.FACING);

        matrices.push();

        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(facing.asRotation()));

        double offsetZ = (facing == Direction.EAST || facing == Direction.WEST) ? 0.45 : -0.45;

        double[] offsetsX = {-0.3, 0.0, 0.3};

        for (int i = 0; i < 3; i++) {
            ItemStack stack = entity.getStack(i);

            if (stack.isEmpty()) {
                continue;
            }

            matrices.push();

            matrices.translate(offsetsX[i], 0.0, offsetZ);

            matrices.scale(0.75f, 0.75f, 0.75f);

            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(225));

            itemRenderer.renderItem(stack, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(), entity.getPos()),
                    OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);

            matrices.pop();
        }

        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
