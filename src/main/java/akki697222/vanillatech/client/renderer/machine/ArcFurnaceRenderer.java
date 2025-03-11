package akki697222.vanillatech.client.renderer.machine;

import akki697222.vanillatech.api.client.renderer.MachineBlockRenderer;
import akki697222.vanillatech.api.common.block.machine.MachineBlock;
import akki697222.vanillatech.api.common.block.machine.MachineBlockEntity;
import akki697222.vanillatech.common.machines.arcfurnace.ArcFurnaceBlock;
import akki697222.vanillatech.common.machines.arcfurnace.ArcFurnaceBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import static akki697222.vanillatech.common.VTPartialModels.ARC_FURNACE_ELECTRODE;
import static akki697222.vanillatech.common.VTPartialModels.ARC_FURNACE_ELECTRODE_ACTIVE;

@OnlyIn(Dist.CLIENT)
public class ArcFurnaceRenderer extends MachineBlockRenderer<ArcFurnaceBlockEntity> {
    public ArcFurnaceRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull MachineBlockEntity machineBlockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        boolean hasElectrode = machineBlockEntity.getBlockState().getValue(ArcFurnaceBlock.HAS_ELECTRODE);

        if (!machineBlockEntity.getBlockState().getValue(MachineBlock.SLAVE) && hasElectrode) {
            poseStack.pushPose();

            poseStack.translate(0, 2, 0);

            poseStack.translate(0.5, 1.0, 0.5);
            float rotationAngle = getRotationAngle(machineBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING));
            poseStack.mulPose(Axis.YP.rotationDegrees(rotationAngle));
            poseStack.translate(-0.5, -1.0, -0.5);

            BakedModel model = Minecraft.getInstance().getModelManager().getModel(machineBlockEntity.getBlockState().getValue(MachineBlock.ACTIVE) ? ARC_FURNACE_ELECTRODE_ACTIVE : ARC_FURNACE_ELECTRODE);

            context.getBlockRenderDispatcher().getModelRenderer().renderModel(
                    poseStack.last(),
                    buffer.getBuffer(RenderType.solid()),
                    null,
                    model,
                    1.0f, 1.0f, 1.0f,
                    packedLight,
                    packedOverlay,
                    ModelData.EMPTY,
                    RenderType.solid()
            );

            poseStack.popPose();
        }
    }

    private float getRotationAngle(Direction facing) {
        return switch (facing) {
            case EAST  -> 90;
            case SOUTH -> 0;
            case WEST  -> -90;
            case NORTH -> 180;
            default    -> 0;
        };
    }
}
