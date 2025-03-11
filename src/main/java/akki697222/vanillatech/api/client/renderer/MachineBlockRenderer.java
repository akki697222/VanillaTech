package akki697222.vanillatech.api.client.renderer;

import akki697222.vanillatech.api.common.block.machine.MachineBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public abstract class MachineBlockRenderer<T extends MachineBlockEntity> implements BlockEntityRenderer<T> {
    protected final BlockEntityRendererProvider.Context context;

    public MachineBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public abstract void render(@NotNull MachineBlockEntity machineBlockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay);
}
