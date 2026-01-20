package akki697222.vanillatech.client.renderer.machine;

import akki697222.vanillatech.api.client.renderer.MachineBlockRenderer;
import akki697222.vanillatech.api.common.block.machine.MachineBlock;
import akki697222.vanillatech.api.common.block.machine.MachineBlockEntity;
import akki697222.vanillatech.common.machine.arcfurnace.ArcFurnaceBlock;
import akki697222.vanillatech.common.machine.arcfurnace.ArcFurnaceBlockEntity;
import akki697222.vanillatech.common.machine.boiler.solidfuel.SolidFuelBoilerBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import static akki697222.vanillatech.common.VTPartialModels.ARC_FURNACE_ELECTRODE;
import static akki697222.vanillatech.common.VTPartialModels.ARC_FURNACE_ELECTRODE_ACTIVE;

@OnlyIn(Dist.CLIENT)
public class SolidFuelBoilerRenderer extends MachineBlockRenderer<SolidFuelBoilerBlockEntity> {
    public SolidFuelBoilerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull MachineBlockEntity be, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (be.getBlockState().getValue(MachineBlock.SLAVE)) return;

        // JSONモデル(models/block/solid_fuel_boiler.json)を取得
        BlockState state = be.getBlockState();
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);

        poseStack.pushPose();

        poseStack.translate(0, 1, 0);

        Direction facing = state.getValue(MachineBlock.FACING);
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        poseStack.translate(-0.5, 0, -0.5);

        var renderType = RenderType.CUTOUT;
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);

        int lightAbove = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above());

        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                poseStack.last(),
                vertexConsumer,
                state,
                model,
                1.0f, 1.0f, 1.0f,
                lightAbove,
                packedOverlay,
                ModelData.EMPTY,
                renderType
        );

        poseStack.popPose();
    }

    private float getRotationAngle(Direction facing) {
        return switch (facing) {
            case EAST  -> 90;
            case WEST  -> -90;
            case NORTH -> 180;
            default    -> 0;
        };
    }
}
